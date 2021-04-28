package com.cr.campusmicroblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cr
 * @date 2020-11-17 12:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTicket {
    private Integer id;

    private Integer userId;

    private String ticket;

    private Date expired;

    private Integer status;
}
