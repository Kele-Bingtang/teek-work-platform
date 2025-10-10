package top.teek.websocket.helper;

import top.teek.redis.utils.RedisUtil;
import top.teek.utils.ListUtil;
import top.teek.websocket.constant.WebSocketConstant;
import top.teek.websocket.core.WebSocketMessageContext;
import top.teek.websocket.core.WebSocketSessionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/9 19:34:17
 * @note WebSocket 工具类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketHelper {

    /**
     * 向指定的 WebSocket 会话发送消息
     *
     * @param sessionKey 要发送消息的用户 id
     * @param message    要发送的消息内容
     */
    public static void sendMessage(String sessionKey, String message) {
        WebSocketSession session = WebSocketSessionManager.getSessions(sessionKey);
        sendMessage(session, message);
    }

    /**
     * 订阅 WebSocket 消息主题，并提供一个消费者函数来处理接收到的消息
     *
     * @param listener 处理 WebSocket 消息的消费者
     */
    public static void subscribeMessage(MessageListener listener) {
        RedisUtil.subscribe(WebSocketConstant.WEB_SOCKET_TOPIC, listener);
    }

    /**
     * 发布 WebSocket 订阅消息
     *
     * @param webSocketMessage 要发布的 WebSocket 消息对象
     */
    public static void publishMessage(WebSocketMessageContext webSocketMessage) {
        List<String> unsentSessionKeys = new ArrayList<>();
        // 当前服务内 session,直接发送消息
        if (Objects.isNull(webSocketMessage.getSessionKeys())) {
            return;
        }
        for (String sessionKey : webSocketMessage.getSessionKeys()) {
            if (WebSocketSessionManager.existSession(sessionKey)) {
                WebSocketHelper.sendMessage(sessionKey, webSocketMessage.getMessage());
                // 发生后则移除，防止添加到 unsentSessionKeys，因为下面 unsentSessionKeys 会通过 Redis 发布，监听器订阅到消息后，获取消息发送给 unsentSessionKeys 的 sessionKey
                continue;
            }
            unsentSessionKeys.add(sessionKey);
        }
        // 不在当前服务内 session，发布订阅消息
        if (ListUtil.isNotEmpty(unsentSessionKeys)) {
            WebSocketMessageContext broadcastMessage = new WebSocketMessageContext();
            broadcastMessage.setMessage(webSocketMessage.getMessage());
            broadcastMessage.setSessionKeys(unsentSessionKeys);
            RedisUtil.publish(WebSocketConstant.WEB_SOCKET_TOPIC, broadcastMessage);
            log.info(" WebSocket 发送主题订阅消息 topic：{}，session keys：{}，message：{}", WebSocketConstant.WEB_SOCKET_TOPIC, unsentSessionKeys, webSocketMessage.getMessage());
        }
    }

    /**
     * 向所有的 WebSocket 会话发布订阅的消息(群发)
     *
     * @param message 要发布的消息内容
     */
    public static void publishAll(String message) {
        WebSocketMessageContext broadcastMessage = new WebSocketMessageContext();
        broadcastMessage.setMessage(message);
        RedisUtil.publish(WebSocketConstant.WEB_SOCKET_TOPIC, broadcastMessage);
        log.info(" WebSocket 发送主题订阅消息 topic：{}，message：{}", WebSocketConstant.WEB_SOCKET_TOPIC, message);
    }

    /**
     * 向指定的 WebSocket 会话发送 Pong 消息
     *
     * @param session 要发送 Pong 消息的 WebSocket 会话
     */
    public static void sendPongMessage(WebSocketSession session) {
        sendMessage(session, new PongMessage());
    }

    /**
     * 向指定的 WebSocket 会话发送文本消息
     *
     * @param session WebSocket 会话
     * @param message 要发送的文本消息内容
     */
    public static void sendMessage(WebSocketSession session, String message) {
        sendMessage(session, new TextMessage(message));
    }

    /**
     * 向指定的 WebSocket 会话发送 WebSocket 消息对象
     *
     * @param session WebSocket 会话
     * @param message 要发送的 WebSocket 消息对象
     */
    private synchronized static void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (Objects.isNull(session) || !session.isOpen()) {
            log.warn("[send] session 会话已经关闭");
        } else {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("[send] session：{}，发送消息：{} 异常", session, message, e);
            }
        }
    }
}
