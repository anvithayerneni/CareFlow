package com.careflow.auth;
import com.careflow.common.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final JdbcTemplate db; private final PasswordEncoder passwords; private final JwtService jwt; private final SecureRandom random=new SecureRandom();
 public AuthController(JdbcTemplate db,PasswordEncoder passwords,JwtService jwt){this.db=db;this.passwords=passwords;this.jwt=jwt;}
 public record Register(@Email @NotBlank String email,@NotBlank @Size(min=12,max=72) String password,@NotBlank @Size(max=100) String firstName,@NotBlank @Size(max=100) String lastName){}
 public record Login(@Email @NotBlank String email,@NotBlank String password){}
 public record TokenResponse(String accessToken,String refreshToken,String tokenType,String role,String displayName){}
 @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public TokenResponse register(@Valid @RequestBody Register r){
  UUID id=UUID.randomUUID();String email=r.email().trim().toLowerCase();try{db.update("INSERT INTO users(id,email,password_hash,first_name,last_name,role) VALUES(?,?,?,?,?,'PATIENT')",id,email,passwords.encode(r.password()),r.firstName().trim(),r.lastName().trim());db.update("INSERT INTO patient_profiles(user_id) VALUES(?)",id);}catch(org.springframework.dao.DuplicateKeyException e){throw new ResponseStatusException(HttpStatus.CONFLICT,"Unable to create account with these details");}
  return tokens(email,"PATIENT",r.firstName()+" "+r.lastName(),id);
 }
 @PostMapping("/login") public TokenResponse login(@Valid @RequestBody Login r){String email=r.email().trim().toLowerCase();var rows=db.query("SELECT id,password_hash,role,first_name,last_name,active FROM users WHERE email=?",(rs,n)->new Object[]{rs.getObject("id",UUID.class),rs.getString("password_hash"),rs.getString("role"),rs.getString("first_name"),rs.getString("last_name"),rs.getBoolean("active")},email);if(rows.isEmpty()||!Boolean.TRUE.equals(rows.get(0)[5])||!passwords.matches(r.password(),(String)rows.get(0)[1]))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Email or password is incorrect");return tokens(email,(String)rows.get(0)[2],rows.get(0)[3]+" "+rows.get(0)[4],(UUID)rows.get(0)[0]);}
 @PostMapping("/refresh") public TokenResponse refresh(@RequestBody Map<String,String> body){String raw=body.get("refreshToken");if(raw==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh token");String hash=hash(raw);var matches=db.query("SELECT rt.id,rt.user_id,u.email,u.role,u.first_name,u.last_name FROM refresh_tokens rt JOIN users u ON u.id=rt.user_id WHERE rt.token_hash=? AND rt.revoked_at IS NULL AND rt.expires_at>now()",(rs,n)->new Object[]{rs.getObject(1,UUID.class),rs.getObject(2,UUID.class),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)},hash);if(matches.isEmpty())throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh token");var m=matches.get(0);db.update("UPDATE refresh_tokens SET revoked_at=now() WHERE id=?",m[0]);return tokens((String)m[2],(String)m[3],m[4]+" "+m[5],(UUID)m[1]);}
 @PostMapping("/logout") public void logout(@RequestBody Map<String,String> body){String token=body.get("refreshToken");if(token!=null)db.update("UPDATE refresh_tokens SET revoked_at=now() WHERE token_hash=? AND revoked_at IS NULL",hash(token));}
 @GetMapping("/me") public Map<String,Object> me(org.springframework.security.core.Authentication auth){return db.queryForMap("SELECT id,email,first_name AS \"firstName\",last_name AS \"lastName\",role FROM users WHERE email=? AND active=TRUE",auth.getName());}
 private TokenResponse tokens(String email,String role,String name,UUID id){byte[] bytes=new byte[48];random.nextBytes(bytes);String refresh=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);db.update("INSERT INTO refresh_tokens(user_id,token_hash,expires_at) VALUES(?,?,?)",id,hash(refresh),OffsetDateTime.now().plusDays(14));return new TokenResponse(jwt.issue(email,role),refresh,"Bearer",role,name);}
 private String hash(String token){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
}
