package top.teek.websocket.config;

import top.teek.websocket.handler.WebSocketHandler;
import top.teek.websocket.interceptor.WebSocketInterceptor;
import top.teek.websocket.listener.WebSocketTopicListener;
import top.teek.websocket.properties.WebSocketProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Teeker
 * @date 2024/6/9 21:26:37
 * @note WebSocket 配置类
 */
@AutoConfiguration
@ConditionalOnProperty(value = "websocket.enabled", havingValue = "true")
@EnableConfigurationProperties(WebSocketProperties.class)
@EnableWebSocket
public class WebSocketConfiguration {

    @Bean
    public WebSocketConfigurer webSocketConfigurer(HandshakeInterceptor handshakeInterceptor, WebSocketHandler webSocketHandler, WebSocketProperties webSocketProperties) {
        // 返回一个 WebSocketConfigurer 对象，用于配置 WebSocket
        return registry -> registry
                // 添加 WebSocket 处理程序和拦截器到指定路径，设置允许的跨域来源
                .addHandler(webSocketHandler, webSocketProperties.getPath())
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins());
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new WebSocketInterceptor();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }

    @Bean
    public WebSocketTopicListener webSocketTopicListener(RedisTemplate<String, Object> redisTemplate) {
        return new WebSocketTopicListener(redisTemplate);
    }
}
