package com.rainyalley.architecture.filter.limit;

import com.rainyalley.architecture.filter.limit.redis.ConsoleRedisImpl;
import redis.clients.jedis.JedisCluster;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleLimitFilter extends AbstractLimitFilter {

    private JedisCluster jedisCluster;

    @Override
    protected void initFilterBean() throws ServletException {
        new ConsoleRedisImpl(jedisCluster);
    }

    @Override
    protected String determineCaller(HttpServletRequest request) {
        return request.getParameter("MerCustId");
    }

    @Override
    protected String determineTarget(HttpServletRequest request) {
        return request.getParameter("CmdId");
    }

    @Override
    protected void reject(RejectReason reason, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write(reason.name());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
}
