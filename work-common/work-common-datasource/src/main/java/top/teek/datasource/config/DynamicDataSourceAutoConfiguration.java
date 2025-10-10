package top.teek.datasource.config;

import top.teek.datasource.processor.LastParamDsProcessor;
import top.teek.datasource.properties.DataSourceProperties;
import top.teek.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023/12/26 20:59
 * @note
 */
@AutoConfiguration
@EnableConfigurationProperties(DataSourceProperties.class)
@AutoConfigureAfter({DefaultDataSourceCreator.class})
public class DynamicDataSourceAutoConfiguration {
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator, DataSourceProperties dataSourceProperties,
                                                               DynamicDataSourceProperties dynamicDataSourceProperties) {
        // 获取动态配置主数据源
        String primary = dynamicDataSourceProperties.getPrimary();
        Map<String, DataSourceProperty> datasource = dynamicDataSourceProperties.getDatasource();
        DataSourceProperty primaryDataSourceProperty = datasource.get(primary);
        if (Objects.nonNull(primaryDataSourceProperty)) {
            BeanUtils.copyProperties(primaryDataSourceProperty, dataSourceProperties);
            // 动态配置主数据源存在，则不需要重新创建主数据源
            return new DynamicDataSourceProvider(defaultDataSourceCreator, dataSourceProperties, false);
        }

        return new DynamicDataSourceProvider(defaultDataSourceCreator, dataSourceProperties, true);
    }

    @Bean
    public DsProcessor dsProcessor() {
        return new LastParamDsProcessor();
    }
}