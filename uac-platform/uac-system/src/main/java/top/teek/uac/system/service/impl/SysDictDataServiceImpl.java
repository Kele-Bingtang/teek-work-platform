package top.teek.uac.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import top.teek.cache.helper.CacheHelper;
import top.teek.core.constants.ColumnConstant;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.CacheNameConstant;
import top.teek.uac.system.mapper.SysDictDataMapper;
import top.teek.uac.system.model.dto.SysDictDataDTO;
import top.teek.uac.system.model.po.SysDictData;
import top.teek.uac.system.model.po.SysDictType;
import top.teek.uac.system.model.vo.SysDictDataVO;
import top.teek.uac.system.service.SysDictDataService;
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
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Cacheable(cacheNames = CacheNameConstant.SYS_DICT, key = "#sysDictDataDTO.dictCode")
    @Override
    public List<SysDictDataVO> listAll(SysDictDataDTO sysDictDataDTO) {
        LambdaQueryWrapper<SysDictData> wrapper = buildQueryWrapper(sysDictDataDTO);
        List<SysDictData> sysDictData = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysDictData, SysDictDataVO.class);
    }

    @Override
    public TablePage<SysDictDataVO> listPage(SysDictDataDTO sysDictDataDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDictData> wrapper = buildQueryWrapper(sysDictDataDTO);
        Page<SysDictData> sysDictDataPage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysDictDataPage, SysDictDataVO.class);
    }

    private LambdaQueryWrapper<SysDictData> buildQueryWrapper(SysDictDataDTO sysDictDataDTO) {
        return Wrappers.<SysDictData>lambdaQuery()
                .eq(Objects.nonNull(sysDictDataDTO.getDictSort()), SysDictData::getDictSort, sysDictDataDTO.getDictSort())
                .eq(StringUtil.hasText(sysDictDataDTO.getDictLabel()), SysDictData::getDictLabel, sysDictDataDTO.getDictLabel())
                .eq(StringUtil.hasText(sysDictDataDTO.getAppId()), SysDictData::getAppId, sysDictDataDTO.getAppId())
                .eq(StringUtil.hasText(sysDictDataDTO.getDictCode()), SysDictData::getDictCode, sysDictDataDTO.getDictCode())
                .orderByAsc(SysDictData::getDictSort);
    }

    @Override
    @CacheEvict(cacheNames = CacheNameConstant.SYS_DICT, key = "#sysDictDataDTO.dictCode")
    public boolean addDictData(SysDictDataDTO sysDictDataDTO) {
        SysDictData sysDictData = MapstructUtil.convert(sysDictDataDTO, SysDictData.class);

        if (StringUtil.hasText(sysDictDataDTO.getParentId())) {
            return baseMapper.insert(sysDictData) > 0;
        }

        sysDictDataDTO.setParentId(ColumnConstant.PARENT_ID);
        return baseMapper.insert(sysDictData) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNameConstant.SYS_DICT, key = "#sysDictDataDTO.dictCode")
    public boolean editDictData(SysDictDataDTO sysDictDataDTO) {
        SysDictData sysDictData = MapstructUtil.convert(sysDictDataDTO, SysDictData.class);
        return baseMapper.updateById(sysDictData) > 0;
    }

    @Override
    public boolean updateDictCode(String oldDictCode, String newDictCode) {
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictCode(newDictCode);
        return baseMapper.update(sysDictData, Wrappers.<SysDictData>lambdaUpdate()
                .eq(SysDictData::getDictCode, oldDictCode)) > 1;
    }

    @Override
    public boolean removeBatch(List<Long> ids) {
        SysDictData dictData = baseMapper.selectById(ids.get(0));

        boolean result = baseMapper.deleteByIds(ids) > 0;

        CacheHelper.evict(CacheNameConstant.SYS_DICT, dictData.getDictCode());
        return result;
    }

    @Override
    public List<Tree<String>> listDataTreeList(SysDictDataDTO sysDictDataDTO) {
        LambdaQueryWrapper<SysDictData> wrapper = buildQueryWrapper(sysDictDataDTO);
        List<SysDictData> sysDictData = baseMapper.selectList(wrapper);

        if (CollUtil.isEmpty(sysDictData)) {
            return Collections.emptyList();
        }

        return TreeBuildUtil.build(sysDictData, ColumnConstant.PARENT_ID, TreeNodeConfig.DEFAULT_CONFIG.setIdKey("value").setNameKey("label"), (treeNode, tree) ->
                tree.setId(treeNode.getDataId())
                        .setParentId(treeNode.getParentId())
                        .setName(treeNode.getDictLabel())
                        .setWeight(treeNode.getDictSort()));
    }

    @Override
    public List<SysDictDataVO> listDataTreeTable(SysDictDataDTO sysDictDataDTO) {
        LambdaQueryWrapper<SysDictData> wrapper = buildQueryWrapper(sysDictDataDTO);
        List<SysDictData> sysDictData = baseMapper.selectList(wrapper);
        List<SysDictDataVO> sysDictDataVOList = MapstructUtil.convert(sysDictData, SysDictDataVO.class);

        return TreeBuildUtil.build(sysDictDataVOList, "0", SysDictDataVO::getDataId);
    }

    @Override
    public boolean checkDictDataValueUnique(SysDictDataDTO sysDictDataDTO) {
        return baseMapper.exists(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictValue, sysDictDataDTO.getDictValue())
                .eq(SysDictData::getDictCode, sysDictDataDTO.getDictCode())
                .eq(StringUtil.hasText(sysDictDataDTO.getParentId()), SysDictData::getParentId, sysDictDataDTO.getParentId())
                .ne(StringUtil.hasText(sysDictDataDTO.getDataId()), SysDictData::getDataId, sysDictDataDTO.getDataId()));
    }

    @Override
    public List<SysDictType> checkDictTypeExitDataAndGet(List<Long> ids) {
        QueryWrapper<SysDictData> wrapper = Wrappers.query();
        wrapper.eq("tsdt.is_deleted", 0)
                .in("tsdt.id", ids);

        return baseMapper.checkExitDictData(wrapper);
    }

    @Override
    public boolean existDictData(String dictCode) {
        return baseMapper.exists(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictCode, dictCode));
    }

    @Override
    public SysDictData listByDictValue(String dictValue, String dictCode) {
        return baseMapper.selectOne(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictValue, dictValue)
                .eq(SysDictData::getDictCode, dictCode)
        );
    }

    @Override
    public SysDictData listByDictLabel(String dictLabel, String dictCode) {
        return baseMapper.selectOne(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictLabel, dictLabel)
                .eq(SysDictData::getDictCode, dictCode)
        );
    }
}




