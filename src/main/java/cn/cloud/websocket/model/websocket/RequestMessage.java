package cn.cloud.websocket.model.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description: 接收数据
 * @Author: ZX
 * @Date: 2019/1/25 14:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestMessage {

    private String sender;

    private String receiver;

    private String msg;

}
