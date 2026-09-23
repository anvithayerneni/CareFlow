package com.careflow.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/** Per-instance limit for the single-instance free demo deployment. */
@Component
@Profile("free")
public class FreeAuthRateLimitFilter extends OncePerRequestFilter implements AuthRequestFilter {
    private record Window(long startedAtMillis, int requests) {}
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean authRequest = request.getMethod().equals("POST") &&
                (path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/register") ||
                        path.equals("/api/v1/auth/refresh"));
        if (authRequest) {
            String key = (request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr()) + ":" + path;
            long now = System.currentTimeMillis();
            Window window = windows.compute(key, (ignored, current) -> current == null || now - current.startedAtMillis() >= 60_000
                    ? new Window(now, 1)
                    : new Window(current.startedAtMillis(), current.requests() + 1));
            if (window.requests() > 10) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many authentication attempts. Try again soon.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
