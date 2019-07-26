package com.yequan.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: yq
 * @Date: 2019/7/4 13:55
 * @Description:
 */
public interface RedisService {
    String getCachePrefix();

    void setCachePrefix(String cachePrefix);

    boolean close();

    boolean openCache();

    boolean isClose();

    boolean hasKey(String key);

    Set<String> keys(String patternKey);

    boolean del(String... key);

    boolean delPattern(String key);

    boolean del(Set<String> keys);

    boolean setExpire(String key, long seconds);

    Long getExpire(String key);

    boolean setString(String key, String value);

    boolean set(String key, Object value, long seconds);

    Object get(String key);

    long incr(String key);

    boolean set(String key, Object obj);

    boolean setObj(String key, Object obj, long seconds);

    <T> T getObj(String key, Class<T> clazz);

    <T> boolean setMap(String key, Map<String, T> map);

    @SuppressWarnings("rawtypes")
    Map getMap(String key);

    long getMapSize(String key);

    Object getMapKey(String key, String hashKey);

    Set<Object> getMapKeys(String key);

    boolean delMapKey(String key, String hashKey);

    <T> boolean setMap(String key, Map<String, T> map, long seconds);

    <T> boolean addMap(String key, String hashKey, T value);

    <T> boolean setList(String key, List<T> list);

    <V> List<V> getList(String key);

    void trimList(String key, int start, int end);

    Object getIndexList(String key, int index);

    boolean addList(String key, Object value);

    <T> boolean setList(String key, List<T> list, long seconds);

    <T> boolean setSet(String key, Set<T> set);

    boolean addSet(String key, Object value);

    <T> boolean setSet(String key, Set<T> set, long seconds);

    <T> Set<T> getSet(String key);

    boolean addZSet(String key, Object value, double score);

    boolean removeZSet(String key, Object value);

    boolean removeZSet(String key, long start, long end);

    <T> Set<T> getZSet(String key, long start, long end);
}
