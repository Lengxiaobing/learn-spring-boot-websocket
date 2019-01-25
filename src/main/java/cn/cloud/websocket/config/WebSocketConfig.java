package cn.cloud.websocket.config;

import cn.cloud.websocket.interceptor.websocket.AuthHandshakeInterceptor;
import cn.cloud.websocket.interceptor.websocket.MyChannelInterceptor;
import cn.cloud.websocket.interceptor.websocket.MyHandshakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket相关配置
 *
 * @author zx
 * @date 2018/9/30
 * @since 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;

    @Autowired
    private MyHandshakeHandler myHandshakeHandler;

    @Autowired
    private MyChannelInterceptor myChannelInterceptor;

    /**
     * 注册Stomp端点
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //注册STOMP端点，即WebSocket客户端连接时用到的WebSocket握手端点
        //添加一个/point端点，客户端就可以通过这个端点来进行连接
        registry.addEndpoint("/point")
                //跨域设置
                .setAllowedOrigins("*")
                // 自定义握手拦截器
                .addInterceptors(authHandshakeInterceptor)
                // 自定义握手处理程序
                .setHandshakeHandler(myHandshakeHandler)
                //启用SockJS功能
                .withSockJS();
    }

    /**
     * 配置消息代理
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //定义了一个客户端订阅地址的前缀信息，也就是客户端接收服务端发送消息的前缀信息
        registry.enableSimpleBroker("/topic");

        //定义了服务端接收地址的前缀，也即客户端给服务端发消息的地址前缀，将直接路由到，带有@MessageMapping注解的控制器方法
        registry.setApplicationDestinationPrefixes("/app");

        //给指定用户发送消息的路径前缀，默认值是/user/
        registry.setUserDestinationPrefix("/user/");
    }

    /**
     * 配置客户端入站通道
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //配置自定义通道拦截器
        registration.interceptors(myChannelInterceptor);
    }

}
