package top.teek.mp.base;

import top.teek.utils.MapstructUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Teeker
 * @date 2024/4/1 21:31
 * @note
 */
@Data
@NoArgsConstructor
public class TablePage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 一页数量
     */
    private long pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> list;


    public static <T> TablePage<T> instance() {
        return new TablePage<>();
    }

    public TablePage(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public static <T> TablePage<T> build(IPage<T> page) {
        TablePage<T> tablePage = TablePage.instance();

        tablePage.setPageNum(page.getCurrent());
        tablePage.setPageSize(page.getSize());
        tablePage.setTotal(page.getTotal());
        tablePage.setList(page.getRecords());

        return tablePage;
    }

    public static <T> TablePage<T> build(List<T> list) {
        TablePage<T> tablePage = TablePage.instance();

        tablePage.setTotal(list.size());
        tablePage.setList(list);

        return tablePage;
    }

    public static <S, T> TablePage<T> build(IPage<S> page, Class<T> clazz) {
        TablePage<T> tablePage = TablePage.instance();

        tablePage.setPageNum(page.getCurrent());
        tablePage.setPageSize(page.getSize());
        tablePage.setTotal(page.getTotal());

        List<T> list = MapstructUtil.convert(page.getRecords(), clazz);

        tablePage.setList(list);

        return tablePage;
    }

}
