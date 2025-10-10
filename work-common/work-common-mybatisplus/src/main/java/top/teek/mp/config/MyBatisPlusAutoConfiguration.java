package top.teek.mp.config;

import cn.hutool.core.net.NetUtil;
import top.teek.core.factory.YmlPropertySourceFactory;
import top.teek.mp.handler.MybatisPlusMetaObjectHandler;
import top.teek.mp.revolver.SqlFilterArgumentResolver;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Teeker
 * @date 2023/7/4 23:04
 * @note
 */
@EnableTransactionManagement(proxyTargetClass = true)
@AutoConfiguration
@PropertySource(value = "classpath:mybatis-plus.yml", factory = YmlPropertySourceFactory.class) // @PropertySource 官方默认只支持 properties、XML，YML 需要手动注册自定义类 YmlPropertySourceFactory
@MapperScan("${mybatis-plus.scanPackage}")
public class MyBatisPlusAutoConfiguration implements WebMvcConfigurer {

    /**
     * SQL 过滤器避免 SQL 注入
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SqlFilterArgumentResolver());
    }

    /**
     * 分页插件，对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 自定义分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花 ID 重复
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }

    /**
     * 查询拦截器
     */
    // @Bean
    // public QueryInterceptor queryInterceptor() {
    //     return new QueryInterceptor();
    // }

}