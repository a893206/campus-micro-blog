package com.cr.campusmicroblog.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cr
 * @date 2020-11-16 23:14
 */
@Data
public class ViewObject {
    private final Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
