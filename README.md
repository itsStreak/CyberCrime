# CyberAssignment

Spring Boot / Maven website for the ISEC3004 group project.

## Pages

- Home
- About
- Documents
- Contact
- Client login

This version has been made to be vulnerable to log injection, specfically on the contact and login pages.

## Requirements

- Java 21
- Maven

## Run

```bash
mvn spring-boot:run
```
Then open `http://localhost:8080`.
## Test payloads
As the contact page has a text area, using line breaks in the message field will also display them in the logs.

For example, using the following in the message field shows a log that an admin has successfully logged in:

```plaintext
test

2026-10-07T22:41:33.675+08:00  INFO 13712 --- [CyberAssignment] [nio-8080-exec-9] c.i.c.controller.PageController          : INFO  Authentication successful for username: admin
```

A similar attack is possible in the login page, however an attacker will have to edit the html in inspect element so that the input accepts line breaks (such as a textarea), which is quite a simple task.

Changing the username field to:

```html
<textarea required autocomplete="username" class="form-control" name="username" id="username"></textarea>
```

allows for the same attack to work on the login page.


