package com.hufs.AnswerDev.Configuration;

import com.hufs.AnswerDev.Util.Filter.JwtAuthenticationFilter;
import com.hufs.AnswerDev.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf disable
                .httpBasic(AbstractHttpConfigurer::disable) // basic 인가 방식 disable
                .formLogin(AbstractHttpConfigurer::disable) // 기존 로그인 폼 disable

                // stateless한 세션
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // URL 별로 권한을 설정
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            .requestMatchers( "/user/login", "/user/login").permitAll()
                            .anyRequest().authenticated();
                    // requestMatchers("/user").hasRole("USER") // USER 권한이 있는 유저만 접근 가능
                })

                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
