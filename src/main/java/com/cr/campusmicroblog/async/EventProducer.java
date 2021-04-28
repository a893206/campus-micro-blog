package com.cr.campusmicroblog.async;

/**
 * @author cr
 * @date 2020-11-18 23:04
 */
public interface EventProducer {
    /**
     * 启动事件
     *
     * @param eventModel 事件类型
     * @return 启动结果
     */
    boolean fireEvent(EventModel eventModel);
}
