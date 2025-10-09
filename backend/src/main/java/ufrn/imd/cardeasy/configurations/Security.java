package ufrn.imd.cardeasy.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http
  ) throws Exception {
    return http
      .authorizeHttpRequests((auth) -> auth
        .requestMatchers("/console/**").permitAll()
        // TODO - Lembrar que isso aqui é questão 
        // de segurança no futuro
        .anyRequest().permitAll()
      ).csrf((csrf) -> csrf.disable())
      .headers((headers) -> headers.frameOptions(
        (options) -> options.disable())
      ).build();
  };
};
