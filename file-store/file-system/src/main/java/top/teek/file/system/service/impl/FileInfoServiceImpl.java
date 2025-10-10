package top.teek.file.system.service.impl;

import top.teek.file.system.helper.FileHelper;
import top.teek.file.system.mapper.FileInfoMapper;
import top.teek.file.system.model.dto.FileInfoDTO;
import top.teek.file.system.model.po.FileInfo;
import top.teek.file.system.model.vo.FileInfoVO;
import top.teek.file.system.service.FileInfoService;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024-08-06 21:08:58
 * @note 针对表「t_file_info（附件信息表）」的数据库操作 Service 实现
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

    @Override
    public List<FileInfoVO> listAppModule(String appId) {
        List<FileInfo> fileInfoList = baseMapper.selectList(Wrappers.<FileInfo>lambdaQuery()
                .select(FileInfo::getAppModule)
                .eq(FileInfo::getAppId, appId)
                .groupBy(FileInfo::getAppModule)
                .orderByAsc(FileInfo::getCreateTime));

        fileInfoList = fileInfoList.stream().map(fileInfo -> {
            if (StringUtil.hasEmpty(fileInfo.getAppModule())) {
                fileInfo.setAppModule("其他");
            }
            return fileInfo;
        }).toList();

        return MapstructUtil.convert(fileInfoList, FileInfoVO.class);
    }


    @Override
    public TablePage<FileInfoVO> listPage(FileInfoDTO fileInfoDTO, PageQuery pageQuery) {
        Page<FileInfo> fileInfoPage = baseMapper.selectPage(pageQuery.buildPage(), Wrappers.<FileInfo>lambdaQuery()
                .eq(StringUtil.hasText(fileInfoDTO.getAppId()), FileInfo::getAppId, fileInfoDTO.getAppId())
                .and("其他".equals(fileInfoDTO.getAppModule()), e -> e.eq(FileInfo::getAppModule, null).or().eq(FileInfo::getAppModule, ""))
                .like(StringUtil.hasText(fileInfoDTO.getAppModule()) && !"其他".equals(fileInfoDTO.getAppModule()), FileInfo::getAppModule, fileInfoDTO.getAppModule())
                .eq(StringUtil.hasText(fileInfoDTO.getFileName()), FileInfo::getFileName, fileInfoDTO.getFileName())
                .lt(Objects.nonNull(fileInfoDTO.getExpire()) && fileInfoDTO.getExpire(), FileInfo::getExpireTime, LocalDateTime.now())
        );

        return TablePage.build(fileInfoPage, FileInfoVO.class);
    }

    @Override
    public boolean addFile(FileInfoDTO fileInfoDTO) {
        FileInfo fileInfo = MapstructUtil.convert(fileInfoDTO, FileInfo.class);
        return baseMapper.insert(fileInfo) > 0;
    }

    @Override
    public boolean editFile(FileInfoDTO fileInfoDTO) {
        FileInfo fileInfo = MapstructUtil.convert(fileInfoDTO, FileInfo.class);
        return baseMapper.updateById(fileInfo) > 0;
    }

    @Override
    public boolean removeFile(String fileKey) {
        FileInfo fileInfo = baseMapper.selectOne(Wrappers.<FileInfo>lambdaQuery()
                .eq(FileInfo::getFileKey, fileKey));

        File file = new File(fileInfo.getFilePath());
        FileHelper.removeFile(file);

        return baseMapper.delete(Wrappers.<FileInfo>lambdaQuery()
                .eq(FileInfo::getFileKey, fileKey)) > 0;
    }

    @Override
    public boolean removeBatchFile(List<String> fileKeyList) {
        List<FileInfo> fileInfoList = baseMapper.selectList(Wrappers.<FileInfo>lambdaQuery()
                .in(FileInfo::getFileKey, fileKeyList));


        List<File> fileList = fileInfoList.stream().map(fileInfo -> new File(fileInfo.getFilePath())).toList();
        FileHelper.removeFiles(fileList);

        List<Long> idList = fileInfoList.stream().map(FileInfo::getId).toList();
        return baseMapper.deleteBatchIds(idList) > 0;
    }
}





