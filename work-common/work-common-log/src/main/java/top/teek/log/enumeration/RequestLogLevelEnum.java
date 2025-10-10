package top.teek.log.enumeration;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Teeker
 * @date 2023/6/30 22:47
 * @note
 */
@Getter
@RequiredArgsConstructor
public enum RequestLogLevelEnum {
    /**
     * No logs.
     */
    NONE(0),

    /**
     * Logs request and response lines.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    BASIC(1),

    /**
     * Logs request and response lines and their respective headers.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    HEADERS(2),

    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    BODY(3);

    /**
     * 级别
     */
    private final int level;

    /**
     * 请求日志配置前缀
     */
    public static final String REQ_LOG_PROPS_PREFIX = "controller.log";
    /**
     * 控制台日志是否启用
     */
    public static final String CONSOLE_LOG_ENABLED_PROP = "controller.log.console.enabled";

    /**
     * 当前版本 小于和等于 比较的版本
     *
     * @param level LogLevel
     * @return 是否小于和等于
     */
    public boolean lte(RequestLogLevelEnum level) {
        return this.level <= level.level;
    }
}