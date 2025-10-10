package top.teek.uac.system.service.impl;

import top.teek.cache.helper.CacheHelper;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.uac.core.constant.CacheNameConstant;
import top.teek.uac.system.mapper.SysDictTypeMapper;
import top.teek.uac.system.model.dto.SysDictTypeDTO;
import top.teek.uac.system.model.po.SysDictType;
import top.teek.uac.system.model.vo.SysDictTypeVO;
import top.teek.uac.system.service.SysDictDataService;
import top.teek.uac.system.service.SysDictTypeService;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2023-11-11 23:40:21
 * @note 针对表【t_sys_dict_type(字典类型表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    private final SysDictDataService sysDictDataService;

    @Override
    public List<SysDictTypeVO> listAll(SysDictTypeDTO sysDictTypeDTO) {
        LambdaQueryWrapper<SysDictType> wrapper = buildQueryWrapper(sysDictTypeDTO);
        List<SysDictType> sysDictTypeList = baseMapper.selectList(wrapper);

        return MapstructUtil.convert(sysDictTypeList, SysDictTypeVO.class);
    }

    @Override
    public TablePage<SysDictTypeVO> listPage(SysDictTypeDTO sysDictTypeDTO, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDictType> wrapper = buildQueryWrapper(sysDictTypeDTO);
        Page<SysDictType> sysDictTypePage = baseMapper.selectPage(pageQuery.buildPage(), wrapper);

        return TablePage.build(sysDictTypePage, SysDictTypeVO.class);
    }

    private LambdaQueryWrapper<SysDictType> buildQueryWrapper(SysDictTypeDTO sysDictTypeDTO) {
        return Wrappers.<SysDictType>lambdaQuery()
                .eq(StringUtil.hasText(sysDictTypeDTO.getDictName()), SysDictType::getDictName, sysDictTypeDTO.getDictName())
                .eq(StringUtil.hasText(sysDictTypeDTO.getDictCode()), SysDictType::getDictCode, sysDictTypeDTO.getDictCode())
                .eq(StringUtil.hasText(sysDictTypeDTO.getAppId()), SysDictType::getAppId, sysDictTypeDTO.getAppId())
                .orderByAsc(SysDictType::getDictId);
    }

    @Override
    public boolean addDictType(SysDictTypeDTO sysDictTypeDTO) {
        SysDictType sysDictType = MapstructUtil.convert(sysDictTypeDTO, SysDictType.class);
        return baseMapper.insert(sysDictType) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editDictType(SysDictTypeDTO sysDictTypeDTO) {
        SysDictType newSysDictType = MapstructUtil.convert(sysDictTypeDTO, SysDictType.class);
        // 同步更新 dictData 的 dictCode
        SysDictType oldDictType = baseMapper.selectById(sysDictTypeDTO.getId());
        sysDictDataService.updateDictCode(oldDictType.getDictCode(), newSysDictType.getDictCode());
        return baseMapper.updateById(newSysDictType) > 0;
    }

    @Override
    public boolean removeBatch(List<Long> ids) {
        List<SysDictType> sysDictTypeList = baseMapper.selectBatchIds(ids);

        boolean result = baseMapper.deleteBatchIds(ids) > 0;

        for (SysDictType sysDictType : sysDictTypeList) {
            // 删除存储的数据
            CacheHelper.evict(CacheNameConstant.SYS_DICT, sysDictType.getDictCode());
        }

        return result;
    }

    @Override
    public boolean checkAppExitDictType(List<String> appIds) {
        return baseMapper.exists(Wrappers.<SysDictType>lambdaQuery()
                .in(SysDictType::getAppId, appIds));
    }

    @Override
    public boolean checkDictCodeUnique(SysDictTypeDTO sysDictTypeDTO) {
        return baseMapper.exists(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getDictCode, sysDictTypeDTO.getDictCode())
                .ne(Objects.nonNull(sysDictTypeDTO.getId()), SysDictType::getId, sysDictTypeDTO.getId()));
    }
}




