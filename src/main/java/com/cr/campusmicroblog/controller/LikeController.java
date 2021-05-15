package com.cr.campusmicroblog.controller;

import com.cr.campusmicroblog.entity.EntityType;
import com.cr.campusmicroblog.entity.HostHolder;
import com.cr.campusmicroblog.entity.User;
import com.cr.campusmicroblog.service.LikeService;
import com.cr.campusmicroblog.service.MicroBlogService;
import com.cr.campusmicroblog.util.MicroBlogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cr
 * @date 2020-11-18 16:18
 */
@Slf4j
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MicroBlogService microBlogService;

    @PostMapping("/like")
    public String like(@Param("newId") int microBlogId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return MicroBlogUtils.getJSONString(1, "用户未登录");
        }
        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_MICRO_BLOG, microBlogId);
        //更新点赞数
        microBlogService.updateLikeCount(microBlogId, (int) likeCount);
        return MicroBlogUtils.getJSONString(0, String.valueOf(likeCount));
    }

    @PostMapping("/dislike")
    public String dislike(@Param("newId") int microBlogId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return MicroBlogUtils.getJSONString(1, "用户未登录");
        }
        long likeCount = likeService.disLike(user.getId(), EntityType.ENTITY_MICRO_BLOG, microBlogId);
        //更新点赞数
        microBlogService.updateLikeCount(microBlogId, (int) likeCount);
        return MicroBlogUtils.getJSONString(0, String.valueOf(likeCount));
    }
}
