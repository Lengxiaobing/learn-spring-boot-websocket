package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.text.MessageFormat;

/**
 * 自定义{@link ChannelInterceptor}，实现断开连接的处理
 *
 * @author zx
 * @date 2018/10/10
 * @since 1.0.0
 */
@Slf4j
@Component
public class MyChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private RedisService redisService;

    /**
     * 信息发送完成事件
     *
     * @param message
     * @param channel
     * @param sent
     * @param ex
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        //用户断开连接
        if (StompCommand.DISCONNECT.equals(command)) {
            String user;
            Principal principal = accessor.getUser();
            if (principal != null && StringUtils.isNotBlank(principal.getName())) {
                user = principal.getName();

                //从Redis中移除用户
                redisService.removeFromSet(Constants.REDIS_WEBSOCKET_USER_SET, user);
            } else {
                user = accessor.getSessionId();
            }
            log.debug(MessageFormat.format("用户{0}的WebSocket连接已经断开", user));
        }
    }

}
