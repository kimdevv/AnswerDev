package com.hufs.AnswerDev.Util.Filter;

import com.hufs.AnswerDev.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), "유효하지 않은 토큰입니다.");
        }
        String accessToken = bearerToken.substring(7);

        int userId = jwtUtil.validateToken(accessToken);
        try {
            Authentication authentication = jwtUtil.getAuthentication(userId).get();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/user/login")
                || path.startsWith("/user/signup")
                || path.startsWith("/answer/register")
                || path.startsWith("/index.html")
                || path.startsWith("/login.html")
                || path.startsWith("/signup.html")
                || path.startsWith("/style.css")
                || path.startsWith("/favicon.ico");
    }
}
