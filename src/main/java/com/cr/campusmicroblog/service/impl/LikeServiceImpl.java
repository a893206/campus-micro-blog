package com.cr.campusmicroblog.service.impl;

import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventProducer;
import com.cr.campusmicroblog.async.EventType;
import com.cr.campusmicroblog.entity.MicroBlog;
import com.cr.campusmicroblog.service.LikeService;
import com.cr.campusmicroblog.service.MicroBlogService;
import com.cr.campusmicroblog.util.JedisAdapter;
import com.cr.campusmicroblog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-18 16:09
 */
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private MicroBlogService microBlogService;

    @Override
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    @Override
    public long like(int userId, int entityType, int entityId) {
        //修改当前点赞状态
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String value = String.valueOf(userId);

        MicroBlog microBlog = microBlogService.getById(entityId);
        EventModel eventModel = new EventModel(EventType.LIKE)
                .setActorId(userId)
                .setEntityType(entityType).setEntityId(entityId)
                .setEntityOwnerId(microBlog.getUserId());
        if (jedisAdapter.sismember(likeKey, value)) {
            jedisAdapter.srem(likeKey, value);
            eventModel.setExt("msg", "取消点赞了你的微博");
        } else {
            jedisAdapter.sadd(likeKey, value);
            eventModel.setExt("msg", "点赞了你的微博");
        }
        eventProducer.fireEvent(eventModel);

        //从点踩里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, value);
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }

    @Override
    public long disLike(int userId, int entityType, int entityId) {
        //修改当前点踩状态
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        String value = String.valueOf(userId);

        MicroBlog microBlog = microBlogService.getById(entityId);
        EventModel eventModel = new EventModel(EventType.LIKE)
                .setActorId(userId)
                .setEntityType(entityType).setEntityId(entityId)
                .setEntityOwnerId(microBlog.getUserId());
        if (jedisAdapter.sismember(disLikeKey, value)) {
            jedisAdapter.srem(disLikeKey, value);
            eventModel.setExt("msg", "取消点踩了你的微博");
        } else {
            jedisAdapter.sadd(disLikeKey, value);
            eventModel.setExt("msg", "点踩了你的微博");
        }
        eventProducer.fireEvent(eventModel);

        //从点赞里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, value);
        return jedisAdapter.scard(likeKey) - jedisAdapter.scard(disLikeKey);
    }
}
