package org.nocoder.commons.redis;

import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2017/7/31.
 */
public interface IRedisService {

    /**
     * set if not exist
     *
     * @param key
     * @param value
     * @return
     */
    Long setNx(String key, String value);

    /**
     * 设置指定字符串值，并返回旧值（如果key不存在，则返回null）
     *
     * @param key
     * @param value
     * @return
     */
    String getSet(String key, String value);

    /**
     * 存入String
     *
     * @param key
     * @param str
     */
    boolean setStr(String key, String str);

    /**
     * 存入对象
     *
     * @param key
     * @param obj
     * @return
     */
    boolean setObject(String key, Object obj);

    /**
     * 存入过期对象
     *
     * @param key
     * @param obj
     * @param timeout
     * @return
     */
    boolean setWithExpire(String key, Object obj, int timeout);


    /**
     * 判断是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 通过前缀匹配是否存在
     *
     * @param keyPre
     * @return
     */
    boolean existsByMatchPre(String keyPre);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    boolean removeObject(String key);

    /**
     * 通配符删除已key值开头的记录
     *
     * @param pattern
     * @return
     */
    void removeObjects(String pattern);


    /**
     * 获取String
     *
     * @param key
     * @return
     */
    String getStr(String key);

    /**
     * 获取给定key的剩余生存时间  返回值为-2的时候key不存在
     */
    public Long getTTL(String key);

    /**
     * 获取对象
     *
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    <T> T getObject(String key, Class<T> c);

    /**
     * 通配符获取已key值开头的记录
     *
     * @param pattern
     * @param c
     * @param <T>
     * @return
     */
    <T> List<T> getObjects(String pattern, Class<T> c);

    /**
     * 获取列表对象
     *
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    <T> List<T> getArrayObject(String key, Class<T> c);

    /**
     * 通配符获取已key值开头的列表对象
     *
     * @param pattern
     * @param c
     * @param <T>
     * @return
     */
    <T> List<T> getArrayObjects(String pattern, Class<T> c);

    /**
     * 获取符合条件的key的数量
     *
     * @param pattern
     * @return
     */
    int getKeysNumber(String pattern);

    /**
     * 更新过期时间
     *
     * @param key
     * @param timeout
     */
    void expire(String key, int timeout);

    /**
     * 更新过期时间
     *
     * @param pattern
     * @param timeout
     */
    void expires(String pattern, int timeout);

    /**
     * 从map对象中获取数据
     *
     * @param key
     * @param filed
     * @return
     */
    String getStringFromMap(String key, String filed);

    /**
     * 向map对象中存入键值对
     *
     * @param key
     * @param filed
     * @return
     */
    boolean setStringToMap(String key, String filed, String value);

    /**
     * 存入map对象
     *
     * @param key
     * @param map
     * @return
     */
    boolean setMapObject(String key, Map map, int timeout);

    /**
     * redis 消息订阅
     *
     * @param jedisPubSub
     * @param channel
     */
    void subscribe(JedisPubSub jedisPubSub, String channel);

    /**
     * redis 消息发布
     *
     * @param channel
     * @param content
     */
    void publish(String channel, String content);
}
