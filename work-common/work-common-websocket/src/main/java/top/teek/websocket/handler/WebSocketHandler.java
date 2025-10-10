package top.teek.websocket.handler;

import top.teek.security.domain.LoginUser;
import top.teek.websocket.constant.WebSocketConstant;
import top.teek.websocket.core.WebSocketMessageContext;
import top.teek.websocket.core.WebSocketSessionManager;
import top.teek.websocket.helper.WebSocketHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/6/9 19:38:28
 * @note WebSocketHandler 实现类
 */
@Slf4j
public class WebSocketHandler extends AbstractWebSocketHandler {

    /**
     * 连接成功后事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(WebSocketConstant.LOGIN_USER_KEY);
        WebSocketSessionManager.addSession(loginUser.getUserId(), session);
        log.info("[connect] sessionId: {}, userId:{}, username:{}", session.getId(), loginUser.getUserId(), loginUser.getUsername());
    }

    /**
     * 处理接收到的文本消息事件
     *
     * @param session WebSocket 会话
     * @param message 接收到的文本消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 从 WebSocket 会话中获取登录用户信息
        LoginUser loginUser = (LoginUser) session.getAttributes().get(WebSocketConstant.LOGIN_USER_KEY);

        // 创建 WebSocket 消息上下文对象
        WebSocketMessageContext webSocketMessageContext = new WebSocketMessageContext();
        webSocketMessageContext.setSessionKeys(List.of(loginUser.getUserId()));
        webSocketMessageContext.setMessage(message.getPayload());
        WebSocketHelper.publishMessage(webSocketMessageContext);
    }

    /**
     * 处理接收到的二进制消息事件
     *
     * @param session WebSocket 会话
     * @param message 接收到的二进制消息
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }

    /**
     * 处理接收到的 Pong 消息（心跳监测）
     *
     * @param session WebSocket 会话
     * @param message 接收到的 Pong 消息
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        WebSocketHelper.sendPongMessage(session);
    }

    /**
     * 处理 WebSocket 传输错误
     *
     * @param session   WebSocket会话
     * @param exception 发生的异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("[transport error] sessionId: {} , exception:{}", session.getId(), exception.getMessage());
    }

    /**
     * 在 WebSocket 连接关闭后执行清理操作
     *
     * @param session WebSocket会话
     * @param status  关闭状态信息
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LoginUser loginUser = (LoginUser) session.getAttributes().get(WebSocketConstant.LOGIN_USER_KEY);
        if (Objects.isNull(loginUser)) {
            return;
        }
        WebSocketSessionManager.removeSession(loginUser.getUserId());
        log.info("[disconnect] sessionId: {},userId:{},username:{}", session.getId(), loginUser.getUserId(), loginUser.getUsername());
    }

    /**
     * 指示处理程序是否支持接收部分消息
     *
     * @return 如果支持接收部分消息，则返回 true；否则返回 false
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
