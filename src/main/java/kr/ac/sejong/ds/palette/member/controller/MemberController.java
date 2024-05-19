package kr.ac.sejong.ds.palette.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberNicknameUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberInfoResponse;
import kr.ac.sejong.ds.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 (Member)")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입")
    @PostMapping("/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody @Valid MemberJoinRequest memberJoinRequest){
        MemberJoinResponse memberJoinResponse = memberService.join(memberJoinRequest);
        return ResponseEntity.ok().body(memberJoinResponse);
    }

    @Operation(summary = "단일 회원 정보 조회")
    @GetMapping("/members")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @Operation(summary = "회원 닉네임 수정")
    @PutMapping("/members")
    public ResponseEntity<Void> updateMemberNickname(Authentication authentication, @RequestBody @Valid MemberNicknameUpdateRequest memberNicknameUpdateRequest){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        memberService.updateNickname(memberId, memberNicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/members")
    public ResponseEntity<Void> deleteMember(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
