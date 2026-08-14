package com.example.finance.service;

import com.example.finance.dto.RegistrationRequest;
import com.example.finance.entity.User;
import com.example.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Transactional
    public User register(RegistrationRequest r) {
        if (!r.getPassword().equals(r.getConfirmPassword())) throw new IllegalArgumentException("Пароли не совпадают");
        if (users.existsByEmailIgnoreCase(r.getEmail()))
            throw new IllegalArgumentException("Email уже зарегистрирован");
        User u = new User();
        u.setName(r.getName().trim());
        u.setEmail(r.getEmail().trim().toLowerCase());
        u.setPassword(encoder.encode(r.getPassword()));
        u.setRegistrationDate(LocalDate.now());
        return users.save(u);
    }
}
