package kr.ac.sejong.ds.palette.couple.controller;

import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.couple.dto.request.CoupleConnectRequest;
import kr.ac.sejong.ds.palette.couple.dto.response.CoupleCodeResponse;
import kr.ac.sejong.ds.palette.couple.dto.response.IsCoupleResponse;
import kr.ac.sejong.ds.palette.couple.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CoupleController {
    private final CoupleService coupleService;

    @GetMapping("/members/{memberId}/couple-codes")
    public ResponseEntity<CoupleCodeResponse> getCoupleCode(@PathVariable(name = "memberId") Long memberId){
        CoupleCodeResponse coupleCodeResponse = coupleService.getCoupleCode(memberId);
        return ResponseEntity.ok().body(coupleCodeResponse);
    }

    @GetMapping("/members/{memberId}/couples")
    public ResponseEntity<IsCoupleResponse> checkCouple(@PathVariable(name = "memberId") Long memberId){
        IsCoupleResponse isCoupleResponse = coupleService.checkCouple(memberId);
        return ResponseEntity.ok().body(isCoupleResponse);
    }

    @PostMapping("/members/{memberId}/couples")
    public ResponseEntity<Void> connectCouple(@PathVariable(name = "memberId") Long memberId, @RequestBody @Valid CoupleConnectRequest coupleConnectRequest){
        coupleService.createCouple(memberId, coupleConnectRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{memberId}/couples")
    public ResponseEntity<Void> disconnectCouple(@PathVariable(name = "memberId") Long memberId){
        coupleService.deleteCouple(memberId);
        return ResponseEntity.ok().build();
    }
}
