package cn.cloud.websocket.common;

/**
 * 公共常量类
 *
 * @author zx
 * @date 2018/7/25
 * @since 1.0.0
 */
public class Constants {
    /**
     * websocket的用户名
     */
    public static final String WEBSOCKET_USER = "user";

    /**
     * websocket的房间号
     */
    public static final String WEBSOCKET_ROOM = "roomNum";

    /**
     * websocket的控制类型
     */
    public static final String WEBSOCKET_SIGN = "sign";

    /**
     * 登录页面的回调地址，在session中存储的变量名
     */
    public static final String LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 用户未读的WebSocket消息，在Redis中存储的变量名的前缀
     */
    public static final String REDIS_UNREAD_MSG_PREFIX = "UNREAD_MSG:";

    /**
     * 建立连接的WebSocket用户，在Redis中存储的SET名称
     */
    public static final String REDIS_WEBSOCKET_USER_SET = "WEBSOCKET_USER_SET:";
}
