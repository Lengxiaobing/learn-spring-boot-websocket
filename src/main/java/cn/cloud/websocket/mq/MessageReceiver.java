package cn.cloud.websocket.mq;

import cn.cloud.websocket.enums.ChannelEnum;
import cn.cloud.websocket.model.websocket.RedisWebsocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

/**
 * Redis中的WebSocket消息的处理者
 *
 * @author zx
 * @date 2018/10/16
 * @since 1.0.0
 */
@Slf4j
@Component
public class MessageReceiver {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry userRegistry;

    /**
     * 处理WebSocket消息
     */
    public void receiveMessage(RedisWebsocketMsg msg) {
        log.info("Received Message: {}", msg);

        //1. 取出用户名并判断是否连接到当前应用节点的WebSocket
        SimpUser simpUser = userRegistry.getUser(msg.getReceiver());

        if (simpUser != null && StringUtils.isNotBlank(simpUser.getName())) {
            //2. 获取WebSocket客户端的订阅地址
            ChannelEnum channelEnum = ChannelEnum.fromCode(msg.getChannelCode());

            if (channelEnum != null) {
                //3. 给WebSocket客户端发送消息
                messagingTemplate.convertAndSendToUser(msg.getReceiver(), channelEnum.getSubscribeUrl(), msg.getContent());
            }
        }
    }
}
