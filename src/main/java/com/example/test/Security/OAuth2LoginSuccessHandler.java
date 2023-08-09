package com.example.test.Security;

import com.example.test.Entity.AuthenticationType;
import com.example.test.Entity.Users;
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
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserServices userServices;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String clientName = oAuth2User.getClientName();

        System.out.println(" OAuth2LoginSuccessHandler: " + name + " | " + email);

        System.out.println("Client name: " + clientName);
        //Error
//        Customer customer = customerService.getCustomerByEmail(email);
//        if(customer == null) {
//            customerService.addNewCustomerUponOAuthLogin(name, email);
//        } else {
//            customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
//        }

        AuthenticationType authenticationType = getAuthenticationType(clientName);
        Users users = userServices.getUserByEmail(email);
        if(users == null) {
            userServices.addNewUserUponOAuthLogin(name, email, authenticationType);
        } else {
            userServices.updateAuthenticationType(users, authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName){
        if (clientName.equals("Google")){
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else {
            return AuthenticationType.DATABASE;
        }
    }
}
