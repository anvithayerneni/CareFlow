package com.careflow.common.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Value("${careflow.cors.allowed-origin-patterns}") private String allowedOriginPatterns;
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean CorsConfigurationSource corsConfigurationSource(){CorsConfiguration cors=new CorsConfiguration();cors.setAllowedOriginPatterns(Arrays.stream(allowedOriginPatterns.split(",")).map(String::trim).filter(s->!s.isEmpty()).toList());cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));cors.setAllowedHeaders(List.of("Authorization","Content-Type"));cors.setExposedHeaders(List.of("Content-Disposition"));cors.setAllowCredentials(false);UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",cors);return source;}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,JwtFilter jwtFilter,AuthRequestFilter rateLimitFilter)throws Exception{return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/**","/api/v1/doctors/**","/actuator/health/**","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**").permitAll().anyRequest().authenticated()).addFilterBefore(rateLimitFilter,UsernamePasswordAuthenticationFilter.class).addFilterBefore(jwtFilter,BasicAuthenticationFilter.class).build();}
}
