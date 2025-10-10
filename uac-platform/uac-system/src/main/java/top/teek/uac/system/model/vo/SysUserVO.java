package top.teek.uac.system.model.vo;

import top.teek.excel.annotation.ExcelDictFormat;
import top.teek.excel.convert.ExcelDictConvert;
import top.teek.uac.system.export.NormalStatusHandler;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Teeker
 * @date 2023/12/4 19:35
 * @note
 */
@Data
public class SysUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty("序号")
    private Integer id;

    /**
     * 用户 ID
     */
    @ExcelProperty("用户 ID")
    private String userId;

    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String username;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String nickname;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 性别（0 保密 1 男 2 女）
     */
    @ExcelProperty("性别")
    private Integer sex;

    /**
     * 生日
     */
    @ExcelProperty("生日")
    private String birthday;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号码")
    private String phone;

    /**
     * 用户状态（0 离线 1 在线）
     */
    @ExcelProperty(value = "用户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readExp = "0:离线, 1:在线")
    private Integer userStatus;

    /**
     * 状态（0 禁用 1 启用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(handler = NormalStatusHandler.class)
    private Integer status;

    /**
     * 头像
     */
    @ExcelIgnore
    private String avatar;

    /**
     * 注册时间
     */
    @ExcelProperty("注册时间")
    private LocalDateTime registerTime;

    /**
     * 最后登录 IP
     */
    @ExcelProperty("最后登录 IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ExcelProperty("最后登录时间")
    private LocalDateTime loginTime;

    /**
     * 部门 ID
     */
    @ExcelProperty("部门 ID")
    private String deptId; 

    /**
     * 部门对象
     */
    @ExcelIgnore
    private SysDeptVO dept;

    /**
     * 是否禁用，给前端使用
     */
    @ExcelIgnore
    private Boolean disabled;
}
