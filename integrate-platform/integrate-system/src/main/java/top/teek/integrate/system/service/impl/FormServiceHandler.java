package top.teek.integrate.system.service.impl;

/**
 * @author Teeker
 * @date 2024/10/28 00:32:12
 * @note
 */

import top.teek.core.http.Assert;
import top.teek.core.exception.ServiceException;
import top.teek.integrate.core.constants.GlobalConstant;
import top.teek.integrate.system.feign.FormFeign;
import top.teek.integrate.system.helper.IntegrateUtil;
import top.teek.integrate.system.model.bo.ConfigPoolBO;
import top.teek.integrate.system.service.ServiceHandlerAdapt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component 
@RequiredArgsConstructor 
@Slf4j
public class FormServiceHandler implements ServiceHandlerAdapt {
    private final FormFeign formFeign;

    @Override
    public boolean match(ConfigPoolBO config) {
        return GlobalConstant.FORM.equalsIgnoreCase(config.getAppName());
    }

    @Override
    public Object auth(ConfigPoolBO config, Map<String, Object> authInfo) {
        return null;
    }

    @Override
    public Object getData(ConfigPoolBO config, Map<String, Object> deliverInfo, Object authInfo) {
        // 获取请求需要携带的参数
        Map<String, Object> reqParamMap = IntegrateUtil.getReqParamMap(config.getReqParams(), deliverInfo);
        // 判断请求类型：GET、POST 等
        String methodType = config.getMethod();
        String url = IntegrateUtil.getUrl(config.getUrl(), deliverInfo);
        log.info("请求的完整地址：{}，请求类型：Get", url);
        String response;
        try {
            if (HttpMethod.GET.name().equalsIgnoreCase(methodType)) {
                // 获取传参的参数，即 URL ? 后面的参数 
                response = formFeign.doGetData(new URI(url), reqParamMap);
            } else if (HttpMethod.POST.name().equalsIgnoreCase(methodType)) {
                response = formFeign.doPostData(new URI(url), reqParamMap);
            } else {
                throw new ServiceException("暂不支持除了 GET、POST 外的其他请求方式");
            }
        } catch (URISyntaxException e) {
            throw new ServiceException(url + " 存在问题，无法转换为 URI 类型");
        }
        return response;
    }

    @Override
    public Object syncDataRequest(ConfigPoolBO config, Map<String, Object> deliverInfo, Object data, Object authInfo) {
        String url = IntegrateUtil.getUrl(config.getUrl(), deliverInfo);
        String response;
        try {
            response = formFeign.doPostData(new URI(url), (Map) data);
        } catch (URISyntaxException e) {
            throw new ServiceException(url + " 存在问题，无法转换为 URI 类型");
        }
        Assert.nonNull(response, config.getAppName() + " 系统响应为空：" + response);
        log.info(config.getAppName() + " 返回结果：" + response);
        return response;
    }
}