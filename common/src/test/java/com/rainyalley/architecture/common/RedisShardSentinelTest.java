package com.rainyalley.architecture.common;

import com.rainyalley.jedis.ss.ShardedSentinelJedis;
import com.rainyalley.jedis.ss.ShardedSentinelJedisPool;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.logging.LogManager;

/**
 * InvokeCacheAspect Tester.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-common.xml")
public class RedisShardSentinelTest {

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private ShardedSentinelJedisPool shardedSentinelJedisPool;


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: around(ProceedingJoinPoint joinPoint)
     */
    @Test
    public void testGet() throws Exception {
        ShardedSentinelJedis ssj = shardedSentinelJedisPool.getResource();
        for (int i = 0; i < 100; i++) {
            String key = "test" + i;
            String value = "hello world" + i;
            ssj.set(key, value);
            String result = ssj.get(key);
            Assert.assertEquals(value, result);
        }
    }



} 
