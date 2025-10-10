package top.teek.ag.system.model.vo.router;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/4/27 23:30:02
 * @note
 */
@Data
@Accessors(chain = true)
public class RouterVO {
    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 其他元素
     */
    private Meta meta;

    /**
     * 子路由
     */
    private List<RouterVO> children;
}
