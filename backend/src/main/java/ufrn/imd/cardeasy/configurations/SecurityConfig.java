package ufrn.imd.cardeasy.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ufrn.imd.cardeasy.security.AuthenticationMiddleware;
import ufrn.imd.cardeasy.services.AccountsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private AccountsService accounts;
  
  @Autowired
  public SecurityConfig(
    AccountsService accounts
  ) {
    this.accounts = accounts;
  };
  
  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  };

  @Bean
  public AuthenticationMiddleware authenticationMiddleware() {
    return new AuthenticationMiddleware(this.accounts);
  };

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    AuthenticationEntryPoint entryPoint,
    AccessDeniedHandler deniedHandler
  ) throws Exception {
    return http
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests((auth) -> auth
        .anyRequest().permitAll()
      ).csrf((csrf) -> csrf.disable())
      .sessionManagement((session) -> 
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      ).headers((headers) -> headers.frameOptions(
        (options) -> options.disable())
      ).addFilterBefore(
        authenticationMiddleware(), 
        UsernamePasswordAuthenticationFilter.class
      ).exceptionHandling(customizer -> customizer
        .authenticationEntryPoint(entryPoint)
        .accessDeniedHandler(deniedHandler)
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
