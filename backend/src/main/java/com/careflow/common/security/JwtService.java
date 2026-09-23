package com.careflow.common.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
@Service
public class JwtService {
  private final SecretKey key; private final long ttl;
  public JwtService(@Value("${careflow.jwt.secret}") String secret,@Value("${careflow.jwt.access-ttl-minutes}") long ttl){this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.ttl=ttl;}
  public String issue(String subject,String role){Instant now=Instant.now();return Jwts.builder().subject(subject).claim("role",role).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(ttl*60))).signWith(key).compact();}
  public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
  public String role(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("role",String.class);}
}
