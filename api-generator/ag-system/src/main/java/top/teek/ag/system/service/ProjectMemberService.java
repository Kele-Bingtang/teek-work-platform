package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.ProjectMemberDTO;
import top.teek.ag.system.model.po.ProjectMember;
import top.teek.ag.system.model.vo.ProjectMemberVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_project_member（项目成员表）」的数据库操作 Service
 */
public interface ProjectMemberService extends IService<ProjectMember> {

    /**
     * 查询项目成员角色
     *
     * @param userId 用户 ID
     * @return 项目成员角色列表
     */
    List<ProjectMemberVO> listProjectRole(String teamId, String userId);

    /**
     * 获取我的项目角色
     *
     * @param projectId 项目 ID
     * @return 项目角色
     */
    String getMyProjectRole(String projectId);

    /**
     * 添加项目成员
     *
     * @param projectMemberDTO 项目成员信息
     * @return 是否成功
     */
    boolean addProjectMember(ProjectMemberDTO projectMemberDTO);

    /**
     * 批量添加项目成员
     *
     * @param projectMemberDTOList 项目成员信息列表
     * @return 是否成功
     */
    boolean addBatchProjectMember(List<ProjectMemberDTO> projectMemberDTOList);

    /**
     * 编辑项目成员
     *
     * @param projectMemberDTO 项目成员信息
     * @return 是否成功
     */
    boolean editProjectMember(ProjectMemberDTO projectMemberDTO);

    /**
     * 检查成员角色
     *
     * @param projectId 项目 ID
     * @param userId    用户 ID
     * @param ordinal   角色序号
     * @return 是否成功
     */
    boolean checkMemberRole(String userId, String projectId, List<Integer> ordinal);

    /**
     * 检查项目是否存在成员
     *
     * @param projectId 项目 ID
     * @param userId    用户 ID
     * @return 项目是否存在成员
     */
    boolean checkMemberExist(String userId, String projectId);

    /**
     * 根据项目 ID 移除所有成员
     *
     * @param projectId 项目 ID
     * @return 是否成功
     */
    boolean removeAllMember(String projectId);

    /**
     * 根据团队 ID 移除所有成员
     *
     * @param teamId 团队 ID
     * @return 是否成功
     */
    boolean removeAllMemberByTeamId(String teamId);


    /**
     * 检查成员唯一性
     *
     * @param projectMemberDTO 项目成员信息
     * @return 是否成功
     */
    boolean checkMemberUnique(ProjectMemberDTO projectMemberDTO);

}
