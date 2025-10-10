package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysPostDTO;
import top.teek.uac.system.model.po.SysPost;
import top.teek.uac.system.model.vo.SysPostVO;
import top.teek.uac.system.model.vo.extra.UserSelectPostVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_post(岗位表)】的数据库操作Service
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * 通过条件查询岗位列表
     *
     * @param sysPostDTO 岗位列表查询参数
     * @return 岗位列表
     */
    List<SysPostVO> listAll(SysPostDTO sysPostDTO);

    /**
     * 通过条件查询岗位列表
     *
     * @param sysPostDTO 岗位列表查询参数
     * @param pageQuery  分页参数
     * @return 岗位列表
     */
    TablePage<SysPostVO> listPage(SysPostDTO sysPostDTO, PageQuery pageQuery);

    /**
     * 校验岗位名称是否唯一
     *
     * @param sysPostDTO 岗位列表查询参数
     * @return 岗位列表
     */
    boolean checkPostNameUnique(SysPostDTO sysPostDTO);

    /**
     * 校验岗位编码是否唯一
     *
     * @param sysPostDTO 岗位列表查询参数
     * @return 岗位列表
     */
    boolean checkPostCodeUnique(SysPostDTO sysPostDTO);

    /**
     * 校验岗位存在用户
     *
     * @param sysPostDTO 查询参数
     * @return 是否存在用户
     */
    boolean checkPostExitUser(SysPostDTO sysPostDTO);

    /**
     * 新增岗位
     *
     * @param sysPostDTO 岗位列表查询参数
     * @return 是否成功
     */
    boolean addPost(SysPostDTO sysPostDTO);

    /**
     * 更新岗位
     *
     * @param sysPostDTO 岗位列表查询参数
     * @return 是否成功
     */
    boolean editPost(SysPostDTO sysPostDTO);

    /**
     * 批量删除岗位
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    boolean removeBatch(List<Long> ids);

    /**
     * 获取岗位列表和已选择的岗位列表
     *
     * @return 岗位列表和已选择的岗位列表
     */
    UserSelectPostVo userSelectPostList(String userId);
}
