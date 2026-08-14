package com.example.finance.controller;

import com.example.finance.dto.RegistrationRequest;
import com.example.finance.entity.User;
import com.example.finance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserService users;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest r) {
        User u = users.register(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", u.getId(), "email", u.getEmail()));
    }
}
