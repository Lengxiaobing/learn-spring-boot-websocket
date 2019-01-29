package cn.cloud.websocket.controller;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.common.SpringContextUtils;
import cn.cloud.websocket.enums.ExpireEnum;
import cn.cloud.websocket.model.User;
import cn.cloud.websocket.model.websocket.MessageTemplate;
import cn.cloud.websocket.model.websocket.RedisWebsocketMsg;
import cn.cloud.websocket.service.impl.RedisServiceImpl;
import cn.cloud.websocket.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试{@link SimpMessagingTemplate}类的基本用法
 *
 * @author zx
 * @date 2018/10/10
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/message")
public class MessageController {
    @Value("${spring.redis.message.channel}")
    private String topicName;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;
    @Autowired
    private RedisServiceImpl redisService;

    /**
     * 根据登录人员，拉取未读的WebSocket消息
     *
     * @return java.util.Map
     */
    @PostMapping("/pullUnreadMessage")
    @ResponseBody
    public Map<String, Object> pullUnreadMessage() {
        Map<String, Object> result = new HashMap<>();
        try {
            HttpSession session = SpringContextUtils.getSession();
            //当前登录用户
            User loginUser = (User) session.getAttribute(Constants.SESSION_USER);

            //存储消息的Redis列表名
            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + loginUser.getUsername();
            //从Redis中拉取所有未读消息
            List<String> messageList = redisService.rangeList(listKey, 0, -1);

            result.put("code", "200");
            if (messageList.size() > 0) {
                //删除Redis中的这个未读消息列表
                redisService.delete(listKey);
                //将数据添加到返回集，供前台页面展示
                result.put("result", messageList);
            }
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 群发信息
     *
     * @param msg
     * @return
     */
    @PostMapping("/send")
    @ResponseBody
    public String sendToEntire(@RequestBody MessageTemplate msg) {
        messagingTemplate.convertAndSend(msg.getDestination(), msg);
        return "success";
    }

    /**
     * 给指定用户发送消息
     *
     * @param msg
     * @return
     */
    @PostMapping("/sendToUser")
    @ResponseBody
    public String sendToSingle(@RequestBody MessageTemplate msg) {
        sendToUser(msg.getSender(), msg.getReceiver(), msg.getDestination(), JsonUtils.toJson(msg));
        return "success";
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
        if (simpUser != null && StringUtils.isNotBlank(simpUser.getName())) {
            messagingTemplate.convertAndSendToUser(receiver, destination, payload);
        }

        //如果接收者在线，在集群的其他节点，需要通知接收者连接的那个节点发送消息
        else if (redisService.isSetMember(Constants.REDIS_WEBSOCKET_USER_SET, receiver)) {
            RedisWebsocketMsg<String> redisWebsocketMsg = new RedisWebsocketMsg<>(receiver, destination, payload);
            redisService.convertAndSend(topicName, JsonUtils.toJson(redisWebsocketMsg));
        }

        //否则将消息存储到redis，等用户上线后主动拉取未读消息
        else {
            //存储消息的Redis列表名
            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + receiver;
            log.info("消息接收者{}还未建立WebSocket连接，{}发送的消息【{}】将被存储到Redis的【{}】列表中",
                    receiver, sender, payload, listKey);

            //存储消息到Redis中
            redisService.addToListRight(listKey, ExpireEnum.UNREAD_MSG, payload);
        }
    }
}
