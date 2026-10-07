package com.isec3004.cyberassignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PageController {

    private static final Logger logger =
            LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    public String home() {
        logger.info("Home page accessed");
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        logger.info("About page accessed");
        return "about";
    }

    @GetMapping("/documents")
    public String documents() {
        logger.info("Documents page accessed");
        return "documents";
    }

    @GetMapping("/login")
    public String login() {
        logger.info("Login page accessed");
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes) {

        // Normal application event only.
        logger.info("Login form submitted for username: " + username); // Made more unsafe for log injection testing


        redirectAttributes.addFlashAttribute(
                "message",
                "Login is not connected yet."
        );

        return "redirect:/login";
    }

    @GetMapping("/contact")
    public String contact() {
        logger.info("Contact page accessed");
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContact(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String message,
                                RedirectAttributes redirectAttributes) {

        logger.info("Contact form submitted, name: " + name + ", email: " + email + ", message: " + message); // Also made unsafe for log injection

        redirectAttributes.addFlashAttribute(
                "message",
                "Thanks, " + name + ". Your message has been received."
        );

        return "redirect:/contact";
    }
}