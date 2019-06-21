package com.daiyanping.cms.mail;

import ch.qos.logback.core.net.LoginAuthenticator;
import com.sun.mail.imap.IMAPBodyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * @ClassName MailService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-06-21
 * @Version 0.1
 */
@Service
public class MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    public void test() {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        // 发件人
//        simpleMailMessage.setFrom("841943896@qq.com");
//        // 收件人
//        simpleMailMessage.setTo("841943896@qq.com");
//        // 纯文本信息
//        simpleMailMessage.setText("hello");
//        // 主题
//        simpleMailMessage.setSubject("测试");
//
//        javaMailSender.send(simpleMailMessage);
        Properties props = new Properties();

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("841943896@qq.com", "uinilqpqenjrbeic");
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setSubject("附件测试");
            mimeMessage.setFrom(new InternetAddress("841943896@qq.com"));
            mimeMessage.setRecipients(Message.RecipientType.TO, "841943896@qq.com");
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            String property = System.getProperty("user.dir");
            mimeBodyPart.attachFile(new File(property + "/pom.xml"));
            MimeBodyPart textMimeBodyPart = new MimeBodyPart();
            textMimeBodyPart.setText("内容");
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textMimeBodyPart);
            mimeMultipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(mimeMultipart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);

    }
}
