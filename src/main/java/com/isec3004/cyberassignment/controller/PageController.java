
package com.isec3004.cyberassignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

        // Intentionally vulnerable to Log Injection for assignment testing.
        // User-controlled input is written directly into the log.
        logger.info("Login form submitted for username: " + username);

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

        // Intentionally vulnerable to Log Injection for assignment testing.
        logger.info("Contact form submitted, name: " + name
                + ", email: " + email
                + ", message: " + message);

        redirectAttributes.addFlashAttribute(
                "message",
                "Thanks, " + name + ". Your message has been received."
        );

        return "redirect:/contact";
    }

    @GetMapping("/documents/download")
    public ResponseEntity<InputStreamResource> downloadDocument(
            @RequestParam String file) throws IOException {

        // Intentionally vulnerable to Path Traversal for assignment testing.
        // The user can supply paths such as ../outside.txt.
        File document = new File("src/main/resources/documents/" + file);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(new FileInputStream(document)));
    }
}
