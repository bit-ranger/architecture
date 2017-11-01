package com.rainyalley.jss;

import redis.clients.util.Pool;

import java.util.List;


public class ShardedSentinelJedisPool extends Pool<ShardedSentinelJedis> {

    private ShardedSentinelJedis shardedSentinelJedis;

    public ShardedSentinelJedisPool(List<JedisShardSentinelInfo> jedisShardSentinelInfoList) {
        shardedSentinelJedis = new ShardedSentinelJedis(jedisShardSentinelInfoList);

    }

    public ShardedSentinelJedis getResource() {
        return shardedSentinelJedis;
    }
}