package kr.ac.sejong.ds.palette.member.dto.response;

import kr.ac.sejong.ds.palette.member.entity.Member;

public record MemberJoinResponse(
        Long id
) {

    public static MemberJoinResponse of(Member member) {
        return new MemberJoinResponse(member.getId());
    }
}
