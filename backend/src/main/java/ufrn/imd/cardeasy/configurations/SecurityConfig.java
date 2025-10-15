package ufrn.imd.cardeasy.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

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
      .authorizeHttpRequests((auth) -> auth
        .anyRequest().permitAll()
      ).csrf((csrf) -> csrf.disable())
      .headers((headers) -> headers.frameOptions(
        (options) -> options.disable())
      ).exceptionHandling(customizer -> customizer
        .authenticationEntryPoint(entryPoint)
      ).build();
  };
};
