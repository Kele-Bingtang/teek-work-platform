package top.teek.notice.system.helper;

import top.teek.core.exception.ServerException;
import top.teek.helper.SpringHelper;
import top.teek.notice.core.constant.CacheNameConstant;
import top.teek.notice.system.model.dto.NoticeInfoDTO;
import top.teek.notice.system.model.po.NoticeMailConfig;
import top.teek.utils.ListUtil;
import top.teek.utils.StringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.time.Duration;
import java.util.*;

/**
 * @author Teeker
 * @date 2024/8/18 21:56:42
 * @note
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class MailHelper {
    /**
     * 多邮箱账户缓存，缓存时间为 4h
     */
    private static final Cache<Object, Object> CAFFEINE = Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(4)).initialCapacity(30).maximumSize(1000L).build();
    /**
     * 模板引擎
     */
    private static final SpringTemplateEngine templateEngine = SpringHelper.getBean(SpringTemplateEngine.class);

    /**
     * 发送邮件
     *
     * @param noticeInfoDTO 邮件发送信息
     */
    public static void send(NoticeInfoDTO noticeInfoDTO) {
        JavaMailSender defaultJavaMailSender = createDefaultSender();
        send(defaultJavaMailSender, SpringHelper.getEnvProperty("spring.mail.username"), noticeInfoDTO);
    }

    /**
     * 发送邮件
     *
     * @param javaMailSender 邮件发送器
     * @param from           发件人
     * @param noticeInfoDTO  邮件发送信息
     */
    public static void send(JavaMailSender javaMailSender, String from, NoticeInfoDTO noticeInfoDTO) {
        String to = noticeInfoDTO.getTo();
        String cc = noticeInfoDTO.getCc();
        String bcc = noticeInfoDTO.getBcc();
        String subject = noticeInfoDTO.getSubject();
        String content = noticeInfoDTO.getContent();
        Integer priority = noticeInfoDTO.getPriority();
        List<MultipartFile> fileList = noticeInfoDTO.getFileList();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            if (to.contains(",")) {
                helper.setTo(to.replace(" ", "").split(","));
            } else {
                helper.setTo(to);
            }
            helper.setFrom(from, noticeInfoDTO.getFromName());
            if (StringUtil.hasText(cc)) {
                if (cc.contains(",")) {
                    helper.setTo(cc.replace(" ", "").split(","));
                } else {
                    helper.setTo(cc);
                }
            }
            if (StringUtil.hasText(bcc)) {
                helper.setBcc(bcc);
            }
            helper.setSubject(subject);
            helper.setPriority(Optional.ofNullable(priority).orElse(3));

            Context context = new Context();
            // 定义模板数据
            context.setVariables(Map.of("content", content, "showTip", noticeInfoDTO.getShowTip()));
            // 获取 thymeleaf 的 html 模板 
            String emailContent = templateEngine.process("common_mail.html", context);
            // 指定模板路径
            helper.setText(emailContent, true);

            for (File file : multipartFileToFile(fileList)) {
                if (StringUtil.hasText(file.getPath())) {
                    helper.addAttachment(file.getName(), file);
                }
            }

            log.info("发送邮件：to={}, cc={}, bcc={}, subject={}, content={}, priority={}, fileList={}", to, cc, bcc, subject, content, priority, fileList);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送邮件失败：{}", e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * 创建默认邮件发送器（Spring Boot 配置文件里的邮件信息自动注入生成邮件发送器）
     *
     * @return 邮件发送器
     */
    public static JavaMailSenderImpl createDefaultSender() {
        return SpringHelper.getBean(JavaMailSenderImpl.class);
    }

    /**
     * 创建邮件发送器
     *
     * @param noticeMailConfig 邮件配置
     * @return 邮件发送器
     */
    public static JavaMailSenderImpl createSender(NoticeMailConfig noticeMailConfig) {
        if (StringUtil.hasEmpty(noticeMailConfig.getConfigId())) {
            log.warn("邮件配置 ID 为空，不创建邮件发送器");
            return null;
        }

        String key = CacheNameConstant.PREFIX + noticeMailConfig.getConfigId();

        // 从缓存获取邮件发送器
        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) CAFFEINE.getIfPresent(key);

        if (Objects.nonNull(javaMailSender)) {
            return javaMailSender;
        }

        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(noticeMailConfig.getHost());

        if (Objects.nonNull(noticeMailConfig.getProtocol())) {
            javaMailSender.setProtocol(noticeMailConfig.getProtocol());
        }
        javaMailSender.setUsername(noticeMailConfig.getUsername());
        javaMailSender.setPassword(noticeMailConfig.getPassword());
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", noticeMailConfig.getHost());
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.setProperty("mail.debug", "true"); // 查看发送邮件的每一步日志，用于定位问题

        if (Objects.nonNull(noticeMailConfig.getPort())) {
            javaMailSender.setPort(noticeMailConfig.getPort());
            props.setProperty("mail.smtp.port", String.valueOf(noticeMailConfig.getPort()));
        }
        javaMailSender.setJavaMailProperties(props);
        // 邮件发送器存入缓存
        CAFFEINE.put(key, javaMailSender);
        return javaMailSender;
    }

    /**
     * MultipartFile 转 File
     */
    public static File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String suffixFileName = FilenameUtils.getExtension(fileName);
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(StringUtil.hasEmpty(fileName) ? "" : fileName, suffixFileName);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            log.error("MultipartFile 转换 File 失败：{}", e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * MultipartFile 集合转 File 集合
     */
    public static List<File> multipartFileToFile(List<MultipartFile> multiFileList) {
        if (ListUtil.isEmpty(multiFileList)) {
            return ListUtil.newArrayList();
        }
        return multiFileList.stream().map(MailHelper::multipartFileToFile).toList();
    }
}
