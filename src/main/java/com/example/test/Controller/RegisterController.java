package com.example.test.Controller;

import com.example.test.Entity.Users;
import com.example.test.Entity.Utility;
import com.example.test.Repository.RoleRepository;
import com.example.test.Repository.UserRepository;
import com.example.test.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserServices userServices;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegisterController(RoleRepository roleRepository,
                              UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegister(Model model, @ModelAttribute("user") Users users) {
        model.addAttribute("user", new Users());
        return "User/register";
    }

//    @PostMapping("/create_user")
//    public String register(Model model, @ModelAttribute("user") Users user,
//                           HttpServletRequest request)
//                           throws UnsupportedEncodingException, MessagingException {
//        try {
//            if(userRepository.getUsersByUsername(user.getUsername())!=null){
//                model.addAttribute("mess", "Tài khoản đã tồn tại, vui lòng nhập username khác! ");
//                return "User/register";
//            }
//            userServices.registerUser(user);
//            sendVerificationEmail(request, user);
//                model.addAttribute("sucess", "Thêm tài khoản Thành Công, Bạn có thể đăng nhập ngay bây giờ! ");
//        }catch (Exception ex){
//            model.addAttribute("mess", "Thêm tài khoản lỗi! ");
//        }
//        return "User/register_success";
//    }
    @PostMapping("/create_user")
    public String createUser(Users user, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        try {
            if (userRepository.getUsersByUsername(user.getUsername()) != null) {
                model.addAttribute("mess", "Tên đăng nhập đã tồn tại, vui lòng nhập tên đăng nhập khác!");
                return "User/register";
            } else {
                userServices.registerUser(user);
                sendVerificationEmail(request, user);
                model.addAttribute("success", "Thêm tài khoản thành công, bạn có thể đăng nhập ngay bây giờ!");
                return "User/register_success";
            }
        } catch (Exception ex) {
            model.addAttribute("mess", "Thêm tài khoản lỗi!");
            return "User/register";
        }
    }

    private void sendVerificationEmail(HttpServletRequest request, Users user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "thanhdatdo99cnh@gmail.com";
        String senderName = "Công ty thiết bị vật tư y tế";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Công ty thiết bị y tế.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        System.out.println("to Address: " + toAddress);
        System.out.println("Verify URL: " + verifyURL);


    }


    //Test///////////////////////////////////
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userServices.verify(code)) {
            return "User/verify_success";
        } else {
            return "User/verify_fail";
        }
    }
}
