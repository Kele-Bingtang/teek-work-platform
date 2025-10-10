package top.teek.uac.demo.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.sensitive.annotation.Sensitive;
import top.teek.sensitive.strategy.SensitiveStrategy;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/6/8 02:19:55
 * @note
 */
@RestController
@RequestMapping("/demo/sensitive")
public class DemoSensitiveController {


    @RequestMapping("/test")
    public Response<TestSensitive> test() {
        TestSensitive testSensitive = new TestSensitive();
        testSensitive.setMessage("端午节放假啦");
        testSensitive.setRuleMessage("123456789");
        testSensitive.setIdCard("210397198608215431");
        testSensitive.setPhone("15777815847");
        testSensitive.setAddress("深圳市龙岗区某某小区1327室");
        testSensitive.setEmail("2456019588@qq.com");
        testSensitive.setBankCard("6226456952351452853");

        return HttpResult.ok(testSensitive);
    }

    @Data
    static class TestSensitive {

        /**
         * 不脱敏消息
         */
        @Sensitive(strategy = SensitiveStrategy.DEFAULT)
        private String message;

        /**
         * 自定义规则脱敏
         */
        @Sensitive(strategy = SensitiveStrategy.CUSTOMIZE_RULE, startLen = 3, endLen = 6)
        private String ruleMessage;

        /**
         * 身份证
         */
        @Sensitive(strategy = SensitiveStrategy.ID_CARD)
        private String idCard;

        /**
         * 电话
         */
        @Sensitive(strategy = SensitiveStrategy.PHONE, roleCode = "admin")
        private String phone;

        /**
         * 地址
         */
        @Sensitive(strategy = SensitiveStrategy.ADDRESS, perms = "system:user:query")
        private String address;

        /**
         * 邮箱
         */
        @Sensitive(strategy = SensitiveStrategy.EMAIL, roleCode = "admin", perms = "system:user:query")
        private String email;

        /**
         * 银行卡
         */
        @Sensitive(strategy = SensitiveStrategy.BANK_CARD, roleCode = "visitor", perms = "system:user:query")
        private String bankCard;

    }
}
