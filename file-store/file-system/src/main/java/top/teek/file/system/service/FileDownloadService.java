package top.teek.file.system.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Teeker
 * @date 2024/8/5 21:41:32
 * @note
 */
public interface FileDownloadService {
    /**
     * 根据 fileKey 下载文件
     *
     * @param fileKey 文件唯一标识
     */
    void downloadFileByFileKey(String appId, String fileKey, boolean isBrowse, HttpServletResponse response);

    /**
     * 根据 fileKey 获取图片的 base64
     *
     * @param fileKey 文件唯一标识
     */
    String getImageBase64(String appId, String fileKey);
}
