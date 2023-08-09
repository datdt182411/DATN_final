package com.example.test.Service;

import com.example.test.Entity.Product;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOutOfStockNotification(List<Product> products) throws MessagingException, UnsupportedEncodingException {
        String toAddress = "thanhdatdo99cnh@gmail.com";
        String subject = "Sản phẩm trong kho đã hết";
        String fromAddress = "thanhdatdo99cnh@gmail.com";
        String senderName = "Công ty thiết bị vật tư Y tế";


//        String content = "Sản phẩm " + product.get() + " đã hết hàng.";
        StringBuilder content = new StringBuilder();
        content.append("Sản phẩm đã hết hàng:\n");
        for (Product product : products) {
            content.append("- ").append(product.getName()).append("\n");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(toAddress);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);

//        helper.setText(content, true);
        helper.setText(content.toString());
        mailSender.send(message);

    }
}
