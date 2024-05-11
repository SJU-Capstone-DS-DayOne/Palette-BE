package kr.ac.sejong.ds.palette.jwt.util;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    public static final Long ACCESS_TOKEN_EXP_MS = 60*60*1000L;  // 1시간
    public static final Long REFRESH_TOKEN_EXP_MS = 24*60*60*1000L;  // 24시간

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String category, String email, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public static ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path("/")  // 쿠키 적용 범위
                .sameSite("None")  // SameSite 정책
                .httpOnly(true)  // 클라이언트 단에서 JS로 쿠키에 접근하지 못하도록 함 (보안)
                .secure(true)  // https일 경우 적용 가능
                .maxAge(REFRESH_TOKEN_EXP_MS.intValue() / 1000)  // 생명주기 24시간
                .build();
    }
}
