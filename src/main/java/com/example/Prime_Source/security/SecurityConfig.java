package com.example.Prime_Source.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // ✅ Enables @PreAuthorize, @Secured, etc.
public class SecurityConfig {

    // ✅ Inject your custom UserDetailsService implementation
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // ✅ BCrypt for hashing passwords (recommended)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ AuthenticationManager uses your UserDetailsService and password encoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService) // custom logic to load user
                .passwordEncoder(passwordEncoder())     // matches encrypted password
                .and()
                .build();
    }

    // ✅ Main Spring Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // ❗ Disable CSRF for development; enable for production
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/login", "/perform_login", "/css/**", "/js/**",
            	                     "/api/users/admin-exists","/api/users/create-admin").permitAll()
            	    .anyRequest().authenticated()
            	)

            .formLogin(form -> form
                .loginPage("/Adminlogin.html")        // ✅ static login page URL
                .loginProcessingUrl("/perform_login") // ✅ Spring Security handles POST here
                .defaultSuccessUrl("/dashboard.html", true) // ✅ redirect after successful login
                .failureUrl("/login.html?error")       // ✅ append error query if failed
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Default: POST to /logout
                .logoutSuccessUrl("/login.html?logout") // After logout, show message
                .permitAll()
            );

        return http.build();
    }
}
