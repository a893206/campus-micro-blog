package com.cr.campusmicroblog.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.entity.HostHolder;
import com.cr.campusmicroblog.entity.LoginTicket;
import com.cr.campusmicroblog.entity.User;
import com.cr.campusmicroblog.mapper.LoginTicketMapper;
import com.cr.campusmicroblog.mapper.UserMapper;
import com.cr.campusmicroblog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author cr
 * @date 2020-11-17 13:00
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketMapper.selectOne(new QueryWrapper<LoginTicket>().eq("ticket", ticket));
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            User user = userMapper.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (modelAndView != null && user != null) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("unreadCount", messageService.getConversationUnReadCount(user.getId(), null));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
