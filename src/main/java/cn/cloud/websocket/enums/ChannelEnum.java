package cn.cloud.websocket.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * WebSocket Channel枚举类
 *
 * @author zx
 * @date 2018/10/16
 * @since 1.0.0
 */
public enum ChannelEnum {

    /**
     * 消息通道--广播模式
     */
    CHANNEL_ENTIRE("entire", "/topic/entire"),
    /**
     * 消息通道--点对点模式
     */
    CHANNEL_SINGLE("single", "/queue/single");


    /**
     * 类型
     */
    private String type;
    /**
     * WebSocket客户端订阅的URL
     */
    private String subscribeUrl;


    ChannelEnum(String type, String subscribeUrl) {
        this.type = type;
        this.subscribeUrl = subscribeUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubscribeUrl() {
        return subscribeUrl;
    }

    public void setSubscribeUrl(String subscribeUrl) {
        this.subscribeUrl = subscribeUrl;
    }

    /**
     * 通过Type查找枚举类
     *
     * @param type
     * @return
     */
    public static ChannelEnum fromCode(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (ChannelEnum channelEnum : values()) {
                if (channelEnum.type.equals(type)) {
                    return channelEnum;
                }
            }
        }
        return null;
    }

}
