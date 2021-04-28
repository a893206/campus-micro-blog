package com.cr.campusmicroblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.entity.Comment;
import com.cr.campusmicroblog.mapper.CommentMapper;
import com.cr.campusmicroblog.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-17 16:58
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public PageInfo<Comment> getCommentsByEntity(int entityId, int entityType, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_id", entityId).eq("entity_type", entityType);
        queryWrapper.orderByDesc("id");
        return new PageInfo<>(commentMapper.selectList(queryWrapper), 5);
    }


    @Override
    public int getCommentCount(int entityId, int entityType) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_id", entityId).eq("entity_type", entityType);
        return commentMapper.selectCount(queryWrapper);
    }
}
