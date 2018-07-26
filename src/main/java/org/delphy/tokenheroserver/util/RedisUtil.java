package org.delphy.tokenheroserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mutouji
 */
@Component
public class RedisUtil {
    private RedisTemplate redisTemplate;

    public RedisUtil(@Autowired RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> void setObject(String key, T t) {
        redisTemplate.opsForValue().set(key, t);
    }
    @SuppressWarnings("unchecked")
    public <T> void setObjectMinite(String key, T t, long time) {
        redisTemplate.opsForValue().set(key, t, time, TimeUnit.MINUTES);
    }
}
