package top.teek.uac.demo.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.encrypt.annotation.ApiEncrypt;
import top.teek.uac.demo.model.DemoEncryptPO;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.web.bind.annotation.*;

/**
 * @author Teeker
 * @date 2024/6/9 01:30:35
 * @note
 */
@RestController
@RequestMapping("/demo/encrypt")
public class DemoEncryptController {

    @PostMapping("/test/save")
    public Response<String> testSaveEncrypt() {
        DemoEncryptPO demoEncryptPo = new DemoEncryptPO();

        demoEncryptPo.setTestKey("123456789");
        demoEncryptPo.setValue("测试 Encrypt");
        Db.save(demoEncryptPo);

        return HttpResult.ok("testEncrypt");
    }

    @GetMapping("/test/query/{id}")
    public Response<DemoEncryptPO> testQueryEncrypt(@PathVariable Long id) {
        DemoEncryptPO demoEncryptPo = Db.getById(id, DemoEncryptPO.class);

        return HttpResult.ok(demoEncryptPo);
    }

    @GetMapping("/test/api/{id}")
    @ApiEncrypt
    public Response<DemoEncryptPO> testApiEncrypt(@PathVariable Long id) {
        DemoEncryptPO demoEncryptPo = Db.getById(id, DemoEncryptPO.class);

        return HttpResult.ok(demoEncryptPo);
    }
}
