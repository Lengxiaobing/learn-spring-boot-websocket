package cn.cloud.websocket.service.impl;

import cn.cloud.websocket.enums.ExpireEnum;
import cn.cloud.websocket.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zx
 * @date 2018/7/30
 * @since 1.0.0
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setWithExpire(String key, String value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    @Override
    public <K> K get(String key) {
        return (K) redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public void addToListLeft(String listKey, ExpireEnum expireEnum, String... values) {
        //插入数据
        redisTemplate.opsForList().leftPushAll(listKey, values);
        //设置过期时间
        redisTemplate.expire(listKey, expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    @Override
    public void addToListRight(String listKey, ExpireEnum expireEnum, String... values) {
        //插入数据
        redisTemplate.opsForList().rightPushAll(listKey, values);
        //设置过期时间
        redisTemplate.expire(listKey, expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    @Override
    public List<String> rangeList(String listKey, long start, long end) {
        //查询数据
        return redisTemplate.opsForList().range(listKey, start, end);
    }

    @Override
    public void addToHash(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Boolean isHasKey(String key, Object hashKeys) {
        return redisTemplate.opsForHash().hasKey(key, hashKeys);
    }
    @Override
    public Map<Object, Object> hashEntries(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public void removeFromHash(String hashKey, Object... hashKeys) {
        redisTemplate.opsForHash().delete(hashKey, hashKeys);
    }

    @Override
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

}
