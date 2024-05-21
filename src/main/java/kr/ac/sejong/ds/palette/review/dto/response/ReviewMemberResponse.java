package kr.ac.sejong.ds.palette.review.dto.response;

import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;

public record ReviewMemberResponse(
        Long memberId,
        String email,
        String nickname,
        Gender gender
) {
    public static ReviewMemberResponse of(Member member) {
        return new ReviewMemberResponse(member.getId(), member.getEmail(), member.getNickname(), member.getGender());
    }
}
