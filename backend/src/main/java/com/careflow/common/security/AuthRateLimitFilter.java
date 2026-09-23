package com.careflow.common.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
@Component
public class AuthRateLimitFilter extends OncePerRequestFilter {
 private final StringRedisTemplate redis;public AuthRateLimitFilter(StringRedisTemplate redis){this.redis=redis;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String path=req.getRequestURI();if(req.getMethod().equals("POST")&&(path.equals("/api/v1/auth/login")||path.equals("/api/v1/auth/register")||path.equals("/api/v1/auth/refresh"))){String ip=req.getRemoteAddr()==null?"unknown":req.getRemoteAddr();try{String key="careflow:auth-rate:"+ip+":"+path;Long count=redis.opsForValue().increment(key);if(count!=null&&count==1)redis.expire(key,Duration.ofMinutes(1));if(count!=null&&count>10){res.setStatus(429);res.setContentType("application/json");res.getWriter().write("{\"error\":\"Too many authentication attempts. Try again soon.\"}");return;}}catch(Exception ignored){/* The database and BCrypt limits remain available if local Redis is temporarily offline. */}}chain.doFilter(req,res);}
}
