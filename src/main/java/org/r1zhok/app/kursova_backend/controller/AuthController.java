package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.LoginRequest;
import org.r1zhok.app.kursova_backend.dto.LoginResponse;
import org.r1zhok.app.kursova_backend.dto.RegisterRequest;
import org.r1zhok.app.kursova_backend.entity.User;
import org.r1zhok.app.kursova_backend.entity.UserRole;
import org.r1zhok.app.kursova_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Реєстрація нового користувача
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email вже використовується!", HttpStatus.BAD_REQUEST);
        }

        UserRole role = UserRole.valueOf(request.getRole() != null ? request.getRole().toUpperCase() : "USER");
        User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail(), role);

        userRepository.save(newUser);
        return new ResponseEntity<>("Користувач успішно зареєстрований.", HttpStatus.CREATED);
    }

    /**
     * Вхід користувача
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Невірний email або пароль.", HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(request.getPassword())) {
            return new ResponseEntity<>("Невірний email або пароль.", HttpStatus.UNAUTHORIZED);
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());

        return ResponseEntity.ok(response);
    }
}
