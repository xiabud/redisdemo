package com.xiecode.redisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootTest
class RedisdemoApplicationTests {

    /**
    * @Description: 连接redis数据库测试
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */

    @Test
    public void initConnect() {
        //创建jedis连接对象，连接redis服务器
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //设置认证密码
        //jedis.auth("");
        //指定数据库，默认0
        jedis.select(1);
        //使用ping命令,测试是否连接成功
        String result = jedis.ping();
        System.out.println(result);
        //添加一条数据
        jedis.set("name", "张三");
        //获取并且打印输出添加的数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
        if (jedis != null) {
            jedis.close();
        }
    }

    /** 
    * @Description: 使用jedis连接池操作redis
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */
    @Test
    public void initConnect02() {
        //初始化连接池对象
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 10000);
        //从连接池获取jedis对象
        Jedis jedis = jedisPool.getResource();
        //指定数据库，默认为0
        jedis.select(2);
        String result = jedis.ping();
        System.out.println("result = " + result);
        jedis.set("name", "王五");
        //读取获取的数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
        if (jedis != null) {
            jedis.close();
        }

    }

}
