package com.cr.campusmicroblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.entity.Message;
import com.cr.campusmicroblog.mapper.MessageMapper;
import com.cr.campusmicroblog.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-17 20:59
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int addMessage(Message msg) {
        return messageMapper.insert(msg);
    }

    @Override
    public PageInfo<Message> getConversationList(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(messageMapper.getConversationList(userId), 5);
    }

    @Override
    public PageInfo<Message> getConversationDetail(String conversationId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.orderByDesc("id");
        return new PageInfo<>(messageMapper.selectList(queryWrapper), 5);
    }

    @Override
    public int getConversationUnReadCount(int userId, String conversationId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("has_read", 0).eq("to_id", userId);
        if (conversationId != null) {
            queryWrapper.eq("conversation_id", conversationId);
        }
        return messageMapper.selectCount(queryWrapper);
    }

    @Override
    public void hasRead(int userId, String conversationId) {
        Message message = new Message();
        message.setHasRead(1);

        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", userId).eq("conversation_id", conversationId);
        messageMapper.update(message, queryWrapper);
    }
}
