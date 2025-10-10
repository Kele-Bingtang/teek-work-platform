package top.teek.ag.system.model.po;

import top.teek.ag.system.model.vo.ServiceColVO;
import top.teek.mp.annotation.FieldValueFill;
import top.teek.mp.annotation.ValueStrategy;
import top.teek.mp.base.BaseNoLogicDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-04-23 01:04:02
 * @note 服务列配置项
*/
@TableName(value = "t_service_col", autoResultMap = true)
@Data
@AutoMapper(target = ServiceColVO.class, reverseConvertGenerate = false)
public class ServiceCol extends BaseNoLogicDO {

    /**
     * 字段 ID
     */
    @TableField(fill = FieldFill.INSERT)
    @FieldValueFill(ValueStrategy.SNOWFLAKE)
    private String colId;

    /**
     * 表字段名称
     */
    private String tableCol;

    /**
     * 请求返回的 JSON 字段名称
     */
    private String jsonCol;

    /**
     * 报表字段名称
     */
    private String reportCol;

    /**
     * 字段类型
     */
    private String colType;

    /**
     * 字段类型长度
     */
    private Integer colLength;

    /**
     * 增删改时，是否作为 where 条件的主键，0 不作为，1 作为
     */
    private Integer isWhereKey;

    /**
     * 字段默认值
     */
    private String defaultValue;

    /**
     * 数据是否加密（0 不加密 1 加密）
     */
    private Integer dataEncrypt;

    /**
     * Select 时的条件筛选，如 where 字段 = xx、like %xx%。
     * 0 为不筛选
     * 1 为 =，精准匹配
     * 2 为 !=，不等于
     * 3 为 like %xx
     * 4 为 like xx%
     * 5 为 like %xx%
     * 6 为 lt，即小于
     * 7 为 gt，即大于
     * 8 为 le，即小于等于
     * 9 为 ge，即大于等于
     * 10 为 value range，即值范围查询
     * 11 为 in
     */
    private Integer queryFilter;

    /**
     * 排序，负数表示 desc，正数表示 asc
     */
    private Integer orderBy;

    /**
     * 是否允许插入（0 不允许 1 允许）
     */
    private Integer allowInsert;

    /**
     * 是否允许更新（0 不允许 1 允许）
     */
    private Integer allowUpdate;

    /**
     * 是否允许返回在请求里（0 不允许 1 允许）
     */
    private Integer allowRequest;

    /**
     * 是否允许出现在报表（0 不允许 1 允许）
     */
    private Integer allowShowInReport;

    /**
     * 是否允许出现在报表的增删改弹出框（0 不允许 1 允许）
     */
    private Integer allowShowInDetail;

    /**
     * 报表字段出现的顺序
     */
    private Integer displaySeq;

    /**
     * 报表字段显示的宽度，-1 为 auto，其他为准确的数值+px
     */
    private Integer reportColWidth;

    /**
     * 报表的增删改弹出框，该字段的输入框宽度，-1 为 auto，其他为准确的数值 + px
     */
    private Integer detailColSpan;

    /**
     * 报表显示的列对齐（1 为左对齐 2 为居中 3 为右对齐）
     */
    private Integer colAlign;

    /**
     * 下拉值配置
     * key 有 type、value
     * type 的 value 有 local、service、sql
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> dropdownConfig;

    /**
     * 服务 ID
     */
    private String serviceId;

    /**
     * 分类 ID
     */
    private String categoryId;
    
    /**
     * 项目 ID
     */
    private String projectId;
    
    /**
     * 团队 ID
     */
    private String teamId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || getClass() != o.getClass()) {
            return false;
        }
        ServiceCol serviceCol = (ServiceCol) o;
        return Objects.equals(this.tableCol, serviceCol.tableCol) && this.serviceId.equals(serviceCol.getServiceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCol, serviceId);
    }
}