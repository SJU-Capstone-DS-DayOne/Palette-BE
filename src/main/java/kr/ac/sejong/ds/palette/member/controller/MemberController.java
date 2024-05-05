package kr.ac.sejong.ds.palette.member.controller;

import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody MemberJoinRequest memberJoinRequest){

        Long joinedId = memberService.join(memberJoinRequest);
        MemberJoinResponse memberJoinResponse = new MemberJoinResponse(joinedId);

        return ResponseEntity.ok().body(memberJoinResponse);
    }
}
