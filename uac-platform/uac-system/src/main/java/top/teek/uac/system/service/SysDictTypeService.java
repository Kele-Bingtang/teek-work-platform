package top.teek.uac.system.service;

import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysDictTypeDTO;
import top.teek.uac.system.model.po.SysDictType;
import top.teek.uac.system.model.vo.SysDictTypeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_dict_type(字典类型表)】的数据库操作Service
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 通过条件查询字典类型列表
     *
     * @param sysDictTypeDTO 查询条件
     * @return 字典类型列表
     */
    List<SysDictTypeVO> listAll(SysDictTypeDTO sysDictTypeDTO);

    /**
     * 通过条件查询字典类型列表
     *
     * @param sysDictTypeDTO 查询条件
     * @param pageQuery      分页参数
     * @return 字典类型列表
     */
    TablePage<SysDictTypeVO> listPage(SysDictTypeDTO sysDictTypeDTO, PageQuery pageQuery);

    /**
     * 新增字典类型
     *
     * @param sysDictTypeDTO 新增字典类型信息
     * @return 是否新增成功
     */
    boolean addDictType(SysDictTypeDTO sysDictTypeDTO);

    /**
     * 修改字典类型
     *
     * @param sysDictTypeDTO 修改字典类型信息
     * @return 是否修改成功
     */
    boolean editDictType(SysDictTypeDTO sysDictTypeDTO);

    /**
     * 批量删除字典类型
     *
     * @param ids 主键列表
     * @return 是否删除成功
     */
    boolean removeBatch(List<Long> ids);

    /**
     * 校验 App 下是否存在字典类型
     * @param appIds 应用 Id 列表
     * @return 是否存在
     */
    boolean checkAppExitDictType(List<String> appIds);

    /**
     * 校验字典类型编码是否唯一
     *
     * @param sysDictTypeDTO 字典类型信息
     * @return 是否唯一
     */
    boolean checkDictCodeUnique(SysDictTypeDTO sysDictTypeDTO);
}
