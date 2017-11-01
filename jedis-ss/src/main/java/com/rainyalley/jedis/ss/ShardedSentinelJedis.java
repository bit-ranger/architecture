package com.rainyalley.jedis.ss;

import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Sharded;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShardedSentinelJedis extends Sharded<JedisSentinelPool, JedisShardSentinelInfo> implements JedisCommands, Closeable{

    public ShardedSentinelJedis(List<JedisShardSentinelInfo> shards) {
        super(shards);
    }

    @Override
    public String set(String key, String value) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.set(key, value, nxxx, expx, time);
        jedis.close();
        return result;
    }

    @Override
    public String set(String key, String value, String nxxx) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.set(key, value, nxxx);
        jedis.close();
        return result;
    }

    @Override
    public String get(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public Boolean exists(String key) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.exists(key);
        jedis.close();
        return result;
    }

    @Override
    public Long persist(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.persist(key);
        jedis.close();
        return result;
    }

    @Override
    public String type(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.type(key);
        jedis.close();
        return result;
    }

    @Override
    public Long expire(String key, int seconds) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.expire(key, seconds);
        jedis.close();
        return result;
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.pexpire(key, milliseconds);
        jedis.close();
        return result;
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.expireAt(key, unixTime);
        jedis.close();
        return result;
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.pexpireAt(key, millisecondsTimestamp);
        jedis.close();
        return result;
    }

    @Override
    public Long ttl(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.ttl(key);
        jedis.close();
        return result;
    }

    @Override
    public Long pttl(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.pttl(key);
        jedis.close();
        return result;
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.setbit(key, offset, value);
        jedis.close();
        return result;
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.setbit(key, offset, value);
        jedis.close();
        return result;
    }

    @Override
    public Boolean getbit(String key, long offset) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.getbit(key, offset);
        jedis.close();
        return result;
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.setrange(key, offset, value);
        jedis.close();
        return result;
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.getrange(key, startOffset, endOffset);
        jedis.close();
        return result;
    }

    @Override
    public String getSet(String key, String value) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.getSet(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long setnx(String key, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.setnx(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String setex(String key, int seconds, String value) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.setex(key, seconds, value);
        jedis.close();
        return result;
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.psetex(key, milliseconds, value);
        jedis.close();
        return result;
    }

    @Override
    public Long decrBy(String key, long integer) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.decrBy(key, integer);
        jedis.close();
        return result;
    }

    @Override
    public Long decr(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.decr(key);
        jedis.close();
        return result;
    }

    @Override
    public Long incrBy(String key, long integer) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.incrBy(key, integer);
        jedis.close();
        return result;
    }

    @Override
    public Double incrByFloat(String key, double value) {
        
        Jedis jedis = shardJedis(key);
        Double result =  jedis.incrByFloat(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long incr(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.incr(key);
        jedis.close();
        return result;
    }

    @Override
    public Long append(String key, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.append(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String substr(String key, int start, int end) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.substr(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long hset(String key, String field, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.hset(key, field, value);
        jedis.close();
        return result;
    }

    @Override
    public String hget(String key, String field) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.hget(key, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.hsetnx(key, field, value);
        jedis.close();
        return result;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.hmset(key, hash);
        jedis.close();
        return result;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.hmget(key, fields);
        jedis.close();
        return result;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.hincrBy(key, field, value);
        jedis.close();
        return result;
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        
        Jedis jedis = shardJedis(key);
        Double result =  jedis.hincrByFloat(key, field, value);
        jedis.close();
        return result;
    }

    @Override
    public Boolean hexists(String key, String field) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.hexists(key, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String key, String... field) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.hdel(key, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hlen(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.hlen(key);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> hkeys(String key) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.hkeys(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> hvals(String key) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.hvals(key);
        jedis.close();
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        
        Jedis jedis = shardJedis(key);
        Map<String, String> result =  jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    @Override
    public Long rpush(String key, String... string) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.rpush(key, string);
        jedis.close();
        return result;
    }

    @Override
    public Long lpush(String key, String... string) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.lpush(key, string);
        jedis.close();
        return result;
    }

    @Override
    public Long llen(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.llen(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.lrange(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public String ltrim(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.ltrim(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public String lindex(String key, long index) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.lindex(key, index);
        jedis.close();
        return result;
    }

    @Override
    public String lset(String key, long index, String value) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.lset(key, index, value);
        jedis.close();
        return result;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.lrem(key, count, value);
        jedis.close();
        return result;
    }

    @Override
    public String lpop(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.lpop(key);
        jedis.close();
        return result;
    }

    @Override
    public String rpop(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.rpop(key);
        jedis.close();
        return result;
    }

    @Override
    public Long sadd(String key, String... member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.sadd(key, member);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> smembers(String key) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.smembers(key);
        jedis.close();
        return result;
    }

    @Override
    public Long srem(String key, String... member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.srem(key, member);
        jedis.close();
        return result;
    }

    @Override
    public String spop(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.spop(key);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> spop(String key, long count) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.spop(key, count);
        jedis.close();
        return result;
    }

    @Override
    public Long scard(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.scard(key);
        jedis.close();
        return result;
    }

    @Override
    public Boolean sismember(String key, String member) {
        
        Jedis jedis = shardJedis(key);
        Boolean result =  jedis.sismember(key, member);
        jedis.close();
        return result;
    }

    @Override
    public String srandmember(String key) {
        
        Jedis jedis = shardJedis(key);
        String result =  jedis.srandmember(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> srandmember(String key, int count) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.srandmember(key, count);
        jedis.close();
        return result;
    }

    @Override
    public Long strlen(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.strlen(key);
        jedis.close();
        return result;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zadd(key, score, member);
        jedis.close();
        return result;
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zadd(key, score, member, params);
        jedis.close();
        return result;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zadd(key, scoreMembers);
        jedis.close();
        return result;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zadd(key, scoreMembers, params);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrange(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long zrem(String key, String... member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zrem(key, member);
        jedis.close();
        return result;
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        
        Jedis jedis = shardJedis(key);
        Double result =  jedis.zincrby(key, score, member);
        jedis.close();
        return result;
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        
        Jedis jedis = shardJedis(key);
        Double result =  jedis.zincrby(key, score, member, params);
        jedis.close();
        return result;
    }

    @Override
    public Long zrank(String key, String member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zrank(key, member);
        jedis.close();
        return result;
    }

    @Override
    public Long zrevrank(String key, String member) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zrevrank(key, member);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrevrange(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrangeWithScores(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeWithScores(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long zcard(String key) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zcard(key);
        jedis.close();
        return result;
    }

    @Override
    public Double zscore(String key, String member) {
        
        Jedis jedis = shardJedis(key);
        Double result =  jedis.zscore(key, member);
        jedis.close();
        return result;
    }

    @Override
    public List<String> sort(String key) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.sort(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        
        Jedis jedis = shardJedis(key);
        List<String> result =  jedis.sort(key, sortingParameters);
        jedis.close();
        return result;
    }

    @Override
    public Long zcount(String key, double min, double max) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zcount(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Long zcount(String key, String min, String max) {
        
        Jedis jedis = shardJedis(key);
        Long result =  jedis.zcount(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrangeByScore(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrangeByScore(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrangeByScore(key, max, min);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrangeByScore(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrevrangeByScore(key, max, min);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrangeByScore(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrevrangeByScore(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrangeByScoreWithScores(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeByScoreWithScores(key, max, min);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<String> result =  jedis.zrevrangeByScore(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrangeByScoreWithScores(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeByScoreWithScores(key, max, min);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeByScoreWithScores(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        
        Jedis jedis = shardJedis(key);
        Set<Tuple> result =  jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.zremrangeByRank(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.zremrangeByScore(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.zremrangeByScore(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.zlexcount(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        Jedis jedis = shardJedis(key);
        Set<String> result = jedis.zrangeByLex(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        Jedis jedis = shardJedis(key);
        Set<String> result = jedis.zrangeByLex(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        Jedis jedis = shardJedis(key);
        Set<String> result = jedis.zrevrangeByLex(key, max, min);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        Jedis jedis = shardJedis(key);
        Set<String> result = jedis.zrevrangeByLex(key, max, min, offset, count);
        jedis.close();
        return result;
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.zremrangeByLex(key, min, max);
        jedis.close();
        return result;
    }

    @Override
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.linsert(key, where, pivot, value);
        jedis.close();
        return result;
    }

    @Override
    public Long lpushx(String key, String... string) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.lpush(key, string);
        jedis.close();
        return result;
    }

    @Override
    public Long rpushx(String key, String... string) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.rpush(key, string);
        jedis.close();
        return result;
    }

    @Override
    public List<String> blpop(String arg) {
        Jedis jedis = shardJedis(arg);
        List<String> result = jedis.blpop(arg);
        jedis.close();
        return result;
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        Jedis jedis = shardJedis(key);
        List<String> result = jedis.blpop(timeout, key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> brpop(String arg) {
        Jedis jedis = shardJedis(arg);
        List<String> result = jedis.brpop(arg);
        jedis.close();
        return result;
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = shardJedis(key);
        List<String> result = jedis.brpop(timeout, key);
        jedis.close();
        return result;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public String echo(String string) {
        Jedis jedis = shardJedis(string);
        String result = jedis.echo(string);
        jedis.close();
        return result;
    }

    @Override
    public Long move(String key, int dbIndex) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.move(key, dbIndex);
        jedis.close();
        return result;
    }

    @Override
    public Long bitcount(String key) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.bitcount(key);
        jedis.close();
        return result;
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.bitcount(key, start, end);
        jedis.close();
        return result;
    }

    @Override
    public Long bitpos(String key, boolean value) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.bitpos(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.bitpos(key, value, params);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<Map.Entry<String, String>> result = jedis.hscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<String> sscan(String key, int cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<String> result = jedis.sscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<Tuple> result = jedis.zscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<Map.Entry<String, String>> result = jedis.hscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        Jedis jedis = shardJedis(key);
        ScanResult<Map.Entry<String, String>> result = jedis.hscan(key, cursor, params);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<String> result = jedis.sscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        Jedis jedis = shardJedis(key);
        ScanResult<String> result = jedis.sscan(key, cursor, params);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        Jedis jedis = shardJedis(key);
        ScanResult<Tuple> result = jedis.zscan(key, cursor);
        jedis.close();
        return result;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        Jedis jedis = shardJedis(key);
        ScanResult<Tuple> result = jedis.zscan(key, cursor, params);
        jedis.close();
        return result;
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.pfadd(key, elements);
        jedis.close();
        return result;
    }

    @Override
    public long pfcount(String key) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.pfcount(key);
        jedis.close();
        return result;
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.geoadd(key, longitude, latitude, member);
        jedis.close();
        return result;
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        Jedis jedis = shardJedis(key);
        Long result = jedis.geoadd(key, memberCoordinateMap);
        jedis.close();
        return result;
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        Jedis jedis = shardJedis(key);
        Double result = jedis.geodist(key, member1, member2);
        jedis.close();
        return result;
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        Jedis jedis = shardJedis(key);
        Double result = jedis.geodist(key, member1, member2, unit);
        jedis.close();
        return result;
    }

    @Override
    public List<String> geohash(String key, String... members) {
        Jedis jedis = shardJedis(key);
        List<String> result = jedis.geohash(key, members);
        jedis.close();
        return result;
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        Jedis jedis = shardJedis(key);
        List<GeoCoordinate> result = jedis.geopos(key, members);
        jedis.close();
        return result;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        Jedis jedis = shardJedis(key);
        List<GeoRadiusResponse> result = jedis.georadius(key, longitude, latitude, radius, unit);
        jedis.close();
        return result;
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis jedis = shardJedis(key);
        List<GeoRadiusResponse> result = jedis.georadius(key, longitude, latitude, radius, unit, param);
        jedis.close();
        return result;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        Jedis jedis = shardJedis(key);
        List<GeoRadiusResponse> result = jedis.georadiusByMember(key, member, radius, unit);
        jedis.close();
        return result;
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis jedis = shardJedis(key);
        List<GeoRadiusResponse> result = jedis.georadiusByMember(key, member, radius, unit, param);
        jedis.close();
        return result;
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        Jedis jedis = shardJedis(key);
        List<Long> result = jedis.bitfield(key, arguments);
        jedis.close();
        return result;
    }

    @Override
    public void close() throws IOException {

    }
    
    private Jedis shardJedis(String key){
        JedisSentinelPool sentinelPool =  getShard(key);
        Jedis jedis = sentinelPool.getResource();
        return jedis;
    }
}
