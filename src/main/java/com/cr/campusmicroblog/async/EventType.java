package com.cr.campusmicroblog.async;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cr
 * @date 2020-11-18 22:44
 */
@AllArgsConstructor
public enum EventType {
    /**
     * 点赞0
     * 评论1
     * 登录2
     * 邮件3
     */
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    @Getter
    private final int value;
}
