package com.example.test.Schedule;

import com.example.test.Export.MaintenanceExcelExporter;
import com.example.test.Repository.MaintenanceOrderRepository;
import com.example.test.Service.MaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class EmailScheduler {

    @Autowired
    private EmailService emailService;

    private final MaintenanceOrderRepository maintenanceRepository;

    private final MaintenanceOrderService maintenanceOrderService;

    public EmailScheduler(MaintenanceOrderRepository maintenanceRepository, MaintenanceOrderService maintenanceOrderService) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceOrderService = maintenanceOrderService;
    }

    @Scheduled(cron = "0 56 10 * * ?")
    public void sendEmail() throws MessagingException, UnsupportedEncodingException {
        String toAddress = "datdtcnh@gmail.com";
        String fromAddress = "thanhdatdo99cnh@gmail.com";
        String subject = "Danh mục các sản phẩm cần bảo trì";
        String senderName = "Công ty thiết bị vật tư Y tế";
        String content = "Các sản phẩm cần bảo trì";
        emailService.sendEmailTest(fromAddress, toAddress, subject, senderName, content);

    }

}