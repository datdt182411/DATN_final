package com.example.test.Controller;

import com.example.test.Entity.AuthenticationType;
import com.example.test.Entity.Role;
import com.example.test.Entity.Users;
import com.example.test.Repository.RoleRepository;
import com.example.test.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder,

                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/userManager")
    public String Index(Model model) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Users> users = userRepository.findAll(pageable);
        int totalPages = users.getTotalPages();
        long totalItems = users.getTotalElements();
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("userList", users);
        return "User/userManager";
    }

    @GetMapping("/userManager/{pageNumber}")
    public String Index(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        Page<Users> users = userRepository.findAll(pageable);
        int totalPages = users.getTotalPages();
        long totalItems = users.getTotalElements();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("userList", users);
        return "User/userManager";
    }

    @GetMapping("/UserDelete")
    public String UserDelete(Model model, @RequestParam(name = "id") Integer id) {
        Users user = userRepository.getUsersById(id);
        user.setStatus(0);
        userRepository.save(user);
        model.addAttribute("userList", userRepository.findAll());
        return "User/userManager";
    }

    @GetMapping("/UserActive")
    public String ActiveUser(Model model, @RequestParam(name = "id") Integer id) {
        Users user = userRepository.getUsersById(id);
        user.setStatus(1);
        userRepository.save(user);
        model.addAttribute("userList", userRepository.findAll());
        return "User/userManager";
    }

    @GetMapping("/ChangePassword")
    public String changePass(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetail.getUsername());
        Users users = userRepository.getUsersByUsername(userDetail.getUsername());
        model.addAttribute("user", users);
        return "User/changePassword";
    }

    @PostMapping("/ChangePassword")
    public String changePassword(Model model, @RequestParam("password1") String password1,
                                 @RequestParam("password2") String password2, RedirectAttributes redirectAttrs) {
        System.out.println(password1 + " _ " + password2);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetail.getUsername());
        Users users = userRepository.getUsersByUsername(userDetail.getUsername());

        if (password1.equalsIgnoreCase(userDetail.getUsername())) {
            redirectAttrs.addFlashAttribute("messageChangePass", "Mật khẩu mới không được phép trùng với tên tài khoản! !");
            return "redirect:/ChangePassword";
        } else if (password1.equals(password2)) {
            users.setPassword(passwordEncoder.encode(password1));
            userRepository.save(users);
            redirectAttrs.addFlashAttribute("messageSuccess", "Đổi mật khẩu thành công !");
            return "redirect:/ChangePassword";
        } else {
            redirectAttrs.addFlashAttribute("messageChangePass", "Mật khẩu confirm không khớp với mật khẩu mới, vui lòng nhập lại !");
            return "redirect:/ChangePassword";
        }
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new Users());
        return "User/addNewUser";
    }

    @PostMapping("/addNewUser")
    public String addSale(Model model, @ModelAttribute("user") Users users,
                          @RequestParam(name = "role") String role,
                          @RequestParam(name = "confirmPass") String confirmPass) {
        System.out.println(users);
        if (confirmPass.equals(users.getPassword())) {
            try {
                Users user = new Users();
                List<Role> roles = new ArrayList<>();
                if (role.equals("SALE")) {
                    roles = roleRepository.findRoleByName("SALE");
                } else if (role.equals("STOCKER")) {
                    roles = roleRepository.findRoleByName("STOCKER");
                } else if (role.equals("TECHNICIAN")) {
                    roles = roleRepository.findRoleByName("TECHNICIAN");
                } else if (role.equals("SHIPPER")) {
                    roles = roleRepository.findRoleByName("SHIPPER");
                }
                user.setRoleList(roles);
                user.setUsername(users.getUsername());
                user.setPhone(users.getPhone());
                user.setEmail(users.getEmail());
                user.setStatus(1);
                user.setPassword(passwordEncoder.encode(users.getPassword()));
                user.setAuthenticationType(AuthenticationType.valueOf("DATABASE"));
                userRepository.save(user);
                model.addAttribute("sucess", "Thêm tài khoản Thành Công, Xem thông tin chi tiết ở phần quản lý tài khoản! ");

            } catch (Exception ex) {
                model.addAttribute("mess", "Thêm tài khoản lỗi! ");
            }
        } else {
            model.addAttribute("mess", "Mật khẩu confirm không trùng với mật khẩu! ");
        }
        return "User/addNewUser";
    }

}
