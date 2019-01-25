package cn.cloud.websocket.interceptor.websocket;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.common.SpringContextUtils;
import cn.cloud.websocket.model.User;
import cn.cloud.websocket.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 自定义{@link DefaultHandshakeHandler}，实现生成自定义的{@link Principal}
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
        HttpSession session = SpringContextUtils.getSession();
        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);

        if (loginUser != null) {
            log.debug(MessageFormat.format("WebSocket连接开始创建Principal，用户：{0}", loginUser.getUsername()));
            //1. 将用户名存到Redis中
            redisService.addToSet(Constants.REDIS_WEBSOCKET_USER_SET, loginUser.getUsername());

            //2. 返回自定义的Principal
            return new MyPrincipal(loginUser.getUsername());
        } else {
            log.error("未登录系统，禁止连接WebSocket");
            return null;
        }
    }

}
