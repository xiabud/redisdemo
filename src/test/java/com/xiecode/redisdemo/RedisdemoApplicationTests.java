package com.xiecode.redisdemo;

import com.xiecode.redisdemo.pojo.User;
import com.xiecode.redisdemo.util.SerializeUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RedisdemoApplicationTests {

    /*
     @Description: 连接redis数据库测试
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */

    //@Test
    //public void initConnect() {
    //    //创建jedis连接对象，连接redis服务器
    //    Jedis jedis = new Jedis("127.0.0.1", 6379);
    //    //设置认证密码
    //    //jedis.auth("");
    //    //指定数据库，默认0
    //    jedis.select(1);
    //    //使用ping命令,测试是否连接成功
    //    String result = jedis.ping();
    //    System.out.println(result);
    //    //添加一条数据
    //    jedis.set("name", "张三");
    //    //获取并且打印输出添加的数据
    //    String name = jedis.get("name");
    //    System.out.println("name = " + name);
    //    if (jedis != null) {
    //        jedis.close();
    //    }
    //}

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
        jedis.close();

    }


    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis = null;

    //初始化实例对象
    @BeforeEach
    public void initConnt() {
        jedis = jedisPool.getResource();
    }

    /**
    * @Description: 操作String
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */
    @Test
    public void testString() {
        //选择数据库
        jedis.select(3);
        //添加一条数据
        jedis.set("name", "张三");
        jedis.mset("address", "上海", "sex", "1");
        List<String> list = jedis.mget("name", "address", "sex");
        for (String s : list) {
            System.out.println("s = " + s);
        }
        //删除数据
        jedis.del("sex");
    }
    
    /** 
    * @Description: 操作hash
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */
    @Test
    public void testHash() {
        //添加一条数据
        jedis.hset("game", "name", "消失的光芒");
        //取值
        //String gameName = jedis.hget("game", "name");
        //添加多条数据
        HashMap<String, String> game = new HashMap<>();
        game.put("createTime", "2015-7-14");
        game.put("type", "run and act");
        jedis.hset("game", game);
        //取多条参数
        List<String> list = jedis.hmget("game", "name", "type", "createTime");
        for (String s : list) {
            System.out.println(s);
        }
        //取整个Hash的方法 使用hgetAll()方法获取
        Map<String, String> all = jedis.hgetAll("game");
        for (Map.Entry<String, String> stringStringEntry : all.entrySet()) {
            System.out.println(stringStringEntry.getKey() + "=" + stringStringEntry.getValue());
        }

        //删除hash的参数 hdel()方法返回的是执行的条数
        Long hdel = jedis.hdel("game", "createTime", "type");
        System.out.println("hdel = " + hdel);
    }


    /**
    * @Description: 操作list
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */
    @Test
    public void testList() {
        //左添加
        jedis.lpush("Nintendo", "全明星大乱斗", "马里奥奥德赛", "塞尔达传说旷野之息");
        //右添加
        jedis.rpush("Nintendo", "银河战士 密特罗德", "星之卡比探索发现", "路易吉鬼屋");
        //获取数据
        List<String> nintendoGameList = jedis.lrange("Nintendo", 0, jedis.llen("Nintendo")-1);
        for (String game : nintendoGameList) System.out.println("game = " + game);

        //获取总条数
        System.out.println(jedis.llen("Nintendo"));
        //删除list部分数据
        Long lrem = jedis.lrem("Nintendo", 3, "全明星大乱斗");
        System.out.println("删除了" + lrem + "条数据");
    }

    /** 
    * @Description: 测试List的左弹出与右弹出
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/18
    */
    @Test
    public void testList02() {
        //获取数据
        List<String> nintendoGameList = jedis.lrange("Nintendo", 0, jedis.llen("Nintendo")-1);
        for (String game : nintendoGameList) System.out.println("game = " + game);

        System.out.println("Nintendo一共有" + jedis.llen("Nintendo") + "条数据。");
        //测试左弹出
        System.out.println("左弹出一个：" + jedis.lpop("Nintendo"));
        //测试右弹出
        System.out.println("右弹出一个：" + jedis.rpop("Nintendo"));

        System.out.println("Nintendo一共有" + jedis.llen("Nintendo") + "条数据。");

    }

    /**
    * @Description: 操作set数据类型
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/19
    */
    @Test
    public void testSet() {
        //添加数据 无法添加相同的数据
        jedis.sadd("capcom", "怪物猎人");
        //获取数据
        Set<String> capcom = jedis.smembers("capcom");
        //获取总条数
        System.out.println("capcom一共有" + jedis.scard("capcom") + "款游戏");
        for (Object o : capcom.toArray()) System.out.println(o.toString());
    }

    @Test
    public void testScoreSet() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("生化危机",2d);
        map.put("恐龙快打",1d);
        map.put("怪物猎人",3d);
        map.put("魔界村",5d);
        map.put("鬼泣5",4d);
        jedis.zadd("capcom2", map);
        //获取数据
        Set<String> capcom = jedis.zrange("capcom2", 0, 4);
        for (Object o : capcom.toArray()) System.out.println(o.toString());
        //获取总条数
        System.out.println(jedis.zcard("capcom2"));
        //删除部分数据
        jedis.zrem("capcom2", "鬼泣5");
        //删除全部数据
        //jedis.del("capcom2");
    }

    /**
    * @Description: 层级目录形式存储数据
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/19
    */
    @Test
    public void testDir() {
        jedis.set("game:corporation:capcom", "生化危机");
        String s = jedis.get("game:corporation:capcom");
        System.out.println(s);
    }


    /**
    * @Description: 设置key的失效时间
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/19
    */
    @Test
    public void testExpire() {
        //给已经存在的key设置失效时间
        //jedis.set("code", "tset");
        //设置失效时间，单位为秒
        //jedis.expire("code", 30);
        //设置失效时间，单位为毫秒
        //jedis.pexpire("code", 30000);
        //查看失效时间
        //System.out.println(jedis.ttl("code"));


        //添加key的时候设置失效时间
        //jedis.setex("code", 30, "test");
        //jedis.psetex("code", 30000, "test");
        //查看失效时间 单位秒
        //System.out.println(jedis.ttl("code"));
        //查看失效时间 单位毫秒
        //System.out.println(jedis.pttl("code"));

        //nx,xx的用法
        SetParams setParams = new SetParams();
        //不存在的时候才能成功
        setParams.nx();
        //存在的时候才能成功
        //setParams.xx();
        //设置失效时间，单位为秒
        //setParams.ex(30);
        //设置失效时间，单位为毫秒
        setParams.px(30000);
        String set = jedis.set("code", "test", setParams);
        System.out.println("set = " + set);
    }


    /**
    * @Description: 查询所有key
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/20
    */
    @Test
    public void selectAllKey() {
        //查询当前数据库所有的key的数量
        Long size = jedis.dbSize();
        System.out.println("size = " + size);
        //查询当前数据库所有的key
        Set<String> set = jedis.keys("*");
        for (Object o : set.toArray()) {
            System.out.println("key = " + o);
        }
    }

    /**
    * @Description: 事务
    * @param: []
    * @return: void
    * @Author: Xiewc
    * @Date: 2022/5/20
    */
    @Test
    public void testMulti() {
        //开启事务
        Transaction tx = jedis.multi();
        tx.set("tx11", "1001");

        //提交事务
        System.out.println(tx.exec());

        //回滚事务
        //System.out.println(tx.discard());
    }

    @Test
    public void testByte() {
        User user = new User();
        user.setId(1001);
        user.setName("谢炜程");
        user.setPassword("123456");
        //序列化为byte数组
        byte[] useKey = SerializeUtil.serialize("user:" + user.getId());
        byte[] userValue = SerializeUtil.serialize(user);

        //存入redis
        jedis.set(useKey, userValue);
        //取出数据
        byte[] bytes = jedis.get(useKey);
        //反序列化
        User user1 = (User) SerializeUtil.unserialize(bytes);
        System.out.println(user1);

    }

    
    //释放资源
    @AfterEach
    public void closeConnt() {
        if (null!=jedis) jedis.close();
    }


}
