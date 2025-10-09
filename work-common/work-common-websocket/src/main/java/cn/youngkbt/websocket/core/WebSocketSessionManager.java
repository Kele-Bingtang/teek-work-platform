package cn.youngkbt.websocket.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kele-Bingtang
 * @date 2024/6/9 19:35:59
 * @note WebSocketSession 用于保存当前所有在线的会话信息
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketSessionManager {

    private static final Map<String, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 将 WebSocket 会话添加到用户会话 Map 中
     *
     * @param sessionKey 会话键，用于检索会话
     * @param session    要添加的 WebSocket 会话
     */
    public static void addSession(String sessionKey, WebSocketSession session) {
        USER_SESSION_MAP.put(sessionKey, session);
    }

    /**
     * 根据会话键从用户会话 Map 中获取 WebSocket 会话
     *
     * @param sessionKey 要获取的会话键
     * @return 与给定会话键对应的 WebSocket 会话，如果不存在则返回 null
     */
    public static WebSocketSession getSessions(String sessionKey) {
        return USER_SESSION_MAP.get(sessionKey);
    }

    /**
     * 获取存储在用户会话 Map 中所有 WebSocket 会话的会话键集合
     *
     * @return 所有 WebSocket 会话的会话键集合
     */
    public static Set<String> getSessionsAll() {
        return USER_SESSION_MAP.keySet();
    }

    /**
     * 从用户会话 Map 中移除指定会话键对应的 WebSocket 会话
     *
     * @param sessionKey 要移除的会话键
     */
    public static void removeSession(String sessionKey) {
        if (!existSession(sessionKey)) {
            return;
        }
        WebSocketSession session = USER_SESSION_MAP.remove(sessionKey);
        try {
            session.close(CloseStatus.BAD_DATA);
        } catch (IOException ignored) {
        }
    }

    /**
     * 检查给定的会话键是否存在于用户会话 Map 中
     *
     * @param sessionKey 要检查的会话键
     * @return 如果存在对应的会话键，则返回 true；否则返回 false
     */
    public static boolean existSession(String sessionKey) {
        return USER_SESSION_MAP.containsKey(sessionKey);
    }
}
