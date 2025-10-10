package top.teek.websocket.listener;

import top.teek.utils.ListUtil;
import top.teek.websocket.constant.WebSocketConstant;
import top.teek.websocket.core.WebSocketMessageContext;
import top.teek.websocket.core.WebSocketSessionManager;
import top.teek.websocket.helper.WebSocketHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/9 20:11:25
 * @note WebSocket Redis 主题订阅监听器
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketTopicListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        if (Objects.equals(channel, WebSocketConstant.WEB_SOCKET_TOPIC)) {
            // 反序列化消息体为 WebSocketMessageContext 对象
            WebSocketMessageContext context = (WebSocketMessageContext) redisTemplate.getValueSerializer().deserialize(message.getBody());

            if (Objects.isNull(context)) {
                log.info("WebSocket 主题订阅收到消息为空");
                return;
            }
            
            log.info("WebSocket 主题订阅收到消息 channel：{}，Session Keys：{}，message：{}", channel, context.getSessionKeys(), context.getMessage());

            // 如果 key 不为空就按照 key 发消息，如果为空就群发
            if (ListUtil.isNotEmpty(context.getSessionKeys())) {
                context.getSessionKeys().forEach(key -> {
                    if (WebSocketSessionManager.existSession(key)) {
                        WebSocketHelper.sendMessage(key, context.getMessage());
                    }
                });
            } else {
                WebSocketSessionManager.getSessionsAll().forEach(key -> {
                    WebSocketHelper.sendMessage(key, context.getMessage());
                });
            }
        }
    }
}
