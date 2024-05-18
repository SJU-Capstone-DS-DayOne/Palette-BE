package kr.ac.sejong.ds.palette.couple.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.couple.dto.request.CoupleConnectRequest;
import kr.ac.sejong.ds.palette.couple.dto.response.CoupleCodeResponse;
import kr.ac.sejong.ds.palette.couple.dto.response.IsCoupleResponse;
import kr.ac.sejong.ds.palette.couple.service.CoupleService;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "커플 (Couple)")
public class CoupleController {
    private final CoupleService coupleService;

    @GetMapping("/couple-codes")
    @Operation(summary = "커플 코드 조회 (없을 시 생성)")
    public ResponseEntity<CoupleCodeResponse> getCoupleCode(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        CoupleCodeResponse coupleCodeResponse = coupleService.getCoupleCode(memberId);
        return ResponseEntity.ok().body(coupleCodeResponse);
    }

    @GetMapping("/couples")
    @Operation(summary = "커플 여부 확인")
    public ResponseEntity<IsCoupleResponse> checkCouple(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        IsCoupleResponse isCoupleResponse = coupleService.checkCouple(memberId);
        return ResponseEntity.ok().body(isCoupleResponse);
    }

    @PostMapping("/couples")
    @Operation(summary = "커플 연결")
    public ResponseEntity<Void> connectCouple(Authentication authentication, @RequestBody @Valid CoupleConnectRequest coupleConnectRequest){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        coupleService.createCouple(memberId, coupleConnectRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/couples")
    @Operation(summary = "커플 연결 해제")
    public ResponseEntity<Void> disconnectCouple(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        coupleService.deleteCouple(memberId);
        return ResponseEntity.ok().build();
    }
}
