package com.myInventory.utils;


import com.myInventory.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Configuration file can be scanned by the classpath scanning
@Component
public class LoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // declaration of 'log' for debugging purposes

    private static final Logger log = LoggerFactory.getLogger(LoginHandler.class);

    // Method sets the full name of the user on the session and also displays
    // information on the server logs

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request,response,authentication);
        User user =(User) authentication.getPrincipal();
        log.debug("{} has logged in", user.getUsername());
        log.debug("{}",user.getFullName());
        request.getSession().setAttribute(Constants.LOGGED_USER, user.getFullName());
    }
}
