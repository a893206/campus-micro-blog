package com.cr.campusmicroblog.service;

import com.cr.campusmicroblog.entity.Message;
import com.github.pagehelper.PageInfo;

/**
 * @author cr
 * @date 2020-11-17 20:58
 */
public interface MessageService {
    /**
     * 添加消息
     *
     * @param msg 消息对象
     * @return 添加结果
     */
    int addMessage(Message msg);

    /**
     * 获取用户站内信列表
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return 消息分页对象集合
     */
    PageInfo<Message> getConversationList(int userId, int pageNum, int pageSize);

    /**
     * 根据会话id获取站内信详情
     *
     * @param conversationId 会话id
     * @param pageNum        页码
     * @param pageSize       每页显示数量
     * @return 消息分页对象集合
     */
    PageInfo<Message> getConversationDetail(String conversationId, int pageNum, int pageSize);

    /**
     * 获取用户未读消息数量
     *
     * @param userId         用户id
     * @param conversationId 会话id
     * @return 未读消息数量
     */
    int getConversationUnReadCount(int userId, String conversationId);

    /**
     * 已读消息
     *
     * @param userId         当前用户id
     * @param conversationId 会话id
     */
    void hasRead(int userId, String conversationId);
}
