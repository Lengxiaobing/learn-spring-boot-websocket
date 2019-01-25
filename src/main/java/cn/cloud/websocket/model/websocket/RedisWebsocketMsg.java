package cn.cloud.websocket.model.websocket;

import cn.cloud.websocket.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Redis中存储WebSocket消息
 *
 * @author zx
 * @date 2018/10/16
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RedisWebsocketMsg<T> {
    /**
     * 消息接收者的username
     */
    private String receiver;
    /**
     * 消息对应的订阅频道的CODE，参考{@link ChannelEnum}的code字段
     */
    private String channelCode;
    /**
     * 消息正文
     */
    private T content;

}
