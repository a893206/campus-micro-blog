package com.cr.campusmicroblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.async.EventModel;
import com.cr.campusmicroblog.async.EventProducer;
import com.cr.campusmicroblog.async.EventType;
import com.cr.campusmicroblog.entity.LoginTicket;
import com.cr.campusmicroblog.entity.User;
import com.cr.campusmicroblog.mapper.LoginTicketMapper;
import com.cr.campusmicroblog.mapper.UserMapper;
import com.cr.campusmicroblog.service.UserService;
import com.cr.campusmicroblog.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cr
 * @date 2020-11-16 23:41
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public User getUser(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>(2);

        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", username));

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userMapper.insert(user);

        // 登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>(2);

        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", username));

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                .setActorId(user.getId())
                .setExt("email", username));

        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketMapper.insert(ticket);
        return ticket.getTicket();
    }

    @Override
    public void logout(String ticket) {
        LoginTicket loginTicket = loginTicketMapper.selectOne(new QueryWrapper<LoginTicket>().eq("ticket", ticket));
        loginTicket.setStatus(1);
        loginTicketMapper.updateById(loginTicket);
    }
}
