package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.enums.ChannelEnum;
import cn.cloud.websocket.model.websocket.MessageTemplate;
import cn.cloud.websocket.service.RedisService;
import cn.cloud.websocket.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

/**
 * @Description: 发送消息
 * @Author: ZX
 * @Date: 2019/1/30 11:00
 */
public class SendInfo {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisService redisService;


    /**
     * 握手后
     *
     * @param roomNum
     */
    public void afterHandshake(String roomNum) {
        Map<Object, Object> map = redisService.hashEntries(Constants.REDIS_WEBSOCKET_USER_SET + roomNum);
        MessageTemplate template = MessageTemplate.builder()
                .sender("system")
                .receiver("all")
                .roomNum(roomNum)
                .sign("online")
                .type("system")
                .destination(ChannelEnum.CHANNEL_ENTIRE.getSubscribeUrl())
                .msg(map)
                .build();
        messagingTemplate.convertAndSend(ChannelEnum.CHANNEL_ENTIRE.getSubscribeUrl() + "/" + roomNum, JsonUtils.toJson(template));
    }

    /**
     * 发送完成后
     *
     * @param roomNum
     * @param user
     */
    public void afterSendCompletion(String roomNum, String user) {
        MessageTemplate template = MessageTemplate.builder()
                .sender("system")
                .receiver("all")
                .roomNum(roomNum)
                .sign("offline")
                .type("system")
                .destination(ChannelEnum.CHANNEL_ENTIRE.getSubscribeUrl())
                .msg("用户 " + user + " 下线。")
                .build();
        messagingTemplate.convertAndSend("/topic/entire/0", JsonUtils.toJson(template));
    }
}
