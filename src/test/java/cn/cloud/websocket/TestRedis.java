package cn.cloud.websocket;

import cn.cloud.websocket.enums.ExpireEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试redis的基本用法
 *
 * @author zx
 * @date 2018/7/27
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRedis {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testSelect() {
        redisTemplate.boundListOps("admin");
        redisTemplate.opsForList().leftPushAll("admin", "n");

        redisTemplate.expire("admin", ExpireEnum.UNREAD_MSG.getTime(), ExpireEnum.UNREAD_MSG.getTimeUnit());

        List<String> list = redisTemplate.opsForList().range("admin", 0, -1);
        list.forEach(System.out::print);
        redisTemplate.delete("admin");
    }

}
