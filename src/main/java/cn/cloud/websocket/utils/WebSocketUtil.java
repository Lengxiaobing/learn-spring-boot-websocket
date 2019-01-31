package cn.cloud.websocket.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: 发送websocket消息
 * @Author: ZX
 * @Date: 2019/1/31 10:26
 */
@Slf4j
@Service
public class WebSocketUtil {

    @Autowired
    private static SimpMessagingTemplate template;

    public WebSocketUtil(SimpMessagingTemplate tmplt) {
        template = tmplt;
    }

    /**
     * 发送给指定的人员
     *
     * @param user
     * @param destination
     * @param message
     */
    public static void sendToUser(String user, String destination, String message) {
        template.convertAndSendToUser(user, destination, message);
    }

    /**
     * 发送给所有人员
     *
     * @param destination
     * @param message
     */
    public static void sendToAll(String destination, String message) {
        template.convertAndSend(destination, message);
    }

}