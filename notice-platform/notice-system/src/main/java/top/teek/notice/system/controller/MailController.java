package top.teek.notice.system.controller;

import top.teek.core.http.HttpResult;
import top.teek.core.http.Response;
import top.teek.notice.system.aspect.annotation.AppAuthorize;
import top.teek.notice.system.model.dto.NoticeInfoDTO;
import top.teek.notice.system.model.vo.NoticeInfoVO;
import top.teek.notice.system.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Teeker
 * @date 2024/8/18 23:44:28
 * @note
 */
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping
    @Operation(summary = "发送邮件", description = "发送邮件（不支持附件）")
    @AppAuthorize("#noticeInfoDTO.getAppId()")
    public Response<NoticeInfoVO> sendMail(NoticeInfoDTO noticeInfoDTO) {
        NoticeInfoVO noticeVO = mailService.sendMail(noticeInfoDTO);
        return HttpResult.ok(noticeVO);
    }

    @PostMapping
    @Operation(summary = "发送邮件", description = "发送邮件（支持附件）")
    @AppAuthorize("#noticeInfoDTO.getAppId()")
    public Response<NoticeInfoVO> sendMailWithFile(NoticeInfoDTO noticeInfoDTO) {
        NoticeInfoVO noticeVO = mailService.sendMail(noticeInfoDTO);
        return HttpResult.ok(noticeVO);
    }

}
