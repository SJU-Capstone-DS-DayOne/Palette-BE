package kr.ac.sejong.ds.palette.member.controller;

import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberNicknameUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberResponse;
import kr.ac.sejong.ds.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public void hello(){
        System.out.println("HI");
    }

    @PostMapping("/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody @Valid MemberJoinRequest memberJoinRequest){
        MemberJoinResponse memberJoinResponse = memberService.join(memberJoinRequest);
        return ResponseEntity.ok().body(memberJoinResponse);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(name = "memberId") Long memberId){
        MemberResponse memberResponse = memberService.getMember(memberId);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<Void> updateMemberNickname(@PathVariable(name = "memberId") Long memberId, @RequestBody @Valid MemberNicknameUpdateRequest memberNicknameUpdateRequest){
        memberService.updateNickname(memberId, memberNicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
