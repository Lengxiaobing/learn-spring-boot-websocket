package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义握手处理程序，实现生成自定义的Principal
 *
 * @author zx
 * @date 2018/10/11
 * @since 1.0.0
 */
@Slf4j
@Component
public class MyHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private RedisService redisService;

    /**
     * 操作用户，判定用户是否登录
     *
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String room = null;
        String loginUser = null;
        String sign = null;
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            loginUser = servletRequest.getServletRequest().getParameter(Constants.WEBSOCKET_USER);
            room = servletRequest.getServletRequest().getParameter(Constants.WEBSOCKET_ROOM);
            sign = servletRequest.getServletRequest().getParameter(Constants.WEBSOCKET_SIGN);

            if (loginUser != null) {
                log.info("WebSocket连接开始创建Principal，用户：{}", loginUser);
                //1. 将用户名存到Redis中
                redisService.addToHash(Constants.REDIS_WEBSOCKET_USER_SET + room, loginUser, sign);

                //2. 返回自定义的Principal
                return new MyPrincipal(loginUser, room, sign);
            } else {
                log.info("未登录系统，禁止连接WebSocket");
                return new MyPrincipal(null, room, sign);
            }
        }
        return new MyPrincipal(loginUser, room, sign);
    }

}
