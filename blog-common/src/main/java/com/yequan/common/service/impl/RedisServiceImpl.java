package com.yequan.common.service.impl;

/**
 * @Auther: yq
 * @Date: 2019/7/4 11:00
 * @Description:
 */

import com.yequan.common.service.RedisService;
import com.yequan.common.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String CACHE_PREFIX;

    private boolean CACHE_CLOSED;

    @SuppressWarnings("rawtypes")
    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            String str = obj.toString();
            if ("".equals(str.trim())) {
                return true;
            }
            return false;
        }
        if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            if (list.isEmpty()) {
                return true;
            }
            return false;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.isEmpty()) {
                return true;
            }
            return false;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            if (set.isEmpty()) {
                return true;
            }
            return false;
        }
        if (obj instanceof Object[]) {
            Object[] objs = (Object[]) obj;
            if (objs.length <= 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 构建缓存key值
     *
     * @param key 缓存key
     * @return
     */
    private String buildKey(String key) {
        if (CACHE_PREFIX == null || "".equals(CACHE_PREFIX)) {
            return key;
        }
        return CACHE_PREFIX + ":" + key;
    }

    /**
     * 返回缓存的前缀
     *
     * @return CACHE_PREFIX_FLAG
     */
    public String getCachePrefix() {
        return CACHE_PREFIX;
    }

    /**
     * 设置缓存的前缀
     *
     * @param cachePrefix
     */
    public void setCachePrefix(String cachePrefix) {
        if (cachePrefix != null && !"".equals(cachePrefix.trim())) {
            CACHE_PREFIX = cachePrefix.trim();
        }
    }

    /**
     * 关闭缓存
     *
     * @return true:成功 false:失败
     */
    public boolean close() {
        Logger.debug(" cache closed ! ");
        CACHE_CLOSED = true;
        return true;
    }

    /**
     * 打开缓存
     *
     * @return true:存在 false:不存在
     */
    public boolean openCache() {
        CACHE_CLOSED = false;
        return true;
    }

    /**
     * 检查缓存是否开启
     *
     * @return true:已关闭 false:已开启
     */
    public boolean isClose() {
        return CACHE_CLOSED;
    }

    /**
     * 判断key值是否存在
     *
     * @param key 缓存的key
     * @return true:存在 false:不存在
     */
    public boolean hasKey(String key) {
        Logger.debug(" hasKey key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return false;
            }
            key = buildKey(key);
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 匹配符合正则的key
     *
     * @param patternKey
     * @return key的集合
     */
    public Set<String> keys(String patternKey) {
        Logger.debug(" keys key :{}", patternKey);
        try {
            if (isClose() || isEmpty(patternKey)) {
                return Collections.emptySet();
            }
            return redisTemplate.keys(patternKey);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return true:成功 false:失败
     */
    public boolean del(String... key) {
        Logger.debug(" delete key :{}", key.toString());
        try {
            if (isClose() || isEmpty(key)) {
                return false;
            }
            Set<String> keySet = new HashSet<String>();
            for (String str : key) {
                keySet.add(buildKey(str));
            }
            redisTemplate.delete(keySet);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return true:成功 false:失败
     */
    public boolean delPattern(String key) {
        Logger.debug(" delete Pattern keys :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.delete(redisTemplate.keys(key));
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除一组key值
     *
     * @param keys
     * @return true:成功 false:失败
     */
    public boolean del(Set<String> keys) {
        Logger.debug(" delete keys :{}", keys.toString());
        try {
            if (isClose() || isEmpty(keys)) {
                return false;
            }
            Set<String> keySet = new HashSet<String>();
            for (String str : keys) {
                keySet.add(buildKey(str));
            }
            redisTemplate.delete(keySet);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param key     缓存key
     * @param seconds 过期秒数
     * @return true:成功 false:失败
     */
    public boolean setExpire(String key, long seconds) {
        Logger.debug(" setExpire key :{}, seconds: {}", key, seconds);
        try {
            if (isClose() || isEmpty(key) || seconds < 0) {
                return false;
            }
            key = buildKey(key);
            return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 查询过期时间
     *
     * @param key 缓存key
     * @return 秒数
     */
    public Long getExpire(String key) {
        Logger.debug(" getExpire key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return 0L;
            }
            key = buildKey(key);
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 缓存存入key-value
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return true:成功 false:失败
     */
    public boolean setString(String key, String value) {
        Logger.debug(" setString key :{}, value: {}", key, value);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 缓存存入key-value
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param seconds 秒数
     * @return true:成功 false:失败
     */
    public boolean set(String key, Object value, long seconds) {
        Logger.debug(" setString key :{}, value: {}, timeout:{}", key, value, seconds);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据key取出String value
     *
     * @param key 缓存key值
     * @return String    缓存的String
     */
    public Object get(String key) {
        Logger.debug(" getString key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 去的缓存中的最大值并+1
     *
     * @param key 缓存key值
     * @return long    缓存中的最大值+1
     */
    public long incr(String key) {
        Logger.debug(" incr key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return 0;
            }
            key = buildKey(key);
            return redisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 缓存中存入序列化的Object对象
     *
     * @param key 缓存key
     * @param obj 存入的序列化对象
     * @return true:成功 false:失败
     */
    public boolean set(String key, Object obj) {
        Logger.debug(" set key :{}, value:{}", key, obj);
        try {
            if (isClose() || isEmpty(key) || isEmpty(obj)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForValue().set(key, obj);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 缓存中存入序列化的Object对象
     *
     * @param key 缓存key
     * @param obj 存入的序列化对象
     * @return true:成功 false:失败
     */
    public boolean setObj(String key, Object obj, long seconds) {
        Logger.debug(" set key :{}, value:{}, seconds:{}", key, obj, seconds);
        try {
            if (isClose() || isEmpty(key) || isEmpty(obj)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForValue().set(key, obj);
            if (seconds > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 取出缓存中存储的序列化对象
     *
     * @param key   缓存key
     * @param clazz 对象类
     * @return <T>	序列化对象
     */
    public <T> T getObj(String key, Class<T> clazz) {
        Logger.debug(" get key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 存入Map数组
     *
     * @param <T>
     * @param key 缓存key
     * @param map 缓存map
     * @return true:成功 false:失败
     */
    public <T> boolean setMap(String key, Map<String, T> map) {
        try {
            if (isClose() || isEmpty(key) || isEmpty(map)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 取出缓存的map
     *
     * @param key 缓存key
     * @return map    缓存的map
     */
    @SuppressWarnings("rawtypes")
    public Map getMap(String key) {
        Logger.debug(" getMap key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询缓存的map的集合大小
     *
     * @param key 缓存key
     * @return int    缓存map的集合大小
     */
    public long getMapSize(String key) {
        Logger.debug(" getMap key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return 0;
            }
            key = buildKey(key);
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return 0;
    }


    /**
     * 根据key以及hashKey取出对应的Object对象
     *
     * @param key     缓存key
     * @param hashKey 对应map的key
     * @return object    map中的对象
     */
    public Object getMapKey(String key, String hashKey) {
        Logger.debug(" getMapkey :{}, hashKey:{}", key, hashKey);
        try {
            if (isClose() || isEmpty(key) || isEmpty(hashKey)) {
                return null;
            }
            key = buildKey(key);
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 取出缓存中map的所有key值
     *
     * @param key 缓存key
     * @return Set<String> map的key值合集
     */
    public Set<Object> getMapKeys(String key) {
        Logger.debug(" getMapKeys key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 删除map中指定的key值
     *
     * @param key     缓存key
     * @param hashKey map中指定的hashKey
     * @return true:成功 false:失败
     */
    public boolean delMapKey(String key, String hashKey) {
        Logger.debug(" delMapKey key :{}, hashKey:{}", key, hashKey);
        try {
            if (isClose() || isEmpty(key) || isEmpty(hashKey)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForHash().delete(key, hashKey);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 存入Map数组
     *
     * @param <T>
     * @param key     缓存key
     * @param map     缓存map
     * @param seconds 秒数
     * @return true:成功 false:失败
     */
    public <T> boolean setMap(String key, Map<String, T> map, long seconds) {
        Logger.debug(" setMap key :{}, value: {}, seconds:{}", key, map, seconds);
        try {
            if (isClose() || isEmpty(key) || isEmpty(map) || seconds < 0) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForHash().putAll(key, map);
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * map中加入新的key
     *
     * @param <T>
     * @param key     缓存key
     * @param hashKey map的Key值
     * @param value   map的value值
     * @return true:成功 false:失败
     */
    public <T> boolean addMap(String key, String hashKey, T value) {
        Logger.debug(" addMap key :{}, hashKey: {}, value:{}", key, hashKey, value);
        try {
            if (isClose() || isEmpty(key) || isEmpty(hashKey) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 缓存存入List
     *
     * @param <T>
     * @param key  缓存key
     * @param list 缓存List
     * @return true:成功 false:失败
     */
    public <T> boolean setList(String key, List<T> list) {
        Logger.debug(" setList key :{}, list: {}", key, list);
        try {
            if (isClose() || isEmpty(key) || isEmpty(list)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForList().leftPushAll(key, list.toArray());
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据key值取出对应的list合集
     *
     * @param key 缓存key
     * @return List<Object> 缓存中对应的list合集
     */
    public <V> List<V> getList(String key) {
        Logger.debug(" getList key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return (List<V>) redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据key值截取对应的list合集
     *
     * @param key   缓存key
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    public void trimList(String key, int start, int end) {
        Logger.debug(" trimList key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return;
            }
            key = buildKey(key);
            redisTemplate.opsForList().trim(key, start, end);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
    }

    /**
     * 取出list合集中指定位置的对象
     *
     * @param key   缓存key
     * @param index 索引位置
     * @return Object    list指定索引位置的对象
     */
    public Object getIndexList(String key, int index) {
        Logger.debug(" getIndexList key :{}, index:{}", key, index);
        try {
            if (isClose() || isEmpty(key) || index < 0) {
                return null;
            }
            key = buildKey(key);
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Object存入List
     *
     * @param key   缓存key
     * @param value List中的值
     * @return true:成功 false:失败
     */
    public boolean addList(String key, Object value) {
        Logger.debug(" addList key :{}, value:{}", key, value);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 缓存存入List
     *
     * @param <T>
     * @param key     缓存key
     * @param list    缓存List
     * @param seconds 秒数
     * @return true:成功 false:失败
     */
    public <T> boolean setList(String key, List<T> list, long seconds) {
        Logger.debug(" setList key :{}, value:{}, seconds:{}", key, list, seconds);
        try {
            if (isClose() || isEmpty(key) || isEmpty(list)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForList().leftPushAll(key, list.toArray());
            if (seconds > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * set集合存入缓存
     *
     * @param <T>
     * @param key 缓存key
     * @param set 缓存set集合
     * @return true:成功 false:失败
     */
    public <T> boolean setSet(String key, Set<T> set) {
        Logger.debug(" setSet key :{}, value:{}", key, set);
        try {
            if (isClose() || isEmpty(key) || isEmpty(set)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForSet().add(key, set.toArray());
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * set集合中增加value
     *
     * @param key   缓存key
     * @param value 增加的value
     * @return true:成功 false:失败
     */
    public boolean addSet(String key, Object value) {
        Logger.debug(" addSet key :{}, value:{}", key, value);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForSet().add(key, value);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * set集合存入缓存
     *
     * @param <T>
     * @param key     缓存key
     * @param set     缓存set集合
     * @param seconds 秒数
     * @return true:成功 false:失败
     */
    public <T> boolean setSet(String key, Set<T> set, long seconds) {
        Logger.debug(" setSet key :{}, value:{}, seconds:{}", key, set, seconds);
        try {
            if (isClose() || isEmpty(key) || isEmpty(set)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForSet().add(key, set.toArray());
            if (seconds > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 取出缓存中对应的set合集
     *
     * @param <T>
     * @param key 缓存key
     * @return Set<Object> 缓存中的set合集
     */
    public <T> Set<T> getSet(String key) {
        Logger.debug(" getSet key :{}", key);
        try {
            if (isClose() || isEmpty(key)) {
                return null;
            }
            key = buildKey(key);
            return (Set<T>) redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 有序集合存入数值
     *
     * @param key   缓存key
     * @param value 缓存value
     * @param score 评分
     * @return
     */
    public boolean addZSet(String key, Object value, double score) {
        Logger.debug(" addZSet key :{},value:{}, score:{}", key, value, score);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从有序集合中删除指定值
     *
     * @param key   缓存key
     * @param value 缓存value
     * @return
     */
    public boolean removeZSet(String key, Object value) {
        Logger.debug(" removeZSet key :{},value:{}", key, value);
        try {
            if (isClose() || isEmpty(key) || isEmpty(value)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForZSet().remove(key, value);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从有序集合中删除指定位置的值
     *
     * @param key   缓存key
     * @param start 起始位置
     * @param end   结束为止
     * @return
     */
    public boolean removeZSet(String key, long start, long end) {
        Logger.debug(" removeZSet key :{},start:{}, end:{}", key, start, end);
        try {
            if (isClose() || isEmpty(key)) {
                return false;
            }
            key = buildKey(key);
            redisTemplate.opsForZSet().removeRange(key, start, end);
            return true;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 从有序集合中获取指定位置的值
     *
     * @param key   缓存key
     * @param start 起始位置
     * @param end   结束为止
     * @return
     */
    public <T> Set<T> getZSet(String key, long start, long end) {
        Logger.debug(" getZSet key :{},start:{}, end:{}", key, start, end);
        try {
            if (isClose() || isEmpty(key)) {
                return Collections.emptySet();
            }
            key = buildKey(key);
            return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    /**
     * 发布消息
     *
     * @param channel 频道
     * @param message 要发布的消息
     */
    public synchronized void publish(String channel, String message) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ((Jedis) connection.getNativeConnection()).publish(channel, message);
                return null;
            }
        });
    }

    /**
     * 订阅消息
     *
     * @param jedisPubSub
     * @param channels
     */
    public synchronized void subscribe(JedisPubSub jedisPubSub, String... channels) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ((Jedis) connection.getNativeConnection()).subscribe(jedisPubSub, channels);
                return null;
            }
        });
    }

}
