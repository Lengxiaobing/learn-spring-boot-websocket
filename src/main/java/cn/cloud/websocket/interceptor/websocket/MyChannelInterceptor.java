package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.enums.ChannelEnum;
import cn.cloud.websocket.model.websocket.MessageTemplate;
import cn.cloud.websocket.service.RedisService;
import cn.cloud.websocket.utils.JsonUtils;
import cn.cloud.websocket.utils.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义通道拦截器，实现断开连接的处理
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
            MyPrincipal principal = (MyPrincipal) accessor.getUser();
            if (principal != null && StringUtils.isNotBlank(principal.getName())) {
                user = principal.getName();
                String roomNum = principal.getRoomNum();
                //判断redis中是否存在该用户
                Boolean hasKey = redisService.isHasKey(Constants.REDIS_WEBSOCKET_USER_SET + roomNum, user);
                if (hasKey) {
                    //从Redis中移除用户
                    redisService.removeFromHash(Constants.REDIS_WEBSOCKET_USER_SET + roomNum, user);

                    //发送下线人员消息
                    Map<Object, Object> map = redisService.hashEntries(Constants.REDIS_WEBSOCKET_USER_SET + roomNum);
                    MessageTemplate template = MessageTemplate.builder()
                            .sender("system")
                            .receiver("all")
                            .roomNum(roomNum)
                            .sign("offline")
                            .type("system")
                            .destination(ChannelEnum.CHANNEL_ENTIRE.getSubscribeUrl())
                            .msg(map)
                            .build();
                    WebSocketUtil.sendToAll(ChannelEnum.CHANNEL_ENTIRE.getSubscribeUrl() + "/" + roomNum, JsonUtils.toJson(template));
                }
            } else {
                user = accessor.getSessionId();
            }
            log.info("用户{}的WebSocket连接已经断开", user);
        }
    }

}
