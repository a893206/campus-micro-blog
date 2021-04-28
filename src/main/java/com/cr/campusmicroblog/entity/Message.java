package com.cr.campusmicroblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cr
 * @date 2020-11-17 17:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String content;

    private Date createdDate;

    private Integer hasRead;

    private String conversationId;

    /**
     * 站内信详情会话数量
     */
    @TableField(exist = false)
    private int cnt;
}
