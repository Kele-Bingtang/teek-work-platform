package top.teek.websocket.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/9 19:35:13
 * @note Websocket 消息上下文
 */
@Data
public class WebSocketMessageContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 需要推送到的 session key 列表
     */
    private List<String> sessionKeys;

    /**
     * 需要发送的消息
     */
    private String message;
}
