package top.teek.file.system.service;

import top.teek.file.system.model.dto.FileInfoDTO;
import top.teek.file.system.model.po.FileInfo;
import top.teek.file.system.model.vo.FileInfoVO;
import top.teek.mp.base.PageQuery;
import top.teek.mp.base.TablePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Teeker
 * @date 2024-08-06 21:08:58
 * @note 针对表「t_file_info（附件信息表）」的数据库操作 Service
 */
public interface FileInfoService extends IService<FileInfo> {

    /**
     * 获取 AppModule 列表
     *
     * @param appId App ID
     * @return AppModule 列表
     */
    List<FileInfoVO> listAppModule(String appId);
    
    /**
     * 获取文件信息列表（分页）
     *
     * @param fileInfoDTO 文件信息条件
     * @param pageQuery  分页参数
     * @return AppModule 文件信息列表（分页）
     */
    TablePage<FileInfoVO> listPage(FileInfoDTO fileInfoDTO, PageQuery pageQuery);

    /**
     * 新增文件信息
     *
     * @param fileInfoDTO 文件信息条件
     * @return 是否添加成功
     */
    boolean addFile(FileInfoDTO fileInfoDTO);

    /**
     * 修改文件信息
     *
     * @param fileInfoDTO 文件信息条件
     * @return 是否修改成功
     */
    boolean editFile(FileInfoDTO fileInfoDTO);

    /**
     * 删除文件信息
     *
     * @param fileKey File Key
     * @return 是否删除成功
     */
    boolean removeFile(String fileKey);

    /**
     * 批量删除文件信息
     *
     * @param fileKeyList File Key 列表
     * @return 是否删除成功
     */
    boolean removeBatchFile(List<String> fileKeyList);
}
