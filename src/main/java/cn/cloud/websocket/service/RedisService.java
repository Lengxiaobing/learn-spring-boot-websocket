package cn.cloud.websocket.service;

import cn.cloud.websocket.enums.ExpireEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RedisService
 *
 * @author zx
 * @date 2018/7/30
 * @since 1.0.0
 */
public interface RedisService {

    /**
     * 向Redis中存储键值对
     *
     * @param key   KEY
     * @param value VALUE
     * @author zx
     * @date 2018/7/30 17:02
     * @since 1.0.0
     */
    void set(String key, String value);

    /**
     * 向Redis中存储键值对，并设置过期时间
     *
     * @param key      KEY
     * @param value    VALUE
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @author zx
     * @date 2018/7/30 17:02
     * @since 1.0.0
     */
    void setWithExpire(String key, String value, long time, TimeUnit timeUnit);

    /**
     * 从Redis中获取键值对
     *
     * @param key KEY
     * @return K
     * @author zx
     * @date 2018/7/30 17:04
     * @since 1.0.0
     */
    <K> K get(String key);

    /**
     * 删除Redis中的某个KEY
     *
     * @param key KEY
     * @return boolean
     * @author zx
     * @date 2018/7/30 17:10
     * @since 1.0.0
     */
    boolean delete(String key);

    /**
     * 将数据添加到Redis的list中（从左边添加）
     *
     * @param listKey    LIST的key
     * @param expireEnum 有效期的枚举类
     * @param values     待添加的数据
     * @author zx
     * @date 2018/10/12 10:13
     * @since 1.0.0
     */
    void addToListLeft(String listKey, ExpireEnum expireEnum, String... values);

    /**
     * 将数据添加到Redis的list中（从右边添加）
     *
     * @param listKey    LIST的key
     * @param expireEnum 有效期的枚举类
     * @param values     待添加的数据
     * @author zx
     * @date 2018/10/12 10:13
     * @since 1.0.0
     */
    void addToListRight(String listKey, ExpireEnum expireEnum, String... values);

    /**
     * 根据起始结束序号遍历Redis中的list
     *
     * @param listKey LIST的key
     * @param start   起始序号
     * @param end     结束序号
     * @author zx
     * @date 2018/10/12 10:15
     * @since 1.0.0
     */
    List<String> rangeList(String listKey, long start, long end);

    /**
     * 将数据添加Redis的hash中
     *
     * @param hashKey hash的key
     * @param hashKey 待添加的数据key
     * @param value   待添加的数据
     * @author zx
     * @date 2018/10/16 19:18
     * @since 1.0.0
     */
    void addToHash(String key, String hashKey, String value);

    /**
     * 判断指定数据是否在Redis的hash中
     *
     * @param key      hash的key
     * @param hashKeys 待判断的数据
     * @return java.lang.Boolean
     * @author zx
     * @date 2018/10/16 19:18
     * @since 1.0.0
     */
    Boolean isHasKey(String key, Object hashKeys);

    /**
     * 查询Hash中的所有值
     *
     * @param key
     * @return
     */
    Map<Object, Object> hashEntries(String key);

    /**
     * 从Redis的Hash中移除数据
     *
     * @param hashKey  hash的key
     * @param hashKeys 待移除的数据
     * @author zx
     * @date 2018/10/16 19:18
     * @since 1.0.0
     */
    void removeFromHash(String hashKey, Object... hashKeys);


    /**
     * 使用Redis的消息队列
     *
     * @param channel topic name
     * @param message 消息内容
     * @author zx
     * @date 2018/10/16 19:18
     * @since 1.0.0
     */
    void convertAndSend(String channel, Object message);
}
