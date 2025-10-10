package top.teek.uac.demo.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.websocket.core.WebSocketMessageContext;
import top.teek.websocket.helper.WebSocketHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/6/10 03:23:05
 * @note
 */
@RestController
@RequestMapping("/demo/websocket")
public class DemoWebSocketController {

    /**
     * 发布消息
     *
     */
    @GetMapping("/send")
    public Response<String> send(WebSocketMessageContext webSocketMessageContext) {
        WebSocketHelper.publishMessage(webSocketMessageContext);
        return HttpResult.ok("操作成功");
    }
}
