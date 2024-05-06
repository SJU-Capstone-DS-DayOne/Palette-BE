package kr.ac.sejong.ds.palette.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.jwt.util.JWTUtil;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            // 이후 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");
        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);  // 만료 시 예외 발생
        } catch (ExpiredJwtException e) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            // 이후 메소드 종료 (필수)
            return;
        }

        // 토큰에서 email 획득
        String email = jwtUtil.getEmail(token);

        // Member를 생성하여 값 설정
        Member member = new Member(email, "tmp", "tmp", Gender.MALE);
        System.out.println(email);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
