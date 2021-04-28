package com.cr.campusmicroblog.service;

import com.cr.campusmicroblog.entity.Comment;
import com.github.pagehelper.PageInfo;

/**
 * @author cr
 * @date 2020-11-17 16:54
 */
public interface CommentService {
    /**
     * 添加评论
     *
     * @param comment 评论对象
     * @return 添加结果
     */
    int addComment(Comment comment);

    /**
     * 根据实体类型获取评论
     *
     * @param entityId   实体类型id
     * @param entityType 实体类型
     * @param pageNum    页码
     * @param pageSize   每页显示数量
     * @return 评论分页对象集合
     */
    PageInfo<Comment> getCommentsByEntity(int entityId, int entityType, int pageNum, int pageSize);

    /**
     * 获取评论数量
     *
     * @param entityId   实体类型id
     * @param entityType 实体类型
     * @return 评论数量
     */
    int getCommentCount(int entityId, int entityType);
}
