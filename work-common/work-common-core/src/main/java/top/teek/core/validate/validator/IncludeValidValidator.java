package top.teek.core.validate.validator;


import top.teek.core.validate.annotation.IncludeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;


/**
 * @author Teeker
 * @date 2022/12/6 22:17
 * @note 如果 Controller 接受的参数包含 value，则校验成功
 */
public class IncludeValidValidator implements ConstraintValidator<IncludeValid, Integer> {
    private String[] values;

    @Override
    public void initialize(IncludeValid constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        if (Objects.isNull(value)) {
            return true;
        }
        for (String s : values) {
            if (s.equals(String.valueOf(value))) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
