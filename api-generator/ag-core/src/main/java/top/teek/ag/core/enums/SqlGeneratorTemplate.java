package top.teek.ag.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Teeker
 * @date 2024/7/21 00:07:29
 * @note
 */
@Getter
@AllArgsConstructor
public enum SqlGeneratorTemplate {
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    DDL,
    ;
    
}
