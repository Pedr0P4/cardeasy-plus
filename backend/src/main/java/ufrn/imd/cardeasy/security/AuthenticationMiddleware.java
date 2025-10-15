package ufrn.imd.cardeasy.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ufrn.imd.cardeasy.models.Account;
import ufrn.imd.cardeasy.services.AccountsService;

@Component
public class AuthenticationMiddleware extends OncePerRequestFilter {
  private AccountsService accounts;

  @Autowired
  public AuthenticationMiddleware(
    AccountsService accounts
  ) {
    this.accounts = accounts;
  };

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, 
    @NonNull HttpServletResponse response, 
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    Optional<String> token = this.getToken(request);
    
    if(token.isPresent()) {
      try {
        Account account = this.accounts.verify(token.get());

        UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
            account, 
            null,
            account.getAuthorities()
          );

        SecurityContextHolder.getContext().setAuthentication(
          authentication
        );
      } catch (Exception e) {
        throw new BadCredentialsException("Acesso negado!");
      };
    };
      
    filterChain.doFilter(request, response);
  };

  private Optional<String> getToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if(header != null && header.startsWith("Bearer ")) 
      return Optional.of(header.substring(7));
    else return Optional.empty();
  };
};
