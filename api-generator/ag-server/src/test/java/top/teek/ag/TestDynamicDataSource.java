package top.teek.ag;

import top.teek.datasource.helper.DataSourceHelper;
import top.teek.datasource.meta.Column;
import top.teek.datasource.meta.Schema;
import top.teek.datasource.meta.Table;
import top.teek.datasource.constant.DataSourceConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Teeker
 * @date 2024/6/30 19:51:55
 * @note
 */
@SpringBootTest
public class TestDynamicDataSource {

    @Test
    void getCatalogs() {
        List<Schema> catalogs = DataSourceHelper.getCatalogs(DataSourceConstant.DS_MASTER);
        catalogs.forEach(System.out::println);
    }

    @Test
    void getSchemas() {
        List<Schema> schemaList = DataSourceHelper.getSchemas(DataSourceConstant.DS_MASTER, "work_ag");
        schemaList.forEach(System.out::println);
    }

    @Test
    void getTables() {
        List<Table> tableList = DataSourceHelper.getTables(DataSourceConstant.DS_MASTER, "work_ag", "work_ag");
        tableList.forEach(System.out::println);
    }
    @Test
    void getColumns() {
        List<Column> tableList = DataSourceHelper.getColumns(DataSourceConstant.DS_MASTER, "work_ag", "work_ag", "t_category");
        tableList.forEach(System.out::println);
    }
}
