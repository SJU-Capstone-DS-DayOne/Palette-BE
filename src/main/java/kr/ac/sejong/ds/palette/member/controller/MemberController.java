package kr.ac.sejong.ds.palette.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberNicknameUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberInfoResponse;
import kr.ac.sejong.ds.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@PathVariable(name = "memberId") Long memberId){
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @Operation(summary = "회원 닉네임 수정")
    @PutMapping("/members/{memberId}")
    public ResponseEntity<Void> updateMemberNickname(@PathVariable(name = "memberId") Long memberId, @RequestBody @Valid MemberNicknameUpdateRequest memberNicknameUpdateRequest){
        memberService.updateNickname(memberId, memberNicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "memberId") Long memberId){
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().build();
    }
}
