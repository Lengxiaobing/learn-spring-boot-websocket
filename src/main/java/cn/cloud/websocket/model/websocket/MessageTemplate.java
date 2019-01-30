package cn.cloud.websocket.model.websocket;

import lombok.*;

/**
 * 消息模板
 *
 * @author zx
 * @date 2018/9/30
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MessageTemplate {

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接收者
     */
    private String receiver;

    /**
     * 房间号
     */
    private String roomNum;

    /**
     * 标记
     */
    private String sign;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 消息主体
     */
    private Object msg;

    public MessageTemplate(String type, Object msg) {
        this.type = type;
        this.msg = msg;
    }

    public MessageTemplate(String type, String destination, Object msg) {
        this.type = type;
        this.destination = destination;
        this.msg = msg;
    }

}
