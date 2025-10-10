package top.teek.file.system.service.impl;

import top.teek.core.exception.ServiceException;
import top.teek.file.system.helper.FileHelper;
import top.teek.file.system.model.dto.UploadFileDTO;
import top.teek.file.system.model.po.FileInfo;
import top.teek.file.system.model.vo.FileUploadSuccessVO;
import top.teek.file.system.properties.FileProperties;
import top.teek.file.system.service.FileInfoService;
import top.teek.file.system.service.FileUploadService;
import top.teek.utils.IdsUtil;
import top.teek.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/8/5 21:41:15
 * @note
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final FileInfoService fileInfoService;
    private final FileProperties fileProperties;

    @Override
    public List<FileUploadSuccessVO> uploadFiles(MultipartFile[] fileList, UploadFileDTO uploadFileDTO) {
        Integer expireTime = uploadFileDTO.getExpireTime();
        checkUploadFileList(fileList, expireTime);

        List<File> saveFileList = new ArrayList<>();
        List<FileUploadSuccessVO> fileInfoVOList = new ArrayList<>();
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            String originalFilename = file.getOriginalFilename();
            String fileType = FilenameUtils.getExtension(originalFilename);
            long fileSize = file.getSize();
            String fileKey = IdsUtil.simpleUUID();
            // 保存到指定路径下
            File saveFile = FileHelper.saveFile(file, uploadFileDTO.getAppId(), uploadFileDTO.getAppModule(), fileKey);
            saveFileList.add(saveFile);

            FileInfo fileInfo = new FileInfo();

            expireTime = Objects.nonNull(expireTime) ? expireTime : fileProperties.getExpireTime();
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(expireTime);
            fileInfo.setAppId(uploadFileDTO.getAppId())
                    .setFileKey(fileKey)
                    .setAppModule(uploadFileDTO.getAppModule())
                    .setFileSize(fileSize)
                    .setFileName(StringUtil.hasText(uploadFileDTO.getFileName()) ? uploadFileDTO.getFileName() + "." + fileType : originalFilename)
                    .setFilePath(saveFile.getAbsolutePath())
                    .setFileType(fileType)
                    .setExpireTime(localDateTime)
                    .setCreateBy(uploadFileDTO.getUploadUserName())
                    .setCreateById(uploadFileDTO.getUploadUserId())
                    .setUpdateBy(uploadFileDTO.getUploadUserName())
                    .setUpdateById(uploadFileDTO.getUploadUserId());

            fileInfoList.add(fileInfo);
            fileInfoVOList.add(new FileUploadSuccessVO().setFileKey(fileKey).setFileName(originalFilename).setExpireTime(localDateTime));
        }

        try {
            if (!fileInfoService.saveBatch(fileInfoList)) {
                // 保存失败，则删除文件
                FileHelper.removeFiles(saveFileList);
                throw new ServiceException("上传文件失败");
            }
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw new ServiceException("上传文件失败");
            }
            FileHelper.removeFiles(saveFileList);
        }

        return fileInfoVOList;
    }

    /**
     * 校验上传文件参数
     **/
    public void checkUploadFileList(MultipartFile[] fileList, Integer expireTime) {
        // 校验文件 
        if (Objects.isNull(fileList) || fileList.length == 0) {
            throw new ServiceException("文件为空，请先上传");
        }

        if (Objects.nonNull(expireTime) && expireTime < -1) {
            throw new ServiceException("参数 expireTime 必须大于当前时间");
        }

        long totalSize = 0L;
        for (MultipartFile multipartFile : fileList) {
            if (!multipartFile.isEmpty()) {
                String originalFileName = multipartFile.getOriginalFilename();
                if (StringUtil.hasText(originalFileName) && originalFileName.length() > fileProperties.getMaxFileNameSize()) {
                    throw new ServiceException("文件名称大小不能超过 " + fileProperties.getMaxFileNameSize() + " 字符");
                }
                if (multipartFile.getSize() == 0L) {
                    throw new ServiceException("文件大小不能为 0");
                }
                totalSize = totalSize + multipartFile.getSize();
            }
        }

        // 校验文件大小总和是否超过限制
        if (totalSize > fileProperties.getMaxFileSize()) {
            throw new ServiceException("文件总大小不能超过 " + fileProperties.getMaxFileSize() / 1024 + " M");
        }
    }

    @Override
    public List<FileUploadSuccessVO> uploadBase64(UploadFileDTO uploadFileDTO) {
        List<String> base64Image = uploadFileDTO.getBase64Images();
        Integer expireTime = uploadFileDTO.getExpireTime();
        String fileKey = IdsUtil.simpleUUID();

        List<File> saveFileList = new ArrayList<>();
        List<FileUploadSuccessVO> fileInfoVOList = new ArrayList<>();
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (String base64 : base64Image) {
            File file = FileHelper.saveFileByBase64(base64, uploadFileDTO.getAppId(), uploadFileDTO.getAppModule(), fileKey);

            saveFileList.add(file);
            FileInfo fileInfo = new FileInfo();

            expireTime = Objects.nonNull(expireTime) ? expireTime : fileProperties.getExpireTime();
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(expireTime);

            String fileType = "png";
            String originalFilename = fileKey + "." + fileType;
            fileInfo.setAppId(uploadFileDTO.getAppId())
                    .setFileKey(fileKey)
                    .setAppModule(uploadFileDTO.getAppModule())
                    .setFileSize(fileInfo.getFileSize())
                    .setFileName(StringUtil.hasText(uploadFileDTO.getFileName()) ? uploadFileDTO.getFileName() + "." + fileType : originalFilename)
                    .setFilePath(file.getAbsolutePath())
                    .setFileType("png")
                    .setExpireTime(localDateTime)
                    .setCreateBy(uploadFileDTO.getUploadUserName())
                    .setCreateById(uploadFileDTO.getUploadUserId())
                    .setUpdateBy(uploadFileDTO.getUploadUserName())
                    .setUpdateById(uploadFileDTO.getUploadUserId());

            fileInfoList.add(fileInfo);
            fileInfoVOList.add(new FileUploadSuccessVO().setFileKey(fileKey).setFileName(originalFilename).setExpireTime(localDateTime));
        }

        try {
            if (!fileInfoService.saveBatch(fileInfoList)) {
                // 保存失败，则删除文件
                FileHelper.removeFiles(saveFileList);
                throw new ServiceException("上传 Base64 图片失败");
            }
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw new ServiceException("上传 Base64 图片失败");
            }
            FileHelper.removeFiles(saveFileList);
        }

        return fileInfoVOList;
    }
}