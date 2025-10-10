package top.teek.uac.demo.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.ratelimiter.annotations.RateLimit;
import top.teek.ratelimiter.enumerate.RateLimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/6/11 00:55:54
 * @note
 */
@RestController
@RequestMapping("/demo/rateLimit")
public class RateLimitController {

    /**
     * 全局限流
     */
    @RateLimit(count = 2, expire = 10)
    @GetMapping("/global")
    public Response<String> global() {
        return HttpResult.ok("操作成功");
    }

    /**
     * 用户 ID 限流
     */
    @RateLimit(count = 2, expire = 10, rateLimitType = RateLimitType.USER)
    @GetMapping("/user")
    public Response<String> user() {
        return HttpResult.ok("操作成功");
    }

    /**
     * 测试请求 IP 限流
     */
    @RateLimit(count = 2, expire = 10, rateLimitType = RateLimitType.IP)
    @GetMapping("/ip")
    public Response<String> ip(String value) {
        return HttpResult.ok("操作成功");
    }

    /**
     * 用户 ID 限流（Key 基于参数获取）
     * key 可以为 #value，也可以为 #{#value}
     */
    @RateLimit(key = "#value", count = 2, expire = 10, rateLimitType = RateLimitType.USER)
    @GetMapping("/userKey")
    public Response<String> userKey(String value) {
        return HttpResult.ok("操作成功");
    }
}
