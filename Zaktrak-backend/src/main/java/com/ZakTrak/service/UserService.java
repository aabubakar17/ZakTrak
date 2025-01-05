package com.ZakTrak.service;
import com.ZakTrak.dto.NewUserRequest;
import com.ZakTrak.model.User;
import com.ZakTrak.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.ZakTrak.dto.AuthenticationResponse;
import com.ZakTrak.service.JwtService;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse createUser(NewUserRequest request) {
        // Check if email exists
        if (userRepository.findByEmail(request.email()) != null) {
            throw new IllegalStateException("Email already registered");
        }

        // Create and save user
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), encodedPassword, request.firstName(), request.lastName());
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(savedUser);

        // Return authentication response with token
        return new AuthenticationResponse(token);
    }

    public User getCurrentUser() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                throw new UsernameNotFoundException("No authenticated user found");
            }

            String userEmail = ((UserDetails) auth.getPrincipal()).getUsername();
            User user = userRepository.findByEmail(userEmail);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + userEmail);
            }

            return user;
    }

}
