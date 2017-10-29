package com.rainyalley.architecture.common.datasource;

import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.ShardInfo;

public class JedisShardSentinelInfo extends ShardInfo<JedisSentinelPool> {

    private JedisSentinelPool jedisSentinelPool;

    private JedisShardInfo jedisShardInfo;

    public JedisShardSentinelInfo(JedisSentinelPool jedisSentinelPool, JedisShardInfo jedisShardInfo){
        this.jedisSentinelPool = jedisSentinelPool;
        this.jedisShardInfo = jedisShardInfo;
    }


    @Override
    protected JedisSentinelPool createResource() {
        return jedisSentinelPool;
    }

    @Override
    public String getName() {
        return jedisShardInfo.getName();
    }
}
