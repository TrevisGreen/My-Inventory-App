package com.myInventory.web;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// Defines class as Controller in the MVC architecture
@Controller
public class LogoutController {

    // This method logs us out, and takes us back to the login screen
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
