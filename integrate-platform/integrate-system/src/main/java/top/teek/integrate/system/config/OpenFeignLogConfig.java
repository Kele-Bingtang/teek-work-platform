package top.teek.integrate.system.config;

import feign.Client;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author Teeker
 * @date 2024/10/28 00:31:06
 * @note
 */
@Configuration
public class OpenFeignLogConfig {
    @Value("${feign.log:basic}")
    private String logLevel;

    /**
     * OpenFeign 日志级别：
     * NONE: 不记录任何日志，是 OpenFeign 默认日志级别（性能最佳，适用于生产环境）
     * BASIC: 仅记录请求方法、URL、响应状态码、执行时间（适用于生产环境追踪问题）
     * HEADERS: 在记录 BASIC 级别的基础上，记录请求和响应的 header 头部信息
     * FULL: 记录请求响应的 header、body 和元数据（适用于开发和测试环境定位问题）
     */
    @Bean
    Logger.Level feignLoggerLeave() {
        if (Logger.Level.NONE.name().toLowerCase().equalsIgnoreCase(logLevel)) {
            return Logger.Level.NONE;
        }
        if (Logger.Level.BASIC.name().toLowerCase().equalsIgnoreCase(logLevel)) {
            return Logger.Level.BASIC;
        }
        if (Logger.Level.HEADERS.name().toLowerCase().equalsIgnoreCase(logLevel)) {
            return Logger.Level.HEADERS;
        }
        if (Logger.Level.FULL.name().toLowerCase().equalsIgnoreCase(logLevel)) {
            return Logger.Level.FULL;
        }
        return Logger.Level.BASIC;
    }

    /**
     * 全局 Feign 日志，在 yml文件引入
     */
    @Bean
    public feign.Logger logger() {
        return new Slf4jLogger();
    }

    /**
     * 绕过 SSL 证书
     */
    @Bean
    public Client feignClient() {
        return new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}