package kr.ac.sejong.ds.palette.member.dto.response;

import kr.ac.sejong.ds.palette.member.entity.Member;

public record MemberLoginResponse(
        Long id
) {

    public static MemberLoginResponse of(Member member) {
        return new MemberLoginResponse(member.getId());
    }
}
