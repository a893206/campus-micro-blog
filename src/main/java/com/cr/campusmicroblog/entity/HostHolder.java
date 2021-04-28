package com.cr.campusmicroblog.entity;

import org.springframework.stereotype.Component;

/**
 * @author cr
 * @date 2020-11-17 13:05
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}
