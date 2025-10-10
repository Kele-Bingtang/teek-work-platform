package top.teek.core.validate.validator;

import top.teek.core.exception.ServiceException;
import top.teek.core.http.ResponseStatusEnum;
import top.teek.core.validate.annotation.ValidFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/5/3 16:52
 * @note 指定多个属性不为空，可设置全满足或任意满足即可通过验证
 */
public class ValidFieldsValidator implements ConstraintValidator<ValidFields, Object> {

    private String[] fields;

    private boolean any;

    @Override
    public void initialize(ValidFields constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
        this.any = constraintAnnotation.any();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        for (String field : fields) {
            try {
                Field declaredField = value.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                Object fieldValue = declaredField.get(value);

                if (fieldValue instanceof String) { // 新增类型判断
                    if ("".equals(fieldValue)) { // 对空字符串进行检查
                        if (!any) {
                            return false;
                        }
                    } else {
                        if (any) {
                            return true;
                        }
                    }
                } else if (Objects.nonNull(fieldValue)) {
                    if (any) {
                        return true;
                    }
                } else {
                    if (!any) {
                        return false;
                    }
                }
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException ignored) {
                throw new ServiceException(ResponseStatusEnum.FAIL);
            }
        }
        return any;
    }
}