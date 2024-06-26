package kr.ac.sejong.ds.palette.jwt.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.sejong.ds.palette.jwt.service.JwtService;
import kr.ac.sejong.ds.palette.jwt.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class JwtController {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {  // 인자는 스프링 컨테이너가 자동으로 주입해주는 값
        return jwtService.reissue(request, response);
    }
}