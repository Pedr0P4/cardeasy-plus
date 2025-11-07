package ufrn.imd.cardeasy.security;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ufrn.imd.cardeasy.configurations.AppSecurityProperties;
import ufrn.imd.cardeasy.errors.Unauthorized;

@Component
public class Authenticator {
  private SecretKey key;

  @Autowired
  public Authenticator(
    AppSecurityProperties properties
  ) {
    this.key = Keys.hmacShaKeyFor(
      Base64.getDecoder().decode(properties.getJwt().getSecret())
    );
  };

  public String encrypt(UUID id) {
    long currentTime = System.currentTimeMillis();

    Date now = new Date(currentTime);
    Duration duration = Duration.ofDays(1);
    Date expiration = new Date(
      currentTime + duration.toMillis()
    );

    return Jwts.builder()
      .subject(id.toString())
      .issuedAt(now)
      .expiration(expiration)
      .signWith(this.key)
      .compact();
  };

  public UUID verify(String token) {
    if (token == null) throw new Unauthorized();

    try {
      Claims claims = Jwts
        .parser()
        .verifyWith(this.key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

      return UUID.fromString(claims.getSubject());
    } catch (Exception e) {
      throw new Unauthorized();
    }
  };
};
