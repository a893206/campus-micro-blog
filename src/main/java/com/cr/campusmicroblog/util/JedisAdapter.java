package com.cr.campusmicroblog.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author cr
 * @date 2020-11-18 15:59
 */
@Slf4j
@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool pool = null;

    public Jedis getJedis() {
        return pool.getResource();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return null;
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }
    }

    public long sadd(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public long srem(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public boolean sismember(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return false;
        }
    }

    public long scard(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.scard(key);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public long lpush(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return 0;
        }
    }

    public List<String> brpop(int timeout, String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
            return null;
        }
    }

    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
