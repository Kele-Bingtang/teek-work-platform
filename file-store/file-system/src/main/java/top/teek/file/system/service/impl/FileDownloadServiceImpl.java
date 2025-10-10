package top.teek.file.system.service.impl;

import top.teek.core.exception.ServiceException;
import top.teek.file.system.helper.FileHelper;
import top.teek.file.system.model.po.FileInfo;
import top.teek.file.system.service.FileDownloadService;
import top.teek.file.system.service.FileInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/8/5 21:41:39
 * @note
 */
@Service
@RequiredArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {
    private final FileInfoService fileInfoService;

    @Override
    public void downloadFileByFileKey(String appId, String fileKey, boolean isBrowse, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoService.getOne(Wrappers.<FileInfo>lambdaQuery()
                .eq(FileInfo::getFileKey, fileKey)
                .eq(FileInfo::getAppId, appId));

        if (Objects.isNull(fileInfo)) {
            throw new ServiceException("文件不存在");
        }

        // 检查过期时间
        LocalDateTime expireTime = fileInfo.getExpireTime();
        if (Objects.nonNull(expireTime) && expireTime.isBefore(LocalDateTime.now())) {
            throw new ServiceException("文件已过期");
        }

        // 文件流写入响应里
        FileHelper.getFileForResponse(fileInfo.getFilePath(), fileInfo.getFileName(), response, isBrowse);
    }

    @Override
    public String getImageBase64(String appId, String fileKey) {
        FileInfo fileInfo = fileInfoService.getOne(Wrappers.<FileInfo>lambdaQuery()
                .eq(FileInfo::getFileKey, fileKey)
                .eq(FileInfo::getAppId, appId));
        
        if(!fileInfo.getFileType().equals("png")) {
            throw new ServiceException("不是图片格式，无法转为 base64");
        }
        
        // 提供文件路径，获取文件并转为 base64 
        return FileHelper.getBase64ByFilePath(fileInfo.getFilePath());
    }
}
