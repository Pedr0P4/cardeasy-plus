package ufrn.imd.cardeasy.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  };

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    AuthenticationEntryPoint entryPoint
  ) throws Exception {
    return http
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests((auth) -> auth
        .anyRequest().permitAll()
      ).csrf((csrf) -> csrf.disable())
      .headers((headers) -> headers.frameOptions(
        (options) -> options.disable())
      ).exceptionHandling(customizer -> customizer
        .authenticationEntryPoint(entryPoint)
      ).build();
  };

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    configuration.setAllowedOrigins(List.of("http://localhost:3000")); 
    configuration.setAllowedMethods(List.of("*")); 
    configuration.setAllowedHeaders(List.of("*")); 
 
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration); 
    return source;
  };
};
