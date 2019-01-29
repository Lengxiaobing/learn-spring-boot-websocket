package cn.cloud.websocket.controller;

import cn.cloud.websocket.model.websocket.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class GreetingController {

    /**
     * 广播模式-发送消息
     */
    @MessageMapping("/entire")
    @SendTo("/topic/entire")
    public MessageTemplate entire(MessageTemplate msg) {
        return msg;
    }

}