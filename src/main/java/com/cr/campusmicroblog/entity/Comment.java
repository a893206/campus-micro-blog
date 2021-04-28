package com.cr.campusmicroblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cr
 * @date 2020-11-17 16:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;

    private String content;

    private Integer userId;

    private Integer entityId;

    private Integer entityType;

    private Date createdDate;

    private Integer status;
}
