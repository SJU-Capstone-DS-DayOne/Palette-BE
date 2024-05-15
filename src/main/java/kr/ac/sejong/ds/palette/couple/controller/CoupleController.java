package kr.ac.sejong.ds.palette.couple.controller;

import kr.ac.sejong.ds.palette.couple.dto.response.CoupleCodeResponse;
import kr.ac.sejong.ds.palette.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoupleController {
    private final CoupleService coupleService;

    @GetMapping("/members/{memberId}/couple-codes")
    public ResponseEntity<CoupleCodeResponse> getCoupleCode(@PathVariable(name = "memberId") Long memberId){
        CoupleCodeResponse coupleCodeResponse = coupleService.getCoupleCode(memberId);
        return ResponseEntity.ok().body(coupleCodeResponse);
    }

}
