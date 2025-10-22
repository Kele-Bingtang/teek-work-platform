package top.teek.ag.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import top.teek.ag.core.constant.CacheNameConstant;
import top.teek.ag.system.mapper.DictDataMapper;
import top.teek.ag.system.model.dto.DictDataDTO;
import top.teek.ag.system.model.po.DictData;
import top.teek.ag.system.model.po.DictType;
import top.teek.ag.system.model.vo.DictDataVO;
import top.teek.ag.system.service.DictDataService;
import top.teek.cache.helper.CacheHelper;
import top.teek.core.constants.ColumnConstant;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import top.teek.utils.TreeBuildUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_dict_data(字典数据表)】的数据库操作Service实现
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Cacheable(cacheNames = CacheNameConstant.SYS_DICT, key = "#dictDataDTO.dictCode")
    @Override
    public List<DictDataVO> listAll(DictDataDTO dictDataDTO) {
        LambdaQueryWrapper<DictData> wrapper = buildQueryWrapper(dictDataDTO);
        List<DictData> dictData = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(dictData, DictDataVO.class);
    }

    @Override
    public TablePage<DictDataVO> listPage(DictDataDTO dictDataDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<DictData> wrapper = buildQueryWrapper(dictDataDTO);
        Page<DictData> dictDataPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(dictDataPage, DictDataVO.class);
    }

    private LambdaQueryWrapper<DictData> buildQueryWrapper(DictDataDTO dictDataDTO) {
        return Wrappers.<DictData>lambdaQuery()
                .eq(StringUtil.hasText(dictDataDTO.getDictLabel()), DictData::getDictLabel, dictDataDTO.getDictLabel())
                .eq(StringUtil.hasText(dictDataDTO.getDictCode()), DictData::getDictCode, dictDataDTO.getDictCode())
                .eq(Objects.nonNull(dictDataDTO.getDictSort()), DictData::getDictSort, dictDataDTO.getDictSort())
                .orderByAsc(DictData::getDictSort);
    }

    @Override
    @CacheEvict(cacheNames = CacheNameConstant.SYS_DICT, key = "#dictDataDTO.dictCode")
    public boolean addDictType(DictDataDTO dictDataDTO) {
        DictData dictData = MapstructUtil.convert(dictDataDTO, DictData.class);

        if (StringUtil.hasText(dictDataDTO.getParentId())) {
            return baseMapper.insert(dictData) > 0;
        }

        dictDataDTO.setParentId(ColumnConstant.PARENT_ID);
        return baseMapper.insert(dictData) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNameConstant.SYS_DICT, key = "#dictDataDTO.dictCode")
    public boolean editDictType(DictDataDTO dictDataDTO) {
        DictData dictData = MapstructUtil.convert(dictDataDTO, DictData.class);
        return baseMapper.updateById(dictData) > 0;
    }

    @Override
    public boolean updateDictCode(String oldDictCode, String newDictCode) {
        DictData dictData = new DictData();
        dictData.setDictCode(newDictCode);
        return baseMapper.update(dictData, Wrappers.<DictData>lambdaUpdate()
                .eq(DictData::getDictCode, oldDictCode)) > 1;
    }

    @Override
    public boolean removeBatch(List<Long> ids) {
        DictData dictData = baseMapper.selectById(ids.get(0));

        boolean result = baseMapper.deleteBatchIds(ids) > 0;

        CacheHelper.evict(CacheNameConstant.SYS_DICT, dictData.getDictCode());
        return result;
    }

    @Override
    public List<Tree<String>> listDataTreeList(DictDataDTO dictDataDTO) {
        LambdaQueryWrapper<DictData> wrapper = buildQueryWrapper(dictDataDTO);
        List<DictData> dictData = baseMapper.selectList(wrapper);

        if (CollUtil.isEmpty(dictData)) {
            return Collections.emptyList();
        }

        return TreeBuildUtil.build(dictData, ColumnConstant.PARENT_ID, TreeNodeConfig.DEFAULT_CONFIG.setIdKey("value").setNameKey("label"), (treeNode, tree) ->
                tree.setId(treeNode.getDataId())
                        .setParentId(treeNode.getParentId())
                        .setName(treeNode.getDictLabel())
                        .setWeight(treeNode.getDictSort()));
    }

    @Override
    public List<DictDataVO> listDataTreeTable(DictDataDTO dictDataDTO) {
        LambdaQueryWrapper<DictData> wrapper = buildQueryWrapper(dictDataDTO);
        List<DictData> dictData = baseMapper.selectList(wrapper);
        List<DictDataVO> dictDataVOList = MapstructUtil.convert(dictData, DictDataVO.class);

        return TreeBuildUtil.build(dictDataVOList, "0", DictDataVO::getDataId);
    }

    @Override
    public boolean checkDictDataValueUnique(DictDataDTO dictDataDTO) {
        return baseMapper.exists(Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getDictValue, dictDataDTO.getDictValue())
                .eq(DictData::getDictCode, dictDataDTO.getDictCode())
                .eq(StringUtil.hasText(dictDataDTO.getParentId()), DictData::getParentId, dictDataDTO.getParentId())
                .ne(Objects.nonNull(dictDataDTO.getId()), DictData::getId, dictDataDTO.getId()));
    }

    @Override
    public List<DictType> checkDictTypeExitDataAndGet(List<Long> ids) {
        QueryWrapper<DictData> wrapper = Wrappers.query();
        wrapper.eq("tsdt.is_deleted", 0)
                .in("tsdt.id", ids);

        return baseMapper.checkExitDictData(wrapper);
    }

    @Override
    public boolean existDictData(String dictCode) {
        return baseMapper.exists(Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getDictCode, dictCode));
    }

    @Override
    public DictData listByDictValue(String dictValue, String dictCode) {
        return baseMapper.selectOne(Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getDictValue, dictValue)
                .eq(DictData::getDictCode, dictCode)
        );
    }

    @Override
    public DictData listByDictLabel(String dictLabel, String dictCode) {
        return baseMapper.selectOne(Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getDictLabel, dictLabel)
                .eq(DictData::getDictCode, dictCode)
        );
    }
}




