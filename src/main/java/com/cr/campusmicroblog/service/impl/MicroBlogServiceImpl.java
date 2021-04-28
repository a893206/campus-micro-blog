package com.cr.campusmicroblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.entity.MicroBlog;
import com.cr.campusmicroblog.mapper.MicroBlogMapper;
import com.cr.campusmicroblog.service.MicroBlogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cr
 * @date 2020-11-16 23:31
 */
@Service
public class MicroBlogServiceImpl implements MicroBlogService {
    @Autowired
    private MicroBlogMapper microBlogMapper;

    @Override
    public PageInfo<MicroBlog> getLatestMicroBlog(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<MicroBlog> queryWrapper = new QueryWrapper<>();
        if (userId != 0) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.orderByDesc("id");
        return new PageInfo<>(microBlogMapper.selectList(queryWrapper), 5);
    }

    @Override
    public int addMicroBlog(MicroBlog microBlog) {
        microBlogMapper.insert(microBlog);
        return microBlog.getId();
    }

    @Override
    public MicroBlog getById(Integer microBlogId) {
        return microBlogMapper.selectById(microBlogId);
    }

    @Override
    public void updateCommentCount(Integer microBlogId, int count) {
        MicroBlog microBlog = new MicroBlog();
        microBlog.setId(microBlogId);
        microBlog.setCommentCount(count);
        microBlogMapper.updateById(microBlog);
    }

    @Override
    public void updateLikeCount(int microBlogId, int likeCount) {
        MicroBlog microBlog = new MicroBlog();
        microBlog.setId(microBlogId);
        microBlog.setLikeCount(likeCount);
        microBlogMapper.updateById(microBlog);
    }
}
