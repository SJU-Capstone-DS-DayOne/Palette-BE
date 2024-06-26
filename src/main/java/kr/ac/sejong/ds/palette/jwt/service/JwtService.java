package kr.ac.sejong.ds.palette.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.sejong.ds.palette.jwt.repository.JwtRepository;
import kr.ac.sejong.ds.palette.jwt.entity.RefreshToken;
import kr.ac.sejong.ds.palette.jwt.util.JWTUtil;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

import static kr.ac.sejong.ds.palette.jwt.util.JWTUtil.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JWTUtil jwtUtil;
    private final JwtRepository jwtRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // refresh token null check
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh token인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = jwtRepository.existsByRefreshToken(refresh);
        if (!isExist) {
            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }


        Member member = memberRepository.findByEmail(jwtUtil.getEmail(refresh)).get();
        Long memberId = member.getId();
        String email = member.getEmail();

        // make new JWT (access token)
        String newAccess = jwtUtil.createJwt("access", memberId, email, ACCESS_TOKEN_EXP_MS);
        String newRefresh = jwtUtil.createJwt("refresh", memberId, email, REFRESH_TOKEN_EXP_MS);

        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        jwtRepository.deleteByRefreshToken(refresh);
        addRefreshToken(email, newRefresh, REFRESH_TOKEN_EXP_MS);

        // response
        response.setHeader("access", newAccess);
        response.setHeader("Set-Cookie", createCookie("refresh", newRefresh).toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void addRefreshToken(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshToken = new RefreshToken(email, refresh, date.toString());

        jwtRepository.save(refreshToken);
    }
}
