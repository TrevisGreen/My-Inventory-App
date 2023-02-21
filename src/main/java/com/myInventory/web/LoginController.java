package com.myInventory.web;

import com.myInventory.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// Defines the class as a controller in the MVC architecture
@Controller
public class LoginController {

    // Initialized first, takes us to the login screen
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }
}
