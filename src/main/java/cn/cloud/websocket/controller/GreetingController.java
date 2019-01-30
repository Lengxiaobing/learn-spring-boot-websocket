package cn.cloud.websocket.controller;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.enums.ChannelEnum;
import cn.cloud.websocket.model.websocket.MessageTemplate;
import cn.cloud.websocket.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
public class GreetingController {

    @Autowired
    private RedisService redisService;

    /**
     * 广播模式-发送消息
     */
    @MessageMapping("/entire")
    @SendTo("/topic/entire")
    public MessageTemplate entire(MessageTemplate msg) {
        return msg;
    }

    /**
     * 广播模式-分组发送消息
     */
    @MessageMapping("/entire/{roomNum}")
    @SendTo("/topic/entire/{roomNum}")
    public MessageTemplate roomNum(MessageTemplate msg, @DestinationVariable String roomNum) {
        return msg;
    }

    /**
     * 广播模式-获取在线人员
     */
    @MessageMapping("/online/{roomNum}")
    @SendTo("/topic/online/{roomNum}")
    public MessageTemplate onlinePersonnel(@DestinationVariable String roomNum) {
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
        return template;
    }
}