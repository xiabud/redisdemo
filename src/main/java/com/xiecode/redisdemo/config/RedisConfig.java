package com.xiecode.redisdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    //服务器地址
    @Value("${spring.redis.host}")
    private String host;

    //服务器端口
    @Value("${spring.redis.port}")
    private Integer port;

    //服务器密码
    //@Value("${spring.redis.password}")
    //private String password;

    //连接超时时间
    @Value("${spring.redis.timeout}")
    private String timeout;

    //连接池最大连接数
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxTotal;

    //最大连接阻塞等待时间
    @Value("${spring.redis.jedis.pool.max-wait}")
    private String maxWait;

    //最大空闲连接
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;

    //最小空闲连接
    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer mimIdle;


    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(Long.parseLong(maxWait.substring(0, maxWait.length() - 2)));
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(mimIdle);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,Integer.parseInt(timeout.substring(0,timeout.length()-2)));
        return jedisPool;
    }


}
