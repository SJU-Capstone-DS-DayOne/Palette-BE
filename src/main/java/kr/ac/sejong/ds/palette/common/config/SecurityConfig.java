package kr.ac.sejong.ds.palette.common.config;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.sejong.ds.palette.jwt.CustomLogoutFilter;
import kr.ac.sejong.ds.palette.jwt.JWTFilter;
import kr.ac.sejong.ds.palette.jwt.LoginFilter;
import kr.ac.sejong.ds.palette.jwt.repository.JwtRepository;
import kr.ac.sejong.ds.palette.jwt.service.JwtService;
import kr.ac.sejong.ds.palette.jwt.util.JWTUtil;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;
    private final MemberRepository memberRepository;

    // SecurityFilterChain을 반환하고 Bean으로 등록함으로써 컴포넌트 기반의 보안 설정이 가능
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // CORS 설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList("https://localhost:5173", "https://palette.kr"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Arrays.asList("access"));

                        return configuration;
                    }
                })));

        // csrf disable - jwt 방식은 세션을 stateless 상태로 관리
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join", "/join/restaurant-candidates", "/reissue", "/restaurants",
                                "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());

        // JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, jwtService, memberRepository), UsernamePasswordAuthenticationFilter.class);

        // CustomLogoutFilter 등록
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, jwtRepository), LogoutFilter.class);

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}