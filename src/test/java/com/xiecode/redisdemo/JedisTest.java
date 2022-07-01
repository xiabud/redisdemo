package com.xiecode.redisdemo;

import lombok.experimental.NonFinal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class JedisTest {

    //服务器地址
    @Value("${spring.redis.host}")
    private String host;

    //服务器端口
    @Value("${spring.redis.port}")
    private Integer port;

    //服务器密码
    @Value("${spring.redis.password}")
    private String password;

    private Jedis jedis = null;

    //创建连接
    @BeforeEach
    public void before() {
        jedis = new Jedis(host, port);
        jedis.auth(password);
    }


    @Test
    public void test() {
        String result = jedis.set("a", "666");
        System.out.println(result);
    }



    /**
    * <p>
    * jedis的事务操作测试
    * </p>
    * @param
    * @return void
    * @author Xiewc
    * @since 2022/7/1
    */
    @Test
    public void transaction() {
        //开启事务
        Transaction multi=jedis.multi();
        try {
            multi.set("ka", "va");
            multi.set("kb", "vb");
            int num = 1 / 0;
            multi.set("kc", "vc");
            //事务提交
            multi.exec();
        } catch (Exception e) {
            //redis事务回滚
            multi.discard();
            e.printStackTrace();
        }
    }

    //关闭连接
    @AfterEach
    public void close() {
        if (null != jedis) jedis.close();
    }


}
