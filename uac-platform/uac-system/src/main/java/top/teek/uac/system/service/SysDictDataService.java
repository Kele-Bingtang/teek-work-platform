package top.teek.uac.system.service;

import cn.hutool.core.lang.tree.Tree;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.system.model.dto.SysDictDataDTO;
import top.teek.uac.system.model.po.SysDictData;
import top.teek.uac.system.model.po.SysDictType;
import top.teek.uac.system.model.vo.SysDictDataVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_dict_data(字典数据表)】的数据库操作Service
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 通过条件查询字典数据列表
     *
     * @param sysDictDataDTO 查询条件
     * @return 字典数据列表
     */
    List<SysDictDataVO> listAll(SysDictDataDTO sysDictDataDTO);

    /**
     * 通过条件查询字典数据列表
     *
     * @param sysDictDataDTO 查询条件
     * @param pageQuery      分页参数
     * @return 字典数据列表
     */
    TablePage<SysDictDataVO> listPage(SysDictDataDTO sysDictDataDTO, PageQuery pageQuery);

    /**
     * 新增字典数据
     *
     * @param sysDictDataDTO 字典数据
     * @return 是否成功
     */
    boolean addDictData(SysDictDataDTO sysDictDataDTO);

    /**
     * 修改字典数据
     *
     * @param sysDictDataDTO 字典数据
     * @return 是否成功
     */
    boolean editDictData(SysDictDataDTO sysDictDataDTO);

    /**
     * 更新字典编码
     *
     * @param oldDictCode  旧字典编码
     * @param newDictCodeS 新字典编码
     * @return 是否成功
     */
    boolean updateDictCode(String oldDictCode, String newDictCodeS);

    /**
     * 批量删除字典数据
     *
     * @param ids 主键列表
     * @return 是否成功
     */
    boolean removeBatch(List<Long> ids);

    /**
     * 通过条件查询字典数据树形结构
     *
     * @param sysDictDataDTO 查询条件
     * @return 字典数据树形结构
     */
    List<Tree<String>> listDataTreeList(SysDictDataDTO sysDictDataDTO);

    /**
     * 通过条件查询字典数据树形表格
     *
     * @param sysDictDataDTO 查询条件
     * @return 字典数据树形表格
     */
    List<SysDictDataVO> listDataTreeTable(SysDictDataDTO sysDictDataDTO);

    /**
     * 校验字典数据是否唯一
     *
     * @param sysDictDataDTO 字典数据
     * @return 是否唯一
     */
    boolean checkDictDataValueUnique(SysDictDataDTO sysDictDataDTO);

    /**
     * 校验字典数据是否存在，并获取对应数据
     *
     * @param ids 主键列表
     * @return 是否存在
     */
    List<SysDictType> checkDictTypeExitDataAndGet(List<Long> ids);

    /**
     * 校验字典类型是否存在字典数据
     *
     * @param dictCode 字典类型 Code
     * @return 是否存在
     */
    boolean existDictData(String dictCode);

    /**
     * 通过 DictValue 获取字典数据
     *
     * @param dictValue 存储值
     * @param dictCode  字典编码
     * @return 字典数据
     */
    SysDictData listByDictValue(String dictValue, String dictCode);

    /**
     * 通过 DictLabel 获取字典数据
     *
     * @param dictLabel 展示值
     * @param dictCode  字典编码
     * @return 字典数据
     */
    SysDictData listByDictLabel(String dictLabel, String dictCode);
}
