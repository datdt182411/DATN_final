package com.example.test.Config;

import com.example.test.Security.AuthenticationSuccessHandler;
import com.example.test.Security.CustomerOAuth2UserService;
import com.example.test.Security.DatabaseLoginSuccessHandler;
import com.example.test.Security.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;



    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/page/**", "/register", "/create_user",
                        "/product/**", "/intro/**", "/contact/**", "/verify/**", "/productDetail", "/oauth2/**","/forgot_password/**", "/reset_password/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/reports/**").hasAnyAuthority("ADMIN", "SALE")
                .antMatchers("/home").hasAnyAuthority("ADMIN", "STOCKER", "TECHNICIAN", "SALE", "SHIPPER").antMatchers("/edit/**")
                .hasAnyAuthority("ADMIN", "EDITOR").antMatchers("/delete/**").hasAuthority("ADMIN")
                .antMatchers("/sql/**", "/logout/**", "/login/**").permitAll().
                anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .successHandler(databaseLoginSuccessHandler)
                .permitAll()

//                .and().formLogin().successHandler(successHandler).loginPage("/login")
//                .permitAll()

                .and()
                .oauth2Login()
                .loginPage("/login").userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/403");
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
