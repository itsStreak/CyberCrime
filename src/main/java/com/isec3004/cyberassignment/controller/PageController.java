package com.isec3004.cyberassignment.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PageController {


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/documents")
    public String documents() {
        return "documents";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes) {
        // Placeholder login behaviour for the site shell.
        // Authentication and assignment-specific logging can be added later.
        redirectAttributes.addFlashAttribute("message",
                "Login is not connected yet.");
        return "redirect:/login";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContact(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String message,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                "Thanks, " + name + ". Your message has been received.");
        return "redirect:/contact";
    }
}
