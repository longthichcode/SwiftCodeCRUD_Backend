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
            .csrf(csrf -> csrf.disable()) // vô hiệu hoá CSRF bảo vệ, do sử dụng JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sử dụng Stateless session để không lưu trứ session trên server
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin("http://localhost:4200");
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))  // cho phé các yêu cầu cord từ frontenf truy cập vào API 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login", "/api/refresh", "/api/users/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jpadynamic/swiftcode/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/jpadynamic/swiftcode/{id}", "/api/jpadynamic/swiftcode/search").hasAnyAuthority("VIEW_SWIFT_CODE")
                .requestMatchers(HttpMethod.POST, "/api/jpadynamic/swiftcode").hasAnyAuthority("MANAGE_SWIFT_CODE")
                .requestMatchers(HttpMethod.PUT, "/api/jpadynamic/swiftcode/{id}").hasAnyAuthority("UPDATE_DELETE_SWIFT_CODE")
                .requestMatchers(HttpMethod.DELETE, "/api/jpadynamic/swiftcode/{id}").hasAnyAuthority("UPDATE_DELETE_SWIFT_CODE")
                .requestMatchers(HttpMethod.POST, "/api/users/create").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/update/{id}").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/update-roles/{id}").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/update-status/{id}").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/{id}/roles").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/roles").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/rolesandper").hasAnyRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/roles/{roleId}/permissions").hasAnyRole("SUPER_ADMIN")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    // Cấu hình AuthenticationManager sử dụng trong JwtAuthenticationFilter
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Mã hoá mật khẩu sử dụng BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}