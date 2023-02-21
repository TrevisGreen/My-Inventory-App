package com.myInventory.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//Defines class a controller in the MVC architecture
@Controller
public class HomeController {

    // declaration of 'log' for debugging purpose
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    // Method that returns us to the home page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        log.debug("Home");
        return "home";
    }
}
