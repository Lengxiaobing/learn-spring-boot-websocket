package cn.cloud.websocket.model.websocket;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 消息模板
 *
 * @author zx
 * @date 2018/9/30
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@ToString
@Builder
public class MessageTemplate {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息主体
     */
    private String data;

    public MessageTemplate(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
