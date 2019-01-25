package cn.cloud.websocket.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis相关配置
 *
 * @author zx
 * @date 2018/7/30
 * @since 1.0.0
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * redis模板，存储关键字是字符串，值是Jdk序列化
     *
     * @param factory
     * @return
     * @Description:
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // key值的序列化方式
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);

        // Value序列化方式
        JdkSerializationRedisSerializer jdkRedisSerializer = new JdkSerializationRedisSerializer();
        template.setValueSerializer(jdkRedisSerializer);
        template.setHashValueSerializer(jdkRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 缓存管理
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 初始化一个RedisCacheWriter
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        // RedisCacheConfiguration默认是使用StringRedisSerializer序列化key,JdkSerializationRedisSerializer序列化value
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        // 设置默认超过期时间
        RedisCacheConfiguration duration = cacheConfig.entryTtl(Duration.ofHours(3));

        // 初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter, duration);

        return cacheManager;
    }

}
