package com.bqjr.redis;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 
 * 类名称: CacheMangerJedisSentinerAdapter <br/>
 * 功能描述:redis操作接口封装-适用于redis主从哨兵集群模式 <br/>
 * 日期: 
 * 
 * @author hadoop
 * @version
 * @since JDK 1.8
 */
public class CacheMangerJedisSentinerAdapter implements ICacheMangerTarget {

    protected JedisSentinelPool jedisPool;

//     protostuff序列化
//    protected RuntimeSchema<Object> schema = RuntimeSchema.createFrom(Object.class);

//    public CacheMangerJedisSentinerAdapter(JedisSentinelPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }
//
//    private static Logger logger = LoggerFactory.getLogger(CacheMangerJedisSentinerAdapter.class);
//
//    private Jedis getResource() throws JedisException {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            // logger.debug("getResource.", jedis);
//        } catch (JedisException e) {
//            logger.warn("getResource.", e);
//            returnBrokenResource(jedis);
//            throw e;
//        }
//        return jedis;
//    }
//
//    private void returnBrokenResource(Jedis jedis) {
//        if (jedis != null) {
//            jedisPool.returnBrokenResource(jedis);
//        }
//    }
//
//    private void returnResource(Jedis jedis) {
//        if (jedis != null) {
//            jedisPool.returnResource(jedis);
//        }
//    }
//
//    @Override
//    public String getWithkey(String key) {
//        String value = null;
//        Jedis jedis = null;
//        try {
//            jedis = getResource();
//            if (jedis.exists(key)) {
//                value = jedis.get(key);
//                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
//            }
//            logger.debug("get {} = {}", key, value);
//        } catch (Exception e) {
//            logger.warn("get {} = {}", key, value, e);
//        } finally {
//            returnResource(jedis);
//        }
//        return value;
//    }
//
//    @Override
//    public String setKeyValue(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = getResource();
//            value = jedis.set(key, value);
//            logger.debug("keys {} = {}", key, value);
//            return value;
//        } catch (Exception e) {
//            logger.warn("keys {} = {}", key, value, e);
//        } finally {
//            returnResource(jedis);
//        }
//        return value;
//    }
//
//    @Override
//    public long delWithkey(String key) {
//        Jedis jedis = null;
//        long value = 0;
//        try {
//            jedis = getResource();
//            value = jedis.del(key);
//            logger.debug("keys {}", "", key);
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return value;
//    }
//
//    @Override
//    public String getHashWithKey(String hkey, String key) {
//        String value = null;
//        Jedis jedis = null;
//        try {
//            jedis = getResource();
//            value = jedis.hget(key, key);
//            logger.debug("keys {} = {}", key, value);
//            return value;
//        } catch (Exception e) {
//            logger.warn("keys {} = {}", key, value, e);
//        } finally {
//            returnResource(jedis);
//        }
//        return value;
//    }
//
//    @Override
//    public Map<String, String> getAllWithHashKey(String hkey) {
//        Jedis jedis = null;
//        try {
//            jedis = getResource();
//            Map<String, String> map = jedis.hgetAll(hkey);
//            // logger.debug("keys {}", "", key);
//            return map;
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return null;
//    }
//
//    @Override
//    public long setHashKeyValue(String hkey, String key, String value) {
//        Jedis jedis = null;
//        long back = 0;
//        try {
//            jedis = getResource();
//            back = jedis.hset(hkey, key, value);
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    /**
//     * * @param nxxx NX|XX, NX -- Only set the key if it does not already exist.
//     *            XX -- Only set the key if it already exist.
//     * 
//     * @param expx
//     *            EX|PX, expire time units: EX = seconds; PX = milliseconds
//     */
//    @Override
//    public String setExpxKeyCache( String key, String value, long timeout) {
//
//        Jedis jedis = null;
//        String back ="";
//        try {
//            jedis = getResource();
//            jedis.del(key);
//            back = jedis.set(key, value,"NX","EX",timeout);
//            // jedis.expireAt(key, unixTime)
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    @Override
//    public String setHashKeyValues(String hkey, Map<String, String> map) {
//        Jedis jedis = null;
//        String back = "";
//        try {
//            jedis = getResource();
//            back = jedis.hmset(hkey, map);
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    @Override
//    public long delHashWithKey(String hkey, String key) {
//        Jedis jedis = null;
//        long back = 0;
//        try {
//            jedis = getResource();
//            back = jedis.hdel(hkey, key);
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    @Override
//    public String setValueByprotostuff(String key, byte[] bytes) {
//        Jedis jedis = null;
//        String back = "";
//        try {
//            jedis = getResource();
//            back = jedis.set(key.getBytes(), bytes);
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    @Override
//    public byte[] getValueByprotostuff(String key) {
//        Jedis jedis = null;
//        byte[] back = null;
//        try {
//            jedis = getResource();
//            back = jedis.get(key.getBytes());
//        } catch (Exception e) {
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }
//
//    @Override
//    public long delByprotostuff(String key) {
//        Jedis jedis = null;
//        long back = 0;
//        try {
//            jedis = getResource();
//            back = jedis.del(key.getBytes());
//        } catch (Exception e) {
//            //抛异常
//        } finally {
//            returnResource(jedis);
//        }
//        return back;
//    }

}