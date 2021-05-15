package com.cr.campusmicroblog.util;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cr.campusmicroblog.entity.Mail;
import com.cr.campusmicroblog.mapper.MailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * @author cr
 * @date 2020-11-19 12:24
 */
@Slf4j
@Component
public class MailSender implements InitializingBean {
    private JavaMailSenderImpl mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailMapper mailMapper;

    public boolean sendWithHtmlTemplate(String to, String subject, String template, Map<String, Object> model) {
        try {
            // 邮箱格式校验
            if (!Validator.isEmail(to)) {
                return false;
            }

            String nick = MimeUtility.encodeText("校园微博系统");
            InternetAddress from = new InternetAddress(nick + "<931009686@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            Context context = new Context();
            context.setVariable("username", to);
            String result = templateEngine.process(template, context);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        String username = "931009686@qq.com";
        Mail mail = mailMapper.selectOne(new QueryWrapper<Mail>().eq("username", username));
        mailSender.setHost(mail.getHost());
        mailSender.setUsername(username);
        mailSender.setPassword(mail.getPassword());
        mailSender.setPort(mail.getPort());

        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
