package com.cr.campusmicroblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cr
 * @date 2020-11-16 23:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    private String name;

    private String password;

    private String salt;

    private String headUrl;
}
