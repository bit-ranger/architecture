package com.rainyalley.jss;

import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.ShardInfo;

public class JedisShardSentinelInfo extends ShardInfo<JedisSentinelPool> {

    private JedisSentinelPool jedisSentinelPool;

    private String shardName;

    public JedisShardSentinelInfo(JedisSentinelPool jedisSentinelPool, String shardName, int weight){
        super(weight);
        this.jedisSentinelPool = jedisSentinelPool;
        this.shardName = shardName;
    }


    @Override
    protected JedisSentinelPool createResource() {
        return jedisSentinelPool;
    }

    @Override
    public String getName() {
        return shardName;
    }
}
