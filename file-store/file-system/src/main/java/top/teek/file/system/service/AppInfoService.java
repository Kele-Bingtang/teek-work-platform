package top.teek.file.system.service;

import top.teek.file.system.model.dto.AppInfoDTO;
import top.teek.file.system.model.po.AppInfo;
import top.teek.file.system.model.vo.AppInfoVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Teeker
 * @date 2024-08-06 21:08:58
 * @note 针对表「t_app_info（app 配置表）」的数据库操作 Service
 */
public interface AppInfoService extends IService<AppInfo> {

    /**
     * 应用列表查询
     *
     * @param appInfoDTO 查询信息
     * @param pageQuery 分页信息
     * @return 应用列表
     */
    TablePage<AppInfoVO> listPage(AppInfoDTO appInfoDTO, PageQuery pageQuery);

    /**
     * 新增应用
     *
     * @param appInfoDTO 应用信息
     * @return 新增结果
     */
    boolean addApp(AppInfoDTO appInfoDTO);

    /**
     * 编辑应用
     *
     * @param appInfoDTO 应用信息
     * @return 编辑结果
     */
    boolean editApp(AppInfoDTO appInfoDTO);

    /**
     * 删除应用
     *
     * @param appId 应用ID
     * @return 删除结果
     */
    boolean removeApp(String appId);

}
