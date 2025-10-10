package top.teek.core.http;


import top.teek.core.base.BaseCommonEnum;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Teeker
 * @date 2022/4/30 14:55
 * @note 请求响应封装
 */
public class HttpResult {

    private HttpResult() {
    }

    public static <T> Response<T> response(T data, BaseCommonEnum baseCommonEnum) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(baseCommonEnum.getCode());
        response.setStatus(baseCommonEnum.getStatus());
        response.setMessage(baseCommonEnum.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> response(T data, BaseCommonEnum baseCommonEnum, String message) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(baseCommonEnum.getCode());
        response.setStatus(baseCommonEnum.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<Map<String, T>> response(String key, T data, BaseCommonEnum status) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = Response.instance();
        map.put(key, data);
        response.setData(map);
        response.setCode(status.getCode());
        response.setStatus(status.getStatus());
        response.setMessage(status.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> response(T data, Integer code, String status, String message) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(code);
        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<Map<String, T>> response(String key, T data, Integer code, String status, String message) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = Response.instance();
        map.put(key, data);
        response.setData(map);
        response.setCode(code);
        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<Map<String, T>> ok(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = Response.instance();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(ResponseStatusEnum.SUCCESS.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> okMessage(String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> ok(Integer code, String message) {
        return returnOKResponse(code, message);
    }

    public static <T> Response<T> fail(T data) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<Map<String, T>> fail(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = Response.instance();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(ResponseStatusEnum.FAIL.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> fail(BaseCommonEnum baseCommonEnum) {
        return returnResponse(baseCommonEnum);
    }

    public static <T> Response<T> fail(BaseCommonEnum baseCommonEnum, String message) {
        return returnResponse(baseCommonEnum, message);
    }

    public static <T> Response<T> failMessage(String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(ResponseStatusEnum.FAIL.getCode());
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> fail(Integer code, String message) {
        return returnFailResponse(code, message);
    }

    public static <T> Response<T> error(T data) {
        Response<T> response = Response.instance();
        response.setData(data);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<Map<String, T>> error(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        Response<Map<String, T>> response = Response.instance();
        map.put(key, data);
        response.setData(map);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(ResponseStatusEnum.ERROR.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> error(BaseCommonEnum baseCommonEnum) {
        return returnResponse(baseCommonEnum);
    }

    public static <T> Response<T> error(BaseCommonEnum baseCommonEnum, String message) {
        return returnResponse(baseCommonEnum, message);
    }

    public static <T> Response<T> errorMessage(String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(ResponseStatusEnum.ERROR.getCode());
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    public static <T> Response<T> error(Integer code, String message) {
        return returnErrorResponse(code, message);
    }

    public static <T> Response<T> okOrFail(T data, String message) {
        if (null == data) {
            return response(null, ResponseStatusEnum.FAIL, ResponseStatusEnum.FAIL.getMessage());
        }
        return response(data, ResponseStatusEnum.SUCCESS, message);
    }

    public static <T> Response<T> okOrFail(T data) {
        if (null == data) {
            return response(null, ResponseStatusEnum.FAIL);
        }
        return response(null, ResponseStatusEnum.SUCCESS);
    }

    public static <T> Response<T> okOrFail(boolean bool) {
        if (!bool) {
            return response(null, ResponseStatusEnum.FAIL);
        }
        return response(null, ResponseStatusEnum.SUCCESS);
    }

    private static <T> Response<T> returnResponse(BaseCommonEnum baseCommonEnum) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(baseCommonEnum.getCode());
        response.setStatus(baseCommonEnum.getStatus());
        response.setMessage(baseCommonEnum.getMessage());
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    private static <T> Response<T> returnResponse(BaseCommonEnum baseCommonEnum, String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(baseCommonEnum.getCode());
        response.setStatus(baseCommonEnum.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    private static <T> Response<T> returnOKResponse(Integer code, String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(code);
        response.setStatus(ResponseStatusEnum.SUCCESS.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    private static <T> Response<T> returnFailResponse(Integer code, String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(code);
        response.setStatus(ResponseStatusEnum.FAIL.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

    private static <T> Response<T> returnErrorResponse(Integer code, String message) {
        Response<T> response = Response.instance();
        response.setData(null);
        response.setCode(code);
        response.setStatus(ResponseStatusEnum.ERROR.getStatus());
        response.setMessage(message);
        response.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return response;
    }

}
