package com.example.test.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
                                        Authentication authentication) throws IOException, ServletException {

        boolean hasAdminRole = false;
        boolean hasSaleRole = false;
        boolean hasTechnicanRole = false;
        boolean hasStockerRole = false;
        boolean hasShipperRole = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ADMIN")
                    || grantedAuthority.getAuthority().equals("SALE")
                    || grantedAuthority.getAuthority().equals("TECHNICAN")
                    || grantedAuthority.getAuthority().equals("STOCKER")
                    || grantedAuthority.getAuthority().equals("SHIPPER")) {
                hasAdminRole = true;
                hasSaleRole = true;
                hasTechnicanRole = true;
                hasStockerRole = true;
                hasShipperRole = true;
                break;
            }
        }

        if (hasAdminRole ||  hasSaleRole || hasTechnicanRole || hasStockerRole || hasShipperRole) {
            redirectStrategy.sendRedirect(arg0, arg1, "/home");
        } else {
            throw new IllegalStateException();
        }
    }

}
