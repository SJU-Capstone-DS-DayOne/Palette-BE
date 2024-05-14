package kr.ac.sejong.ds.palette.member.dto.response;

import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;

public record MemberResponse(
        Long id,
        String email,
        String nickname,
        Gender gender
        // 연인 멤버 닉네임 추가?
) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getNickname(), member.getGender());
    }
}