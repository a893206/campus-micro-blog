package com.cr.campusmicroblog.service;

import com.cr.campusmicroblog.entity.MicroBlog;
import com.github.pagehelper.PageInfo;

/**
 * @author cr
 * @date 2020-11-16 23:21
 */
public interface MicroBlogService {
    /**
     * 获取最近微博
     *
     * @param userId   用户id
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return 微博分页对象集合
     */
    PageInfo<MicroBlog> getLatestMicroBlog(int userId, int pageNum, int pageSize);

    /**
     * 添加微博
     *
     * @param microBlog 微博对象
     * @return 添加结果
     */
    int addMicroBlog(MicroBlog microBlog);

    /**
     * 根据微博id查询微博
     *
     * @param microBlogId 微博id
     * @return 微博对象
     */
    MicroBlog getById(Integer microBlogId);

    /**
     * 更新评论数量
     *
     * @param microBlogId 微博id
     * @param count       评论数量
     */
    void updateCommentCount(Integer microBlogId, int count);

    /**
     * 更新点赞数
     *
     * @param microBlogId 微博id
     * @param likeCount   点赞数
     */
    void updateLikeCount(int microBlogId, int likeCount);
}
