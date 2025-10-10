package top.teek.uac.system.export;

import top.teek.excel.dict.ExcelDictHandler;
import top.teek.uac.system.model.po.SysDictData;
import top.teek.uac.system.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Teeker
 * @date 2024/6/13 21:25:42
 * @note
 */
@Component
@RequiredArgsConstructor
public class NormalStatusHandler implements ExcelDictHandler {
    
    private final SysDictDataService sysDictDataService;
    @Override
    public String getLabel(String value) {
        SysDictData sysDictData = sysDictDataService.listByDictValue(value, "sys_normal_status");
        return sysDictData.getDictLabel();
    }

    @Override
    public String getValue(String label) {
        SysDictData sysDictData = sysDictDataService.listByDictLabel(label, "sys_normal_status");
        return sysDictData.getDictValue();
    }
}
