package top.teek.notice.system.service.impl;

import top.teek.core.exception.ServerException;
import top.teek.core.exception.ServiceException;
import top.teek.helper.SpringHelper;
import top.teek.notice.core.enums.NoticeStatusType;
import top.teek.notice.core.enums.NoticeType;
import top.teek.notice.system.helper.MailHelper;
import top.teek.notice.system.model.dto.NoticeInfoDTO;
import top.teek.notice.system.model.po.AppInfo;
import top.teek.notice.system.model.po.NoticeInfo;
import top.teek.notice.system.model.po.NoticeMailConfig;
import top.teek.notice.system.model.vo.NoticeInfoVO;
import top.teek.notice.system.service.AppInfoService;
import top.teek.notice.system.service.MailService;
import top.teek.notice.system.service.NoticeInfoService;
import top.teek.notice.system.service.NoticeMailConfigService;
import top.teek.utils.IdsUtil;
import top.teek.utils.MapstructUtil;
import top.teek.utils.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Teeker
 * @date 2024/8/18 23:45:23
 * @note
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final NoticeMailConfigService mailConfigService;
    private final AppInfoService appInfoService;
    private final NoticeInfoService noticeInfoService;

    @Override
    public NoticeInfoVO sendMail(NoticeInfoDTO noticeInfoDTO) {
        // 校验内容
        validateMail(noticeInfoDTO);
        // 获取邮箱配置
        NoticeMailConfig noticeMailConfig = mailConfigService.getOne(Wrappers.<NoticeMailConfig>lambdaQuery().eq(NoticeMailConfig::getAppId, noticeInfoDTO.getAppId()));
        AppInfo appInfo = appInfoService.getOne(Wrappers.<AppInfo>lambdaQuery().eq(AppInfo::getAppId, noticeInfoDTO.getAppId()));

        String noticeId = storageNotice(noticeInfoDTO);

        if (Objects.isNull(noticeMailConfig)) {
            // 如果没有配置邮箱且使用默认邮箱
            if (appInfo.getDefaultSender() == 1) {
                sendMail(noticeId, noticeInfoDTO, null, noticeMailConfig);
                return new NoticeInfoVO().setNoticeId(noticeId);
            } else {
                throw new ServiceException("AppId 没有绑定邮箱");
            }
        }

        JavaMailSenderImpl sender = MailHelper.createSender(noticeMailConfig);
        if (Objects.isNull(sender)) {
            throw new ServiceException("邮件配置 ID 为空，不创建邮件发送器");
        }

        if (Objects.isNull(noticeInfoDTO.getShowTip())) {
            noticeInfoDTO.setShowTip(noticeMailConfig.getShowTips() == 1);
        }
        sendMail(noticeId, noticeInfoDTO, sender, noticeMailConfig);

        return new NoticeInfoVO().setNoticeId(noticeId);
    }

    private void sendMail(String noticeId, NoticeInfoDTO noticeInfoDTO, JavaMailSenderImpl sender, NoticeMailConfig noticeMailConfig) {
        String username = SpringHelper.getEnvProperty("spring.mail.username");
        if (Objects.nonNull(sender)) {
            username = sender.getUsername();
        }

        if (Objects.isNull(noticeInfoDTO.getFromName())) {
            noticeInfoDTO.setFromName(noticeMailConfig.getNickname());
        }

        noticeInfoService.update(Wrappers.<NoticeInfo>lambdaUpdate()
                .set(NoticeInfo::getFrom, username)
                .set(StringUtil.hasText(noticeInfoDTO.getFromName()), NoticeInfo::getFromName, noticeInfoDTO.getFromName())
                .eq(NoticeInfo::getNoticeId, noticeId));
        try {
            if (Objects.isNull(sender)) {
                MailHelper.send(noticeInfoDTO);
            } else {
                MailHelper.send(sender, sender.getUsername(), noticeInfoDTO);
            }
            updateStatus(noticeId, NoticeStatusType.COMPLETED);
        } catch (Exception e) {
            updateStatus(noticeId, NoticeStatusType.FAILED);
            throw new ServerException("发送邮件失败，邮件 ID：" + noticeId);
        }
    }

    @Override
    public void validateMail(NoticeInfoDTO noticeInfoDTO) {

    }

    @Override
    public String storageNotice(NoticeInfoDTO noticeInfoDTO) {
        NoticeInfo noticeInfo = MapstructUtil.convert(noticeInfoDTO, NoticeInfo.class);
        String simpleUUID = IdsUtil.simpleUUID();
        noticeInfo.setNoticeId(simpleUUID);
        noticeInfo.setMessageType(NoticeType.EM.name());

        noticeInfoService.save(noticeInfo);
        return simpleUUID;
    }

    @Override
    public void updateStatus(String noticeId, NoticeStatusType noticeStatusType) {
        boolean update = noticeInfoService.update(Wrappers.<NoticeInfo>lambdaUpdate()
                .set(NoticeInfo::getStatus, noticeStatusType.getStatus())
                .eq(NoticeInfo::getNoticeId, noticeId)
        );

        if (!update) {
            log.error("更新状态失败，消息 ID：{}", noticeStatusType);
        }
    }

    @Override
    public void deleteNotice(String noticeId) {
        noticeInfoService.update(Wrappers.<NoticeInfo>lambdaUpdate()
                .set(NoticeInfo::getIsDeleted, 1)
                .eq(NoticeInfo::getNoticeId, noticeId)
        );
    }
}
