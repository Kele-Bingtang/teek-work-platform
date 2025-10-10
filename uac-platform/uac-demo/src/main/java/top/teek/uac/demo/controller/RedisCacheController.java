package top.teek.uac.demo.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/6/12 01:09:58
 * @note
 */
@RestController
@RequestMapping("/demo/cache")
@RequiredArgsConstructor
public class RedisCacheController {

    @Cacheable(cacheNames = "demo:cache#60s", key = "#key", condition = "#key != null")
    @GetMapping("/test1")
    public Response<String> test1(String key, String value) {
        return HttpResult.ok("操作成功：" + value);
    }

    @CachePut(cacheNames = "demo:cache#60s", key = "#key", condition = "#key != null")
    @GetMapping("/test2")
    public Response<String> test2(String key, String value) {
        return HttpResult.ok("操作成功：" + value);
    }


    @CacheEvict(cacheNames = "demo:cache#60s", key = "#key", condition = "#key != null")
    @GetMapping("/test3")
    public Response<String> test3(String key, String value) {
        return HttpResult.ok("操作成功：" + value);
    }

}
