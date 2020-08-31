package org.nocoder.commons.redis.impl;

import com.alibaba.fastjson.JSONObject;
import org.nocoder.commons.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Admin on 2017/7/31.
 */
public class RedisServiceImpl implements IRedisService {

    private JedisPool jedisPool;

    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceImpl.class);

    //开启获取缓存
    private static final boolean useRedis = true;
    //jedis返回成功状态值
    private static final String JEDIS_RESULT_STATUS = "ok";
    //jedis存储成功返回值
    private static final int JEDIS_PUT_STATUS = 1;

    /**
     * <p>设置key value,如果key已经存在则返回0,nx==> not exist</p>
     *
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public Long setNx(String key, String value) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setnx(key, value);
            return result;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            return 0L;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     *
     * @param key
     * @param value
     * @return 旧值 如果key不存在 则返回null
     */
    public String getSet(String key, String value) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = jedisPool.getResource();
            res = jedis.getSet(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return res;
    }

    public boolean setStr(String key, String str) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(key, str);
            if (JEDIS_RESULT_STATUS.equalsIgnoreCase(result)) {
                flag = true;
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    public boolean setObject(String key, Object obj) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(key, JSONObject.toJSONString(obj));
            if (JEDIS_RESULT_STATUS.equalsIgnoreCase(result)) {
                flag = true;
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    public boolean setWithExpire(String key, Object obj, int timeout) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = "";
            if (obj instanceof String) {
                result = jedis.set(key, (String) obj);
            } else {
                result = jedis.set(key, JSONObject.toJSONString(obj));
            }
            if (JEDIS_RESULT_STATUS.equalsIgnoreCase(result) && jedis.expire(key, timeout) == JEDIS_PUT_STATUS) {
                flag = true;
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    public boolean exists(String key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = jedisPool.getResource();
            result = jedis.exists(key);
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    @Override
    public boolean existsByMatchPre(String keyPre) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = jedisPool.getResource();
            Set<String> stringSet = jedis.keys(keyPre + "*");
            if (stringSet != null && stringSet.size() > 0) {
                result = true;
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            result = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    public boolean removeObject(String key) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long result = jedis.del(key);
            flag = true;
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    public void removeObjects(String pattern) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> stringSet = jedis.keys(pattern + "*");
            for (String key : stringSet) {
                jedis.del(key);
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String getStr(String key) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 获取给定key的剩余生存时间 返回值为-2的时候key不存在
     */
    public Long getTTL(String key) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = jedisPool.getResource();
            result = jedis.ttl(key);
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public <T> T getObject(String key, Class<T> c) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        T obj = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.get(key);
            if (result != null) {
                obj = JSONObject.parseObject(result, c);
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return obj;
    }

    @Override
    public <T> List<T> getObjects(String pattern, Class<T> c) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        List<T> list = new ArrayList<>();
        try {
            jedis = jedisPool.getResource();
            Set<String> stringSet = jedis.keys(pattern + "*");
            for (String key : stringSet) {
                String result = jedis.get(key);
                if (result != null) {
                    list.add(JSONObject.parseObject(result, c));
                }
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return list;
    }

    public <T> List<T> getArrayObject(String key, Class<T> c) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.get(key);
            if (result != null) {
                return JSONObject.parseArray(result, c);
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    @Override
    public <T> List<T> getArrayObjects(String pattern, Class<T> c) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        List<T> list = new ArrayList<>();
        try {
            jedis = jedisPool.getResource();
            Set<String> stringSet = jedis.keys(pattern + "*");
            for (String key : stringSet) {
                String result = jedis.get(key);
                if (result != null) {
                    list.addAll(JSONObject.parseArray(result, c));
                }
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return list;
    }

    @Override
    public int getKeysNumber(String pattern) {
        if (!useRedis) {
            return 0;
        }
        Jedis jedis = null;
        try {
            pattern = pattern + "*";
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(pattern);
            if (keys != null) {
                return keys.size();
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0;
    }

    @Override
    public void expire(String key, int timeout) {
        if (!useRedis) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void expires(String pattern, int timeout) {
        if (!useRedis) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(pattern + "*");
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    jedis.expire(key, timeout);
                }
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 从map对象中获取数据
     *
     * @param key
     * @param filed
     * @return
     */
    public String getStringFromMap(String key, String filed) {
        if (!useRedis) {
            return null;
        }
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, filed);
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 存入map对象
     *
     * @param key
     * @param map
     * @return
     */
    public boolean setMapObject(String key, Map map, int timeout) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.hmset(key, map);
            if (JEDIS_RESULT_STATUS.equalsIgnoreCase(result) && jedis.expire(key, timeout) == JEDIS_PUT_STATUS) {
                flag = true;
            }
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    @Override
    /**
     * 消息订阅
     */
    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        new RedisSubThread(this.jedisPool, jedisPubSub, channel).start();
    }

    @Override
    /**
     * 消息发布
     */
    public void publish(String channel, String content) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(channel, content);
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


    /**
     * 向map对象中存入键值对
     *
     * @param key
     * @param filed
     * @return
     */
    public boolean setStringToMap(String key, String filed, String value) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long result = jedis.hset(key, filed, value);
            flag = true;
        } catch (Exception se) {
            LOG.error("{}", se);
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            flag = false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return flag;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


}
