package top.teek.mp.base;

import cn.hutool.core.util.StrUtil;
import top.teek.core.exception.ServiceException;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author Teeker
 * @date 2023/11/26 23:10
 * @note 分页实体类，接收前端传来的数据
 * 支持：
 * { pageNum: 1, pageSize: 20, orderRule: [ {column: id, type: asc}, {column: createTime, type: desc} ] }
 * 即 order by id asc,create_time asc limit 1 20
 */
@Data
public class PageQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页数
     */
    private Integer pageNum;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 排序规则
     * 1、column：排序的字段，驼峰形式
     * 2、type：排序的类型：asc 升序，desc 降序
     */
    private transient List<Map<String, String>> orderRuleList;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    public <T> Page<T> buildPage() {
        int pageNum = Optional.ofNullable(getPageNum()).orElse(DEFAULT_PAGE_NUM);
        int pageSize = Optional.ofNullable(getPageSize()).orElse(DEFAULT_PAGE_SIZE);

        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItemList = buildOrderItem();
        if (!orderItemList.isEmpty()) {
            page.addOrder(orderItemList);
        }
        return page;
    }

    /**
     * 构建排序规则
     */
    private List<OrderItem> buildOrderItem() {
        if (Objects.isNull(orderRuleList)) {
            return Collections.emptyList();
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (Map<String, String> orderRule : orderRuleList) {
            String column = StrUtil.toUnderlineCase(orderRule.get("column"));
            String type = orderRule.get("type");

            if (!StringUtil.hasText(column, type)) {
                break;
            }

            if ("asc".equals(type)) {
                orderItemList.add(OrderItem.asc(column));
            } else if ("desc".equals(type)) {
                orderItemList.add(OrderItem.desc(column));
            } else {
                throw new ServiceException("排序参数有误");
            }
        }
        return orderItemList;
    }

    @Setter
    @Getter
    static class OrderRule {
        /**
         * 排序的字段，驼峰形式
         */
        private String column;
        /**
         * 排序的类型：asc 升序，desc 降序
         */
        private String type;
    }
}
