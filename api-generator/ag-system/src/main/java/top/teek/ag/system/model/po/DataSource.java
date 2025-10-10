package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.DataSourceVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 数据源配置表
 */
@TableName(value = "t_data_source")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataSourceVO.class, reverseConvertGenerate = false)
public class DataSource extends BaseDO {
    /**
     * 数据源 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String dataSourceId;

    /**
     * 数据源名称
     */
    private String dataSourceName;

    /**
     * 数据源类型
     */
    private String dataSourceType;

    /**
     * 数据源链接地址
     */
    private String jdbcUrl;

    /**
     * 数据源服务器地址
     */
    private String host;

    /**
     * 数据源端口
     */
    private String port;

    /**
     * 数据源驱动类
     */
    private String driverClassName;

    /**
     * 数据源账号
     */
    private String username;

    /**
     * 数据源密码
     */
    private String password;

    /**
     * 数据源描述
     */
    private String description;

    /**
     * 密码加解密密钥
     */
    private String secretKey;

    /**
     * 团队 ID
     */
    private String teamId;

    @Serial
    private static final long serialVersionUID = 1L;
}