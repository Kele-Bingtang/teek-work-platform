package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.ReportDTO;
import top.teek.ag.system.model.po.Report;
import top.teek.ag.system.model.vo.ReportDataVO;
import top.teek.ag.system.model.vo.ReportVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_report（报表表）」的数据库操作 Service
 */
public interface ReportService extends IService<Report> {
    /**
     * 根据条件查询报表信息
     *
     * @param serviceId 服务 ID
     * @return 报表信息
     */
    ReportVO getReportByServiceId(String serviceId);

    /**
     * 添加报表
     *
     * @param reportDTO 报表信息
     * @return 是否添加成功
     */
    boolean addReport(ReportDTO reportDTO);

    /**
     * 编辑报表
     *
     * @param reportDTO 报表信息
     * @return 是否编辑成功
     */
    boolean editReport(ReportDTO reportDTO);

    /**
     * 根据服务 ID 删除报表
     *
     * @param serviceId 服务 ID
     * @return 是否删除成功
     */
    boolean removeReport(String serviceId);

    /**
     * 根据分类 ID 删除报表
     *
     * @param categoryId 分类 ID
     * @return 是否删除成功
     */
    boolean removeReportByCategoryId(String categoryId);

    /**
     * 根据项目 ID 删除报表
     *
     * @param projectId 项目 ID
     * @return 是否删除成功
     */
    boolean removeReportByProjectIdId(String projectId);

    /**
     * 根据团队 ID 删除报表
     *
     * @param teamId 团队 ID
     * @return 是否删除成功
     */
    boolean removeReportByTeamId(String teamId);

    /**
     * 检查报表标题是否唯一
     *
     * @param reportDTO 报表信息
     * @return 报表标题是否唯一
     */
    boolean checkReportTitleUnique(ReportDTO reportDTO);

    /**
     * 获取报表配置信息
     *
     * @param serviceId 服务 ID
     * @return 报表配置信息
     */
    ReportDataVO listReportConfig(String serviceId);

}
