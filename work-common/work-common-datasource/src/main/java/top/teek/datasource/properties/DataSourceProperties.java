package top.teek.datasource.properties;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Teeker
 * @date 2023/12/26 21:06
 * @note
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties extends DataSourceProperty {
    /**
     * 存储数据源的表，自动生成 queryDsSql
     */
    private String dsName;
    /**
     * 查询数据源的 sql，优先级高于 dsName
     */
    private String selectSql;
}