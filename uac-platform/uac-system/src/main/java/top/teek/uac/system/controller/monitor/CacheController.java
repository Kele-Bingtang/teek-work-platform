package top.teek.uac.system.controller.monitor;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.uac.system.model.vo.extra.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Teeker
 * @date 2024/4/9 下午9:48
 * @note
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/cache")
public class CacheController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    @PreAuthorize("hasAuthority('system:cache:list')")
    public Response<CacheInfo> list() {

        RedisServerCommands redisServerCommands = redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands();
        // 获取 Redis 缓存命令统计信息
        Properties commandStats = redisServerCommands.info("commandstats");
        List<Map<String, String>> pieList = new ArrayList<>();

        if (Objects.nonNull(commandStats)) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }

        CacheInfo cacheInfo =  new CacheInfo();
        cacheInfo.setInfo(redisServerCommands.info()); // 获取 Redis 缓存完整信息
        cacheInfo.setDbSize(redisServerCommands.dbSize()); // 获取 Redis 缓存中可用键 Key 的总数
        cacheInfo.setCommandStats(pieList);
        return HttpResult.ok(cacheInfo);
    }
}
