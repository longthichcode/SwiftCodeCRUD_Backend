package com.project.Security.Service;

import com.project.Security.Entity.UserEntity;
import com.project.Security.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy thông tin người dùng: " + username));
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        // Thêm roles và permissions từ functions
        user.getROLES().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getROLE_NAME()));
            // Thêm permissions từ functions
            role.getFunctions().forEach(function ->
                function.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getName()))
                )
            );
        });

        return new User(
            user.getUSER_NAME(),
            user.getPASSWORD(),
            user.getENABLED(),
            true, true, true,
            authorities
        );
    }
}