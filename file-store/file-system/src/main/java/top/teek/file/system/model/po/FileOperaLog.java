package top.teek.file.system.model.po;

import top.teek.file.system.model.vo.FileOperaLogVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 附件信息日志表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_file_opera_log")
@Data
@Accessors(chain = true)
@AutoMapper(target = FileOperaLogVO.class, reverseConvertGenerate = false)
public class FileOperaLog extends BaseDO {
    /**
     * 日志 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String operaId;

    /**
     * 应用系统标识
     */
    private String appId;

    /**
     * 附件唯一标识
     */
    private String fileKey;

    /**
     * 方法名
     */
    private String method;

    /**
     * 操作类型（1 上传 2 下载）
     */
    private Integer operaType;

    /**
     * 操作用户
     */
    private String operaUser;

    /**
     * 主机地址
     */
    private String operaIp;

    /**
     * 操作地点
     */
    private String operaLocation;

    /**
     * 请求地址
     */
    private String operaUrl;

    /**
     * 消耗时间
     */
    private Long costTime;

    /**
     * 消耗时间
     */
    private String errorMsg;

    /**
     * 备注
     */
    private String remark;

    @Serial
    private static final long serialVersionUID = 1L;
}