package top.teek.ag.system.service;

import top.teek.ag.system.model.dto.TeamMemberDTO;
import top.teek.ag.system.model.dto.TeamMemberWithProjectRoleDTO;
import top.teek.ag.system.model.po.TeamMember;
import top.teek.ag.system.model.vo.TeamMemberVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-06-22 00:15:48
 * @note 针对表「t_team_member（团队成员表）」的数据库操作 Service
 */
public interface TeamMemberService extends IService<TeamMember> {

    /**
     * 查询团队成员
     *
     * @param teamMemberDTO 团队成员信息
     * @return 团队成员列表
     */
    List<TeamMemberVO> listAll(TeamMemberDTO teamMemberDTO);

    /**
     * 查询团队成员（分页）
     *
     * @param teamMemberDTO 团队成员信息
     * @param pageQuery     分页信息
     * @return 团队成员列表
     */
    TablePage<TeamMemberVO> listPage(TeamMemberDTO teamMemberDTO, PageQuery pageQuery);

    /**
     * 添加团队成员
     *
     * @param teamMemberDTOList 团队成员信息
     * @param inviteUserId 执行邀请的用户 ID
     * @return 是否添加成功
     */
    boolean addTeamMembers(List<TeamMemberDTO> teamMemberDTOList, String inviteUserId);

    /**
     * 修改团队成员的角色
     *
     * @param teamId  团队 ID
     * @param userId  用户 ID
     * @param ordinal 团队角色级别
     * @return 是否修改成功
     */
    boolean editTeamMemberRole(String teamId, String userId, int ordinal);

    /**
     * 退出团队
     *
     * @param teamId 团队 ID
     * @return 是否退出成功
     */
    boolean leaveTeam(String teamId);

    /**
     * 移除团队所有成员
     *
     * @param teamId 团队 ID
     * @return 是否移除成功
     */
    boolean removeAllTeamMember(String teamId);

    /**
     * 检查成员是否存在
     *
     * @param teamId 团队 ID
     * @param userId 用户 ID
     * @return 是否存在
     */
    boolean checkMemberExist(String teamId, String userId);

    /**
     * 检查成员的团队角色
     *
     * @param teamId  团队 ID
     * @param userId  用户 ID
     * @param ordinal 团队角色级别
     * @return 是否为团队负责人
     */
    boolean checkMemberRole(String teamId, String userId, List<Integer> ordinal);

    /**
     * 修改团队成员角色和团队下项目成员角色
     *
     * @param teamMemberWithProjectRoleDTO 团队成员和团队下项目成员角色信息
     * @return 是否修改成功
     */
    Boolean editTeamMemberWithProjectRole(TeamMemberWithProjectRoleDTO teamMemberWithProjectRoleDTO);

}
