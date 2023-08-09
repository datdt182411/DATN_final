package com.example.test.Security;

import com.example.test.Config.MyUserDetails;
import com.example.test.Entity.AuthenticationType;
import com.example.test.Entity.Users;
import com.example.test.Service.CustomerService;
import com.example.test.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserServices userServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        Users user = myUserDetails.getUser();
        userServices.updateAuthenticationType(user, AuthenticationType.DATABASE);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
