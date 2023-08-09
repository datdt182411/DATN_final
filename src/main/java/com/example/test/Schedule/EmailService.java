package com.example.test.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String fromAddress, String toAddress, String subject,  String senderName, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(toAddress);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendEmailTest(String fromAddress, String toAddress, String subject,  String senderName, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        LocalDate now = LocalDate.now();

        helper.setTo(toAddress);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);

        // Phan 1 gom doan tin nhan
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(content);

        // phan 2 chua tap tin txt
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();

        // Duong dan den file cua ban khi export bang button trên Web
//        String filePath = "D:\\DA3\\Danh muc san pham bao tri " + now + ".xlsx" ;

        //Duong dan den file khi dung @Schedule tu dong sinh file
        String filePath ="Danh muc san pham bao tri " + now + ".xlsx" ;

        DataSource source1 = new FileDataSource(filePath);
        messageBodyPart2.setDataHandler(new DataHandler(source1));
        messageBodyPart2.setFileName(filePath);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);

        message.setContent(multipart);
        mailSender.send(message);
    }
}