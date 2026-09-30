# CyberAssignment

Spring Boot / Maven website for the ISEC3004 group project.

## Pages

- Home
- About
- Documents
- Contact
- Client login

The current version is the normal website shell. Assignment-specific vulnerable and mitigated implementations can be added separately.

## Requirements

- Java 21
- Maven

## Exploits 
### Path Traversal 
http://localhost:8080/documents/download?file=../outside.txt    -  Accesses document outside of wanted documents 

Vulnerable Java Line:
```
File document = new File("src/main/resources/documents/" + file);
```

## Run

```bash
mvn spring-boot:run
```

Then open `http://localhost:8080`.
