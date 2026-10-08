
package com.isec3004.cyberassignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class PageController {

    private static final Logger logger =
            LoggerFactory.getLogger(PageController.class);

    // Restrict downloads to this directory only.
    private static final Path DOCUMENTS_DIRECTORY =
            Paths.get("src/main/resources/documents")
                    .toAbsolutePath()
                    .normalize();

    /*
      Log mitagation
      Sanitize untrusted values before writing them to logs, Escape CR, LF and other control characters so user input cannot create fake log entries or manipulate log output.
     */
    private static String sanitizeForLog(String input) {

        if (input == null) {
            return "null";
        }

        StringBuilder sanitized = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {

            char character = input.charAt(i);

            switch (character) {
                case '\n':
                    sanitized.append("\\n");
                    break;

                case '\r':
                    sanitized.append("\\r");
                    break;

                case '\t':
                    sanitized.append("\\t");
                    break;

                default:
                    if (Character.isISOControl(character)
                            || character == '\u2028'
                            || character == '\u2029') {
                        sanitized.append(
                                String.format("\\u%04X", (int) character)
                        );
                    } else {
                        sanitized.append(character);
                    }
            }
        }

        return sanitized.toString();
    }

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

        /*
         * Sanitize user input before logging. Parameterized logging avoids string concatenation, while sanitization prevents forged log lines.
         */
        logger.info(
                "Login form submitted for username: {}",
                sanitizeForLog(username)
        );

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

        /*
         Sanitize all user-controlled fields before writing them to the application log.
         */
        logger.info(
                "Contact form submitted, name: {}, email: {}, message: {}",
                sanitizeForLog(name),
                sanitizeForLog(email),
                sanitizeForLog(message)
        );

        redirectAttributes.addFlashAttribute(
                "message",
                "Thanks, " + name + ". Your message has been received."
        );

        return "redirect:/contact";
    }

    /*
            Path traversal mitigation
     */
    @GetMapping("/documents/download")
    public ResponseEntity<?> downloadDocument(
            @RequestParam String file) throws IOException {

        // Reject empty filenames, directory separators and traversal syntax.
        if (file.isBlank()
                || file.equals(".")
                || file.equals("..")
                || file.contains("/")
                || file.contains("\\")
                || file.contains(":")
                || file.indexOf('\0') >= 0) {

            logger.warn(
                    "Blocked invalid document download request: {}",
                    sanitizeForLog(file)
            );

            return ResponseEntity.badRequest()
                    .body("Invalid filename.");
        }

        // Resolve the requested filename against the trusted directory.
        Path requestedFile = DOCUMENTS_DIRECTORY
                .resolve(file)
                .normalize();

        // Ensure the resolved path stays within the permitted directory.
        if (!requestedFile.startsWith(DOCUMENTS_DIRECTORY)) {

            logger.warn(
                    "Blocked path traversal attempt: {}",
                    sanitizeForLog(file)
            );

            return ResponseEntity.badRequest()
                    .body("Invalid file path.");
        }

        // Reject missing files, directories and symbolic links.
        if (!Files.isRegularFile(
                requestedFile,
                java.nio.file.LinkOption.NOFOLLOW_LINKS)) {

            logger.warn(
                    "Document not found or not permitted: {}",
                    sanitizeForLog(file)
            );

            return ResponseEntity.notFound().build();
        }

        // Resolve the real path to guard against filesystem redirection.
        Path realDirectory = DOCUMENTS_DIRECTORY.toRealPath();
        Path realFile = requestedFile.toRealPath(
                java.nio.file.LinkOption.NOFOLLOW_LINKS
        );

        if (!realFile.startsWith(realDirectory)) {

            logger.warn(
                    "Blocked document outside allowed directory: {}",
                    sanitizeForLog(file)
            );

            return ResponseEntity.badRequest()
                    .body("Invalid file path.");
        }

        logger.info(
                "Document downloaded successfully: {}",
                sanitizeForLog(file)
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file + "\""
                )
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(
                        Files.newInputStream(realFile)
                ));
    }
}
