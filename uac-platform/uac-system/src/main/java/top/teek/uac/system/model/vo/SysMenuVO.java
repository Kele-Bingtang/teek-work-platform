package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelClassConvert;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;
import top.teek.uac.system.model.vo.router.Meta;
import top.teek.utils.TreeBuildUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023-12-12 00:23:08
 * @note 菜单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuVO extends TreeBuildUtil.TreeBO<SysMenuVO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty("序号")
    private Long id;

    /**
     * 菜单 ID
     */
    @ExcelProperty("菜单 ID")
    private String menuId;

    /**
     * 父级菜单 ID
     */
    @ExcelProperty("父级菜单 ID")
    private String parentId;

    /**
     * 菜单编码
     */
    @ExcelProperty("菜单编码")
    private String menuCode;

    /**
     * 菜单名
     */
    @ExcelProperty("菜单名")
    private String menuName;

    /**
     * 菜单地址前缀
     */
    @ExcelProperty("菜单地址前缀")
    private String pathPrefix;

    /**
     * 菜单地址
     */
    @ExcelProperty("菜单地址")
    private String path;

    /**
     * 图标
     */
    @ExcelProperty("图标")
    private String icon;

    /**
     * 显示顺序
     */
    @ExcelProperty("显示顺序")
    private Integer orderNum;

    /**
     * 权限标识
     */
    @ExcelProperty("权限标识")
    private String permission;

    /**
     * 菜单类型（C目录 M菜单 F按钮）
     */
    @ExcelProperty(value = "菜单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "C:目录, M:菜单, F:按钮")
    private String menuType;

    /**
     * 组件路径
     */
    @ExcelProperty("组件路径")
    private String component;

    /**
     * 菜单前端额外配置
     */
    @ExcelProperty(value = "菜单前端额外配置", converter = ExcelClassConvert.class)
    private Meta meta;

    /**
     * 菜单介绍
     */
    @ExcelProperty("菜单介绍")
    private String intro;

    /**
     * 应用 ID
     */
    @ExcelProperty("应用 ID")
    private String appId;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


}