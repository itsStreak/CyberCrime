package com.isec3004.cyberassignment.controller;

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

    @GetMapping("/documents/download")
    public ResponseEntity<InputStreamResource> downloadDocument(@RequestParam String file) throws IOException {
        File document = new File("src/main/resources/documents/" + file);   // this is the path to the document folder in the project - the vulnerability is that the user can specify any file path, including sensitive files outside the intended directory

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(new FileInputStream(document)));
    }
}
