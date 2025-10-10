package top.teek.utils;

import top.teek.helper.SpringHelper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;


/**
 * @author Teeker
 * @date 2023/11/15 0:09
 * @note
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {

    private static final Validator VALIDATOR = SpringHelper.getBean(Validator.class);

    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(object, groups);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }
}
