package top.teek.integrate.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.util.Map;

/**
 * @author Teeker
 * @date 2024/10/28 00:40:54
 * @note
 */
@FeignClient(name = "formFeign", url = "http://www.teek.top")
public interface FormFeign {
    
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String doGetData(URI uri, @SpringQueryMap Map<String, ?> body);

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String doPostData(URI uri, Map<String, ?> body);
}