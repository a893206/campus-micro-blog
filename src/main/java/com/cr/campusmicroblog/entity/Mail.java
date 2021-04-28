package com.cr.campusmicroblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cr
 * @date 2020-11-19 13:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private Integer id;

    private String host;

    private String username;

    private String password;

    private Integer port;
}
