package top.teek.core.config;

import top.teek.core.date.DatePatternPlus;
import top.teek.core.serializer.JacksonNumberSerializer;
import top.teek.utils.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Teeker
 * @date 2023/6/30 23:03
 * @note
 */
@Slf4j
@AutoConfiguration
public class JacksonAutoConfiguration {
    @Bean
    public static BeanPostProcessor objectMapperBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ObjectMapper)) {
                    return bean;
                }
                ObjectMapper objectMapper = (ObjectMapper) bean;
                SimpleModule simpleModule = new SimpleModule();
                /*
                 * 1. 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
                 * 2. 新增 LocalDateTime 序列化、反序列化规则
                 */
                simpleModule
                        .addSerializer(Long.class, JacksonNumberSerializer.INSTANCE)
                        .addSerializer(Long.TYPE, JacksonNumberSerializer.INSTANCE)
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePatternPlus.NORM_DATETIME_FORMATTER)) // yyyy-MM-dd HH:mm:ss
                        .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))// yyyy-MM-dd
                        .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME))// HH:mm:ss
                        .addSerializer(Instant.class, InstantSerializer.INSTANCE)// Instant 类型序列化
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePatternPlus.NORM_DATETIME_FORMATTER))// yyyy-MM-dd HH:mm:ss
                        .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))// yyyy-MM-dd
                        .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME))// HH:mm:ss
                        .addDeserializer(Instant.class, InstantDeserializer.INSTANT);// Instant 反序列化

                objectMapper.registerModules(simpleModule);

                JacksonUtil.init(objectMapper);
                log.info("初始化 jackson 自动配置");
                return bean;
            }
        };
    }
}
