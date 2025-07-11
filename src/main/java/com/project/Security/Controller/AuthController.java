package com.project.Security.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Security.DTO.AuthRequest;
import com.project.Security.DTO.AuthResponse;
import com.project.Security.DTO.RefreshRequest;
import com.project.Security.Entity.UserEntity;
import com.project.Security.JWT.JwtUtil;
import com.project.Security.Service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            
            UserEntity user = userService.findByUsername(request.getUsername());
            if (!user.getENABLED()) {
                return ResponseEntity.status(403).body("Account is disabled");
            }
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            
            String accessToken = jwtUtil.generateAccessToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                    .map(authentication -> authentication.getAuthority())
                    .collect(Collectors.toList())
            );
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
            List<String> roles = userDetails.getAuthorities().stream()
                .map(authentication -> authentication.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toList());
            List<String> permissions = userDetails.getAuthorities().stream()
				.map(authentication -> authentication.getAuthority())
				.filter(authority -> !authority.startsWith("ROLE_"))
				.collect(Collectors.toList());
            List<String> functions = user.getROLES().stream()
				.flatMap(role -> role.getFunctions().stream())
				.map(function -> function.getName()).distinct()
				.collect(Collectors.toList());
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, roles, functions, permissions, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
//        String refreshToken = request.getRefreshToken();
//        try {
//            String username = jwtUtil.extractUsername(refreshToken);
//            if (jwtUtil.validateToken(refreshToken, username)) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                String newAccessToken = jwtUtil.generateAccessToken(
//                    userDetails.getUsername(),
//                    userDetails.getAuthorities().stream()
//                        .map(authentication -> authentication.getAuthority())
//                        .collect(Collectors.toList())
//                );
//                return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, null, userDetails.getUsername()));
//            }
//            return ResponseEntity.status(401).body("Invalid refresh token");
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid refresh token");
//        }
//    }
}