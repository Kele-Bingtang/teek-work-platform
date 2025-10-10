package top.teek.websocket.interceptor;

import top.teek.security.domain.LoginUser;
import top.teek.security.utils.LoginHelper;
import top.teek.websocket.constant.WebSocketConstant;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author Teeker
 * @date 2024/6/9 19:26:32
 * @note WebSocket 握手请求的拦截器
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * WebSocket 握手之前执行的前置处理方法
     *
     * @param request    WebSocket握手请求
     * @param response   WebSocket握手响应
     * @param wsHandler  WebSocket处理程序
     * @param attributes 与WebSocket会话关联的属性
     * @return 如果允许握手继续进行，则返回true；否则返回false
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        attributes.put(WebSocketConstant.LOGIN_USER_KEY, loginUser);
        return true;
    }

    /**
     * WebSocket 握手成功后执行的后置处理方法
     *
     * @param request   WebSocket握手请求
     * @param response  WebSocket握手响应
     * @param wsHandler WebSocket处理程序
     * @param exception 握手过程中可能出现的异常
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 在这个方法中可以执行一些握手成功后的后续处理逻辑，比如记录日志或者其他操作
    }
}
