package com.mermaid.framework.redis.impl;

import com.mermaid.framework.redis.RedisDistributeLockObject;
import com.mermaid.framework.redis.RedisDistributeLockResult;
import com.mermaid.framework.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Chensheng.Ku
 */
public class DefaultRedisService implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRedisService.class);
    private static Map<String,Object> operationsMap = new HashMap<>();

    private static final String STRING_TYPE="STRING_TYPE";
    private static final String SET_TYPE="SET_TYPE";
    private static final String HASH_TYPE="HASH_TYPE";
    private static final String ZSET_TYPE="ZSET_TYPE";
    private static final String LIST_TYPE="LIST_TYPE";
    private static final String HYPERLOG_TYPE="HYPERLOG_TYPE";
    /**
     * 服务名
     */
    private String name;

    @Autowired
    private RedisTemplate redisTemplate;

    public DefaultRedisService() {

    }

    public DefaultRedisService(String name) {
        this(name,null);
    }

    public DefaultRedisService(String name, RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.setName(name);
        initOperationsMap();
    }

    private void initOperationsMap() {
        ValueOperations valueOperations = this.redisTemplate.opsForValue();
        SetOperations setOperations = this.redisTemplate.opsForSet();
        HashOperations hashOperations = this.redisTemplate.opsForHash();
        HyperLogLogOperations hyperLogLogOperations = this.redisTemplate.opsForHyperLogLog();
        ZSetOperations zSetOperations = this.redisTemplate.opsForZSet();
        ListOperations listOperations = this.redisTemplate.opsForList();

        operationsMap.put(STRING_TYPE,valueOperations);
        operationsMap.put(SET_TYPE,setOperations);
        operationsMap.put(HASH_TYPE,hashOperations);
        operationsMap.put(ZSET_TYPE,zSetOperations);
        operationsMap.put(HYPERLOG_TYPE,hyperLogLogOperations);
        operationsMap.put(LIST_TYPE,listOperations);
    }

    @Override
    public void set(String key, Object value) {
        set(key,value, 0L);
    }

    @Override
    public void set(String key, Object value, long expire) {
        try {
            if(null == value) {
                delete(key);
            }else {
                ValueOperations operations = getValueOperation();
                operations.set(key,value,expire);
            }
        } catch (Exception e) {
            logger.error("redis set error",e);
        }
    }

    private ValueOperations getValueOperation() {
        return (ValueOperations) operationsMap.get(STRING_TYPE);
    }

    private ListOperations getListOperation() {
        return (ListOperations) operationsMap.get(LIST_TYPE);
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return keys;
    }

    @Override
    public String get(String key) {
        return get(key,String.class);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T get(String key, Class<T> tpl) {
        if(!StringUtils.hasText(key)) {
                return null;
        }
        ValueOperations operations = (ValueOperations) operationsMap.get(STRING_TYPE);
        Object ret = operations.get(key);
        return (T) ret;
    }

    @Override
    public void delete(String... keys) {
        List<String> listKey = new ArrayList<>();
        for (String key : keys) {
            listKey.add(key);
        }
        redisTemplate.delete(listKey);
    }

    @Override
    public void expire(String key, long expire) {
        redisTemplate.expire(key,expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean lock(String lockName) {
        return lock(lockName, 0L);
    }

    private byte[] str2Bytes(String s) {
        try {
            return s.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean lock(String lockName, long expire) {
        if(exists(lockName)) {
            logger.error("已存在该锁="+lockName);
            return false;
        }
        RedisDistributeLockObject dlock = new RedisDistributeLockObject(lockName,expire);
        return lock(dlock);
    }

    @Override
    public RedisDistributeLockResult acquireLock(RedisDistributeLockObject dlock) {
        return new RedisDistributeLockResult(dlock.getLockName(),lock(dlock),dlock.getIdentifier(),dlock);
    }

    private boolean lock(final RedisDistributeLockObject dlock) {
        boolean flag = false;
        try {
            final byte[] key = str2Bytes(dlock.getLockName());
            flag = (Boolean)redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    Boolean flag = connection.setNX(key, str2Bytes(dlock.getIdentifier()));
                    if(flag && dlock.getSelfReleaseExpired() > 0) {
                        connection.expire(key, dlock.getSelfReleaseExpired());
                    }
                    return flag;
                }
            });
        } catch (Exception e) {
            logger.error("redis lock error",e);
        }
        return flag;
    }

    @Override
    public void unlock(final RedisDistributeLockResult result) {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] lockName = str2Bytes(result.getLockName());
                if(str2Bytes(result.getIdenfier()).equals(connection.get(lockName))) {
                    connection.del(lockName);
                }
                return 0L;
            }
        });
    }

    @Override
    public void unlock(String lockName) {
        delete(lockName);
    }

    @Override
    public Long push(String key, Object value) {
        ListOperations listOperation = getListOperation();

        Long aLong = listOperation.leftPush(key, value);
        return aLong;
    }

    @Override
    public Long pushAll(String key, Object... values) {
        ListOperations listOperation = getListOperation();
        Long aLong = listOperation.leftPushAll(key, values);
        return aLong;
    }

    @Override
    public <T> T pop(String key) {
        ListOperations listOperation = getListOperation();
        Object ret = null;
        ret = listOperation.leftPop(key);
        if(null == ret) {
            ret = listOperation.rightPop(key);
        }
        return (T) ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!StringUtils.hasText(name)) {
            name = DefaultRedisService.class.getSimpleName();
        }
        this.name = name;
    }

    @Override
    public long getCurrentCounter(String key) {
        try {
            Object value = getValueOperation().get(key);
            if(null != value) {
                Long longValue = Long.parseLong(String.valueOf(value));
                return longValue;
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(),e);
        }
        return 0L;
    }

    @Override
    public long increment(String key) {
        return increment(key,1L);
    }

    @Override
    public long increment(String key, long val) {
        try {
            return getValueOperation().increment(key, val);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return 0;
    }

    @Override
    public long decrement(String key) {
        return decrement(key,1L);
    }

    @Override
    public long decrement(final String key,final long val) {
        try {
            return  (long) redisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.decrBy(str2Bytes(key),val);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return 0L;
    }
}
