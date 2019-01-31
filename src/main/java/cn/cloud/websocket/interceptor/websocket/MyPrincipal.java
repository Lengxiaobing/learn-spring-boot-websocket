package cn.cloud.websocket.interceptor.websocket;

import java.security.Principal;

/**
 * 自定义 Principal
 *
 * @author zx
 * @date 2018/10/11
 * @since 1.0.0
 */
public class MyPrincipal implements Principal {
    private String loginName;
    private String roomNum;
    private String type;

    public MyPrincipal(String loginName, String roomNum, String type) {
        this.loginName = loginName;
        this.roomNum = roomNum;
        this.type = type;
    }

    @Override
    public String getName() {
        return loginName;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getType() {
        return type;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setType(String type) {
        this.type = type;
    }
}
