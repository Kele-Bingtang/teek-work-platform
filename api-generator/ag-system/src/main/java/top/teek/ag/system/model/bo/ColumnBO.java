package top.teek.ag.system.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/7/12 22:27:58
 * @note
 */
@Data
@Accessors(chain = true)
public class ColumnBO {
    private String column;
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || getClass() != o.getClass()) {
            return false;
        }
        ColumnBO columnBO = (ColumnBO) o;
        return Objects.equals(this.column, columnBO.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(column);
    }
}
