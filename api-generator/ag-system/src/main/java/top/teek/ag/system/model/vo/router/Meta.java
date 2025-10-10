package top.teek.ag.system.model.vo.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

/**
 * @author Teeker
 * @date 2024/4/27 23:55:44
 * @note
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Jackson 解析时候，去掉值为 null 的属性
public class Meta {
    /**
     * 显示在侧边栏、面包屑和标签栏的文字
     */
    private String title;

    /**
     * 菜单图标，该页面在左侧菜单、面包屑显示的图标
     */
    private String icon;

    /**
     * 可访问该页面的权限数组
     */
    private Set<String> roles;

    /**
     * 路由内的按钮权限
     */
    private Set<String> auths;

    /**
     * 是否允许点击面包屑，如果为 true，则该路由无法在面包屑中被点击，默认为 false
     */
    private Boolean notClickBread;

    /**
     * 是否不添加到面包屑，如果为 true，则该路由将不会出现在面包屑中，默认为 false
     */
    private Boolean hideInBread;

    /**
     * 是否不添加到菜单，如果为 true，则该路由不会显示在左侧菜单，默认为 false
     */
    private Boolean hideInMenu;

    /**
     * 是否总是渲染为菜单，如果为 false 且某一级路由下只有一个二级路由，则左侧菜单直接显示该二级路由，如果为 true，则总会让一级菜单作为下拉菜单，默认为 false，仅限父级路由使用
     */
    private Boolean alwaysShowRoot;

    /**
     * 是否缓存，如果为 true，该路由在切换标签后不会缓存，如果需要缓存，则「必须」设置页面组件 name 属性（class 名）和路由配置的 name 一致，默认为 false
     */
    private Boolean isKeepAlive;

    /**
     * 是否固定在 tabs nav，如果为 true，则该路由按照路由表顺序依次标签固定在标签栏，默认为 false
     */
    private Boolean isAffix;

    /**
     * 是否全屏，不渲染 Layout 布局，只渲染当前路由组件
     */
    private Boolean isFull;

    /**
     * Restful 路由搭配使用，当前路由为详情页时，需要高亮的菜单
     */
    private String activeMenu;

    /**
     * 关闭路由前的回调，如果设置该字段，则在关闭当前 tab 页时会去前端 @/router/before-close.js 里寻找该字段名「对应」的方法，作为关闭前的钩子函数，无默认值
     */
    private String beforeCloseName;

    /**
     * 路由在左侧菜单的排序，rank 值越高越靠后
     */
    private Integer rank;

    /**
     * IFrame 链接，填写后该路由将打开 IFrame 指定的链接
     */
    private String frameSrc;

    /**
     * IFrame 页是否开启首次加载动画（默认 true）
     */
    private String frameLoading;

    /**
     * IFrame 页是否开启缓（默认 false）
     */
    private String frameKeepAlive;

    /**
     * IFrame 页是否开新标签页打开，true 以新标签页打开，false 不打开（默认 false）
     */
    private String frameOpen;

    /**
     * 页面加载动画
     */
    private Transition transition;

    /**
     * 是否不添加到标签页，默认 false
     */
    private Boolean hideInTab;

    /**
     * 动态路由可打开的最大数量，默认不限制
     */
    private Integer dynamicLevel;

    /**
     * 是否开启 i18n
     */
    private Boolean useI18n;

    /**
     * 菜单的文字超出后，是否使用 el-toolTip 提示，仅针二级路由及以上生效
     */
    private Boolean useTooltip;

    /**
     * 携带参数
     */
    private Map<String, Object> params;

}
