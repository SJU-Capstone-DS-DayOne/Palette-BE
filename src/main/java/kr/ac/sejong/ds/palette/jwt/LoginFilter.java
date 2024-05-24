package kr.ac.sejong.ds.palette.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.sejong.ds.palette.jwt.service.JwtService;
import kr.ac.sejong.ds.palette.jwt.util.JWTUtil;
import kr.ac.sejong.ds.palette.jwt.dto.MemberLoginRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberLoginResponse;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static kr.ac.sejong.ds.palette.jwt.util.JWTUtil.*;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 email, password 추출
        MemberLoginRequest memberLoginRequest;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            memberLoginRequest = objectMapper.readValue(messageBody, MemberLoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 클라이언트 요청에서 email, password 추출
        String email = memberLoginRequest.getEmail();
        String password = memberLoginRequest.getPassword();

        System.out.println(email);

        // 스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 유저 정보
        Member member = memberRepository.findByEmail(authentication.getName()).get();
        Long memberId = member.getId();
        String email = member.getEmail();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", memberId, email, ACCESS_TOKEN_EXP_MS);  // 1시간
        String refresh = jwtUtil.createJwt("refresh", memberId, email, REFRESH_TOKEN_EXP_MS);  // 24시간

        // Refresh 토큰 저장
        jwtService.addRefreshToken(email, refresh, REFRESH_TOKEN_EXP_MS);

        // 응답 설정
        response.setHeader("access", access);
        response.setHeader("Set-Cookie", createCookie("refresh", refresh).toString());
        response.setStatus(HttpStatus.OK.value());

        // 유저 ID 응답
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            String result = objectMapper.writeValueAsString(MemberLoginResponse.of(member));
            response.getWriter().write(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // 로그인 실패시 401 응답 코드와 내용 반환
        response.setCharacterEncoding("utf-8");
        response.setStatus(401);
        try {
            PrintWriter writer = response.getWriter();
            writer.write("로그인 인증에 실패하였습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
