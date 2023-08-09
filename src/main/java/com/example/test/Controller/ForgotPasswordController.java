package com.example.test.Controller;

import com.example.test.Entity.Users;
import com.example.test.Entity.Utility;
import com.example.test.Exception.UserNotFoundException;
import com.example.test.Repository.UserRepository;
import com.example.test.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    UserServices userServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot_password")
    public String showRequestForm() {
        return "User/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        Users users = userServices.getUserByEmail(email);
        if ( users == null){
            model.addAttribute("message", "Địa chỉ email không tồn tại!");
        }
        try {
            String token = userServices.updateResetPasswordToken(email);
            System.out.println(token);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link, email);
            if(token != " ") {
                model.addAttribute("message", "Chúng tôi đã gửi đường dẫn đổi mật khẩu tới email của bạn. Hãy kiểm tra email !");
            }
        } catch (UserNotFoundException e) {
               model.addAttribute("error", e.getMessage());
            } catch (UnsupportedEncodingException | MessagingException e) {
               model.addAttribute("error", "Could not send email");
        }

        return "User/forgot_password_form";
    }

    public void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String subject = "Here's the link to reset your password";
        String fromAddress = "thanhdatdo99cnh@gmail.com";
        String senderName = "Công ty thiết bị vật tư Y tế";

        String content = "<p>Hello,</p>"
                + "<p> You have requested to reset your password.</p>"
                + "Click the link below to change your password: </p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(toAddress);
        helper.setFrom(fromAddress, senderName);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(String token, Model model){
        Users users = userServices.getByResetPasswordToken(token);
        if (users != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("message", "Invalid Token");
            return "User/message";
        }

        return "User/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            System.out.println("Check:" + password);
            userServices.updatePassword(token, password);
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "You have successfully changed your password");

            return "User/message";
        } catch (UserNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", e.getMessage());
            return "User/message";

        }
    }
}
