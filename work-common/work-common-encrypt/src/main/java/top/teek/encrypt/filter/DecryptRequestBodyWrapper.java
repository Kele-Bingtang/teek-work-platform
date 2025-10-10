package top.teek.encrypt.filter;

import cn.hutool.core.io.IoUtil;
import top.teek.encrypt.helper.EncryptHelper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Teeker
 * @date 2024/6/8 23:32:29
 * @note 请求体包装类，实现解密功能
 */
public class DecryptRequestBodyWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    /**
     * 采用 AES 加密数据，加密使用的密钥由 RSA 加密而得到
     *
     * @param request request
     * @param privateKey       RSA 私钥 (用于解密 AES 秘钥)
     * @param headerFlag      请求头标志
     */
    public DecryptRequestBodyWrapper(HttpServletRequest request, String privateKey, String headerFlag) throws IOException {
        super(request);
        // 获取 AES 密码，该密码采用 RSA 加密
        String headerRsa = request.getHeader(headerFlag);
        String decryptAes = EncryptHelper.decryptByRsa(headerRsa, privateKey);
        // 解密 AES 密码
        String aesPassword = EncryptHelper.decryptByBase64(decryptAes);
        request.setCharacterEncoding("UTF-8");
        byte[] readBytes = IoUtil.readBytes(request.getInputStream(), false);
        String requestBody = new String(readBytes, StandardCharsets.UTF_8);
        // 解密 body 采用 AES 加密
        String decryptBody = EncryptHelper.decryptByAes(requestBody, aesPassword);
        body = decryptBody.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    @Override
    public int getContentLength() {
        return body.length;
    }

    @Override
    public long getContentLengthLong() {
        return body.length;
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }


    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public int available() {
                return body.length;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
