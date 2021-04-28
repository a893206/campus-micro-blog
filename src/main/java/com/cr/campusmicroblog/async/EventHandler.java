package com.cr.campusmicroblog.async;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-18 22:59
 */
public interface EventHandler {
    /**
     * 处理事件
     *
     * @param model 事件模型
     */
    void doHandle(EventModel model);

    /**
     * 相关事件类型集合
     *
     * @return 事件类型集合
     */
    List<EventType> getSupportEventTypes();
}
