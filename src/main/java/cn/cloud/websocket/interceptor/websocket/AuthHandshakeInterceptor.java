package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 自定义握手拦截器，实现需要登录才允许连接WebSocket
 *
 * @author zx
 * @date 2018/10/11
 * @since 1.0.0
 */
@Slf4j
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 在握手之前。用来注册用户信息，绑定WebSocketSession，在 handler 里根据用户信息获取WebSocketSession发送消息
     *
     * @param request
     * @param response
     * @param handler
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String loginUser = servletRequest.getServletRequest().getParameter(Constants.WEBSOCKET_USER);

            if (StringUtils.isBlank(loginUser)) {
                log.info("用户未登录系统，禁止连接WebSocket");
                return false;
            } else {
                log.info("用户{}请求建立WebSocket连接", loginUser);
                return true;
            }
        }
        return true;
    }

    /**
     * 握手成功后，发送上线人员信息
     *
     * @param request
     * @param response
     * @param handler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
    }
}
