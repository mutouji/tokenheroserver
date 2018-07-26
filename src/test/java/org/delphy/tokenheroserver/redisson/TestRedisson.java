package org.delphy.tokenheroserver.redisson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisson {
    @Autowired
    private RedissonClient redissonClient;
    @Test
    public void testRedisson() {
        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("rd");
        // with ttl = 10 seconds
        Integer prevValue = mapCache.put("key1", 10, 10, TimeUnit.SECONDS);
        // with ttl = 15 seconds and maxIdleTime = 5 seconds
        Integer prevValue2 = mapCache.put("key2", 20, 5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS);
    }
}
