package cn.cloud.websocket.controller;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.common.SpringContextUtils;
import cn.cloud.websocket.enums.ChannelEnum;
import cn.cloud.websocket.enums.ExpireEnum;
import cn.cloud.websocket.model.User;
import cn.cloud.websocket.model.websocket.MessageTemplate;
import cn.cloud.websocket.model.websocket.RedisWebsocketMsg;
import cn.cloud.websocket.model.websocket.RequestMessage;
import cn.cloud.websocket.service.impl.RedisServiceImpl;
import cn.cloud.websocket.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

@Slf4j
@Controller
public class GreetingController {
    @Value("${spring.redis.message.channel}")
    private String topicName;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;

    /**
     * 广播模式-发送系统消息
     */
    @SubscribeMapping("/system")
    public String system() {
        MessageTemplate template = MessageTemplate.builder().type("system").data("订阅系统消息成功").build();
        return JsonUtils.toJson(template);
    }

    /**
     * 点对点模式-发送系统消息
     */
    @SubscribeMapping("/system/{user}")
    public String singleSystem() {
        MessageTemplate template = MessageTemplate.builder().type("system").data("订阅系统消息成功").build();
        return JsonUtils.toJson(template);
    }

    /**
     * 广播模式-传递翻页信息
     *
     * @param message
     * @return
     */
    @MessageMapping("/page")
    @SendTo("/topic/page")
    public MessageTemplate page(MessageTemplate message) {
        return message;
    }

    /**
     * 点对点模式-传递翻页信息
     *
     * @param message
     * @return
     */
    @MessageMapping("/page/{user}")
    @SendToUser("/topic/page/{user}")
    public MessageTemplate singlePage(MessageTemplate message) {
        return message;
    }

    /**
     * 广播模式-传递指针信息
     *
     * @param message
     * @return
     */
    @MessageMapping("/pointer")
    @SendTo("/topic/pointer")
    public MessageTemplate pointer(MessageTemplate message) {
        return message;
    }

    /**
     * 点对点模式-传递指针信息
     *
     * @param message
     * @return
     */
    @MessageMapping("/pointer/{user}")
    @SendToUser("/topic/pointer/{user}")
    public MessageTemplate singlePointer(MessageTemplate message) {
        return message;
    }


    /**
     * 给指定用户发送WebSocket消息
     *
     * @param message
     * @return
     */
    @MessageMapping("/sendToUser")
    @SendToUser("/topic/response")
    public String chat(RequestMessage message) {
        String sender = message.getSender();
        String receiver = message.getReceiver();
        String msg = message.getMsg();

        HttpSession session = SpringContextUtils.getSession();
        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);
        if (loginUser == null) {
            return "用户信息为空，请先登录。";
        }

        MessageTemplate resultData = new MessageTemplate(ChannelEnum.CHANNEL_SYSTEM.getType(), msg);
        sendToUser(loginUser.getUsername(), receiver, ChannelEnum.CHANNEL_SYSTEM.getSubscribeUrl(), JsonUtils.toJson(resultData));
        return msg;
    }


    /**
     * 给指定用户发送消息，并处理接收者不在线的情况
     *
     * @param sender      消息发送者
     * @param receiver    消息接收者
     * @param destination 目的地
     * @param payload     消息正文
     */
    public void sendToUser(String sender, String receiver, String destination, String payload) {
        SimpUser simpUser = userRegistry.getUser(receiver);

        //如果接收者在线，并在本节点，则发送消息
        if (StringUtils.isNotBlank(simpUser.getName())) {
            messagingTemplate.convertAndSendToUser(receiver, destination, payload);
        }

        //如果接收者在线，在集群的其他节点，需要通知接收者连接的那个节点发送消息
        else if (redisService.isSetMember(Constants.REDIS_WEBSOCKET_USER_SET, receiver)) {
            RedisWebsocketMsg<String> redisWebsocketMsg = new RedisWebsocketMsg<>(receiver, ChannelEnum.CHANNEL_SYSTEM.getType(), payload);
            redisService.convertAndSend(topicName, JsonUtils.toJson(redisWebsocketMsg));
        }

        //否则将消息存储到redis，等用户上线后主动拉取未读消息
        else {
            //存储消息的Redis列表名
            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + receiver + ":" + destination;
            log.info(MessageFormat.format("消息接收者{0}还未建立WebSocket连接，{1}发送的消息【{2}】将被存储到Redis的【{3}】列表中",
                    receiver, sender, payload, listKey));

            //存储消息到Redis中
            redisService.addToListRight(listKey, ExpireEnum.UNREAD_MSG, payload);
        }
    }

}