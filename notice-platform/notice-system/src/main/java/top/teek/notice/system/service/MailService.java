package top.teek.notice.system.service;

import top.teek.notice.core.enums.NoticeStatusType;
import top.teek.notice.system.model.dto.NoticeInfoDTO;
import top.teek.notice.system.model.vo.NoticeInfoVO;

/**
 * @author Teeker
 * @date 2024/8/18 23:45:00
 * @note
 */
public interface MailService {
    /**
     * 发送邮件
     * @param noticeInfoDTO 邮件信息
     * @return 消息发送成功的消息
     */
    NoticeInfoVO sendMail(NoticeInfoDTO noticeInfoDTO);

    /**
     * 验证邮件信息
     * @param noticeInfoDTO 邮件信息
     */
    void validateMail(NoticeInfoDTO noticeInfoDTO);

    /**
     * 存储消息
     * @param noticeInfoDTO 邮件信息
     * @return 存储成功的消息ID
     */
    String storageNotice(NoticeInfoDTO noticeInfoDTO);

    /**
     * 更新消息状态
     * @param noticeId 消息ID
     * @param noticeStatusType 状态
     */
    void updateStatus(String noticeId, NoticeStatusType noticeStatusType);

    /**
     * 删除消息
     * @param noticeId 消息ID
     */
    void deleteNotice(String noticeId);
}
