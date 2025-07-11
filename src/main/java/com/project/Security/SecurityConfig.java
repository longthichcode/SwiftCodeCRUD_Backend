package com.project.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import com.project.Security.JWT.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF do sử dụng JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin("http://localhost:4200");
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            .authorizeHttpRequests(auth -> auth
                // Các endpoint không cần xác thực
                .requestMatchers("/api/login", "/api/refresh", "/api/users/register").permitAll()
                
                // ccs endpoint liên quan đến SwiftCode
                .requestMatchers(HttpMethod.POST, "/api/jpadynamic/swiftcode/search").hasAuthority("VIEW_SWIFTCODE")
                .requestMatchers(HttpMethod.POST, "/api/jpadynamic/swiftcode").hasAuthority("CREATE_SWIFTCODE")
                .requestMatchers(HttpMethod.PUT, "/api/jpadynamic/swiftcode/{id}").hasAuthority("UPDATE_SWIFTCODE")
                .requestMatchers(HttpMethod.DELETE, "/api/jpadynamic/swiftcode/{id}").hasAuthority("DELETE_SWIFTCODE")
                .requestMatchers(HttpMethod.GET, "/api/jpadynamic/swiftcode/{id}").hasAuthority("VIEW_SWIFTCODE_DETAIL")
                
                // các endpoint liên quan đến người dùng
                .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("VIEW_USERS")
                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAuthority("VIEW_USER_DETAIL")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("UPDATE_USER")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/update-roles").hasAuthority("UPDATE_USER_ROLE")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/update-status").hasAuthority("UPDATE_USER_STATUS")
                .requestMatchers(HttpMethod.POST, "/api/users/create").hasAuthority("CREATE_USER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("DELETE_USER")
                
                // các endpoint liên quan đến vai trò và chức năng
                .requestMatchers(HttpMethod.GET, "/api/users/roles").hasAuthority("VIEW_ROLES")
                .requestMatchers(HttpMethod.PUT, "/api/users/roles/{id}/functions").hasAuthority("UPDATE_ROLE_FUNCTION")
                .requestMatchers(HttpMethod.GET, "/api/users/roles/functions").hasAuthority("VIEW_FUNCTION")
                .requestMatchers(HttpMethod.PUT, "/api/users/roles/functions").hasAuthority("UPDATE_FUNCTION")
                
                // các endpoint liên quan đến chức năng và quyền
                .requestMatchers(HttpMethod.GET, "/api/users/functions").hasAuthority("VIEW_FUNCTION") // Sửa từ UserController
                .requestMatchers(HttpMethod.GET, "/api/users/functions/{functionId}/permissions").hasAuthority("VIEW_FUNCTION")
                .requestMatchers(HttpMethod.GET, "/api/users/permissions").hasAuthority("VIEW_FUNCTION")
                .requestMatchers(HttpMethod.GET, "/api/users/{id}/roles").hasAuthority("VIEW_ROLES")
                
                // Yêu cầu xác thực cho tất cả các endpoint còn lại
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    // Cấu hình AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Mã hóa mật khẩu sử dụng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}