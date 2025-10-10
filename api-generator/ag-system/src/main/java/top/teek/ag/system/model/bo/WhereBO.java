package top.teek.ag.system.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/7/12 22:26:24
 * @note
 */
@Data
@Accessors(chain = true)
public class WhereBO {
    private String key;
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || getClass() != o.getClass()) {
            return false;
        }
        WhereBO whereBO = (WhereBO) o;
        return Objects.equals(this.key, whereBO.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
