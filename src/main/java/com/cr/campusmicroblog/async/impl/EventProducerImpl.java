package com.cr.campusmicroblog.async.impl;

import com.alibaba.fastjson.JSONObject;
import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventProducer;
import com.cr.campusmicroblog.util.JedisAdapter;
import com.cr.campusmicroblog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-18 23:06
 */
@Service
public class EventProducerImpl implements EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
