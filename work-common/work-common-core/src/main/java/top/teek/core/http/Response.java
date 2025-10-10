package top.teek.core.http;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Teeker
 * @date 2022/4/30 14:47
 * @note 响应对象
 */
@Getter
@Setter
public class Response<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -464624820023286858L;
    /**
     * 自定义状态码
     **/
    private Integer code;
    /**
     * 状态码信息
     **/
    protected String status;
    /**
     * 消息
     **/
    private String message;
    /**
     * 时间戳
     **/
    private Long timestamp;
    /**
     * 数据
     **/
    protected T data;

    public static <T> Response<T> instance() {
        return new Response<>();
    }

}
