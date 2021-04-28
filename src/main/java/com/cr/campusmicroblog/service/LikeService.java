package com.cr.campusmicroblog.service;

/**
 * @author cr
 * @date 2020-11-18 16:06
 */
public interface LikeService {
    /**
     * 获取用户点赞状态
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 默认0，点赞1，点踩-1
     */
    int getLikeStatus(int userId, int entityType, int entityId);

    /**
     * 用户点赞
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 点赞数
     */
    long like(int userId, int entityType, int entityId);

    /**
     * 用户点踩
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 点赞数
     */
    long disLike(int userId, int entityType, int entityId);
}
