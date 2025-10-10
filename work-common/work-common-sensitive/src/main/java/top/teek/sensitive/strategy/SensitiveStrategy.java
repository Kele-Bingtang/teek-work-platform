package top.teek.sensitive.strategy;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author Teeker
 * @date 2024/6/8 01:05:00
 * @note 脱敏策略，采用 hutool 的 DesensitizedUtil 工具类实现脱敏
 */
@AllArgsConstructor
@Getter
public enum SensitiveStrategy {

    /**
     * 默认脱敏
     */
    DEFAULT(value -> value),

    /**
     * 自定义规则脱敏
     */
    CUSTOMIZE_RULE(value -> value),

    /**
     * 中文名脱敏
     */
    CHINESE_NAME(DesensitizedUtil::chineseName),

    /**
     * 密码脱敏
     */
    PASSWORD(DesensitizedUtil::password),

    /**
     * 身份证脱敏
     */
    ID_CARD(s -> DesensitizedUtil.idCardNum(s, 3, 4)),

    /**
     * 手机号脱敏
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 地址脱敏
     */
    ADDRESS(s -> DesensitizedUtil.address(s, 8)),

    /**
     * 邮箱脱敏
     */
    EMAIL(DesensitizedUtil::email),

    /**
     * 银行卡
     */
    BANK_CARD(DesensitizedUtil::bankCard);

    private final Function<String, String> desensitize;
}
