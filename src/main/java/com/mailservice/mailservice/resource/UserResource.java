package com.mailservice.mailservice.resource;


import com.mailservice.mailservice.Service.UserService;
import com.mailservice.mailservice.domain.HttpResponse;
import com.mailservice.mailservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpResponse> createUser(@RequestBody User user) {
        User newUser = userService.saveUser(user);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", newUser))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/sendEmail")
    public ResponseEntity<HttpResponse> sendEmail(@RequestParam("id") Long id) {
        String isSuccess = userService.sendEmail(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Email sent")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}