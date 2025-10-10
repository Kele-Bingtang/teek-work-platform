package top.teek.integrate.system.config;

import top.teek.core.exception.ServerException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import feign.form.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Teeker
 * @date 2024-10-28 00:36:15
 * @note
 */
@Configuration
@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try {
            String errorContent = Util.toString(response.body().asReader(CharsetUtil.UTF_8));
            log.info("错误信息：" + errorContent);
            throw new ServerException(errorContent);
        } catch (IOException e) {
            log.error("FeignClientErrorDecoder decode exception", e);
            throw new ServerException(e.getMessage());
        }
    }
}