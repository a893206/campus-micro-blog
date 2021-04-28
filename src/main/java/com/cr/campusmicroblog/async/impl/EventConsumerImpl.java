package com.cr.campusmicroblog.async.impl;

import com.alibaba.fastjson.JSONObject;
import com.cr.campusmicroblog.async.EventConsumer;
import com.cr.campusmicroblog.async.EventHandler;
import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventType;
import com.cr.campusmicroblog.util.JedisAdapter;
import com.cr.campusmicroblog.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cr
 * @date 2020-11-18 23:13
 */
@Slf4j
@Service
public class EventConsumerImpl implements EventConsumer {
    private final Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
            List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
            for (EventType type : eventTypes) {
                List<EventHandler> list = config.getOrDefault(type, new ArrayList<>());
                list.add(entry.getValue());
                config.put(type, list);
            }
        }

        new Thread(() -> {
            while (true) {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);
                for (String message : events) {
                    if (message.equals(key)) {
                        continue;
                    }

                    EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        log.error("不能识别的事件");
                        continue;
                    }

                    for (EventHandler handler : config.get(eventModel.getType())) {
                        handler.doHandle(eventModel);
                    }
                }
            }
        }).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
