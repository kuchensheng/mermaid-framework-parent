# mermaid-redis
## 1.Introduction
**mermaid-redis**是redis访问组件，支持Redis的缓存、分布式锁、分布式计数器队列以及Topic（pub/sub队列）
## 2.QuickStart
```xml
<dependency>
    <groupId>com.mermaid.framework</groupId>
    <artifactId>mermaid-redis</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
## 3.接口说明
```java
/**
* 更多详情查看 @see 各接口说明
*/
/**
     * 设置存储给定键的值
     * @throws Exception
     */
    @Test
    public void set() throws Exception {
        redisService.set("test","kuchensheng");
        Assert.assertTrue(true);
    }

    /**
     * 设置存储给定键的值，并设置失效时间
     * @throws Exception
     */
    @Test
    public void setWithExpire() throws Exception {
        redisService.set("testwithexpire","kucsdf",2);
        Assert.assertTrue(true);
    }

    /**
     * 获取键的集合
     * @throws Exception
     */
    @Test
    public void keys() throws Exception {
        Set<String> keys = redisService.keys("test*");
        logger.info("result={}",keys.toString());
        Assert.assertNotNull(keys);
    }

    /**
     * 获取给定键的值，默认String类型
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        String pipeline = redisService.get("test");
        logger.info("key={},value={}","test",pipeline);
        Assert.assertNotNull(pipeline);
    }

    @Test
    public void getWithClazz() throws Exception {
        String pipeline = redisService.get("test", String.class);
        Assert.assertNotNull(pipeline);
    }

    /**
     * 判断元素是否存在
     * @throws Exception
     */
    @Test
    public void exists() throws Exception {
        boolean test = redisService.exists("test");
        Assert.assertTrue(test);
    }

    /**
     * 删除键[集合]
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        redisService.delete("test","testwithexpire");
        Assert.assertTrue(true);
    }

    /**
     * 给键设置失效时间
     * @throws Exception
     */
    @Test
    public void expire() throws Exception {
        redisService.expire("test",2);
        Assert.assertTrue(true);
    }
```
## 4.mermaid-redis配置清单
```text
#Redis服务器
spring.redis.host=192.168.56.1
#Redis服务器连接端口
spring.redis.port=6379

spring.redis.password=
#redis数据索引
spring.redis.database=0
spring.redis.pool.max-active=8
#redis连接池达到最大连接数后，新的redis连接获取请求等待时间，单位：毫秒，“-1”表示无限等待
spring.redis.pool.max-wait=-1
spring.redis.pool.max-idle=8
spring.redis.timeout=0
```