package top.teek.mp.handler;

import cn.hutool.core.util.StrUtil;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.constant.MyBatisDefaultConstants;
import top.teek.security.domain.LoginUser;
import top.teek.security.domain.SecurityUser;
import top.teek.security.utils.SecurityUtils;
import top.teek.security.utils.LoginHelper;
import top.teek.utils.IdsUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/7/4 23:06
 * @note MybatisPlus 自动填充配置
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("mybatis plus start insert fill ....");

        LocalDateTime now = LocalDateTime.now();

        LoginUser user = getUser();

        if (Objects.nonNull(user)) {
            fillValIfNullByName("createBy", user.getNickname(), metaObject, false);
            fillValIfNullByName("updateBy", user.getNickname(), metaObject, false);
            fillValIfNullByName("createById", user.getUsername(), metaObject, false);
            fillValIfNullByName("updateById", user.getUsername(), metaObject, false);
        }

        fillValIfNullByName("createTime", now, metaObject, false);
        fillValIfNullByName("updateTime", now, metaObject, false);
        fillValIfNullByName("isDeleted", MyBatisDefaultConstants.DEFAULT_DELETED, metaObject, false);
        fillValIfNullByName("status", MyBatisDefaultConstants.DEFAULT_STATUS, metaObject, false);

        fillValFieldTypes(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("mybatis plus start update fill ....");

        LoginUser user = getUser();

        if (Objects.nonNull(user)) {
            fillValIfNullByName("updateBy", user.getNickname(), metaObject, true);
            fillValIfNullByName("updateById", user.getUsername(), metaObject, true);
        }
        
        fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
    }

    public static void fillValFieldTypes(MetaObject metaObject) {
        // 获取绑定 FieldValueFill 注解的属性
        List<Field> fieldList = FieldUtils.getFieldsListWithAnnotation(metaObject.getOriginalObject().getClass(), FieldValueFill.class);
        if (!fieldList.isEmpty()) {
            for (Field field : fieldList) {
                FieldValueFill fieldValueFill = field.getAnnotation(FieldValueFill.class);
                if (fieldValueFill.value().equals(ValueStrategy.UUID)) {
                    fillValIfNullByName(field.getName(), IdsUtil.simpleUUID(), metaObject, false);
                } else if (fieldValueFill.value().equals(ValueStrategy.SNOWFLAKE)) {
                    fillValIfNullByName(field.getName(), String.valueOf(IdWorker.getId()), metaObject, false);
                }
            }
        }
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     *
     * @param fieldName  属性名
     * @param fieldVal   属性值
     * @param metaObject MetaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (StringUtil.hasText(setValueStr) && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    /**
     * 获取 spring security 当前的用户名
     *
     * @return 当前用户名
     */
    private LoginUser getUser() {
        LoginUser userInfo = LoginHelper.getLoginUser();
        Object principal = SecurityUtils.getPrincipal();

        if (Objects.isNull(userInfo) && principal instanceof SecurityUser) {
            return MapstructUtil.convert(principal, LoginUser.class);
        }

        return userInfo;
    }
}