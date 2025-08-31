package hexlet.code.app.controller;

import hexlet.code.app.dto.UserCreateRequest;
import hexlet.code.app.dto.UserResponse;
import hexlet.code.app.dto.UserUpdateRequest;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> ResponseEntity.ok(convertToResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Hash password
        String hashedPassword = passwordService.hashPassword(request.getPassword());
        
        // Create user
        User user = new User(
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            hashedPassword
        );
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedUser));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User existingUser = existingUserOpt.get();
        
        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            existingUser.setEmail(request.getEmail());
        }
        
        // Update fields if provided
        if (request.getFirstName() != null) {
            existingUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existingUser.setLastName(request.getLastName());
        }
        if (request.getPassword() != null) {
            String hashedPassword = passwordService.hashPassword(request.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        
        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(convertToResponse(updatedUser));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    private UserResponse convertToResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCreatedAt()
        );
    }
}
