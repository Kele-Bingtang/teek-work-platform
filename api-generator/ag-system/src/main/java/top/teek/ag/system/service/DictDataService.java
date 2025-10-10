package top.teek.ag.system.service;

import cn.hutool.core.lang.tree.Tree;
import top.teek.ag.system.model.dto.DictDataDTO;
import top.teek.ag.system.model.po.DictData;
import top.teek.ag.system.model.po.DictType;
import top.teek.ag.system.model.vo.DictDataVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t__dict_data(字典数据表)】的数据库操作Service
 */
public interface DictDataService extends IService<DictData> {

    /**
     * 通过条件查询字典数据列表
     *
     * @param dictDataDTO 查询条件
     * @return 字典数据列表
     */
    List<DictDataVO> listAll(DictDataDTO dictDataDTO);

    /**
     * 通过条件查询字典数据列表
     *
     * @param dictDataDTO 查询条件
     * @param pageQuery      分页参数
     * @return 字典数据列表
     */
    TablePage<DictDataVO> listPage(DictDataDTO dictDataDTO, PageQuery pageQuery);

    /**
     * 新增字典数据
     *
     * @param dictDataDTO 字典数据
     * @return 是否成功
     */
    boolean addDictType(DictDataDTO dictDataDTO);

    /**
     * 修改字典数据
     *
     * @param dictDataDTO 字典数据
     * @return 是否成功
     */
    boolean editDictType(DictDataDTO dictDataDTO);

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
     * @param dictDataDTO 查询条件
     * @return 字典数据树形结构
     */
    List<Tree<String>> listDataTreeList(DictDataDTO dictDataDTO);

    /**
     * 通过条件查询字典数据树形表格
     *
     * @param dictDataDTO 查询条件
     * @return 字典数据树形表格
     */
    List<DictDataVO> listDataTreeTable(DictDataDTO dictDataDTO);

    /**
     * 校验字典数据是否唯一
     *
     * @param dictDataDTO 字典数据
     * @return 是否唯一
     */
    boolean checkDictDataValueUnique(DictDataDTO dictDataDTO);

    /**
     * 校验字典数据是否存在，并获取对应数据
     *
     * @param ids 主键列表
     * @return 是否存在
     */
    List<DictType> checkDictTypeExitDataAndGet(List<Long> ids);

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
    DictData listByDictValue(String dictValue, String dictCode);

    /**
     * 通过 DictLabel 获取字典数据
     *
     * @param dictLabel 展示值
     * @param dictCode  字典编码
     * @return 字典数据
     */
    DictData listByDictLabel(String dictLabel, String dictCode);
}
