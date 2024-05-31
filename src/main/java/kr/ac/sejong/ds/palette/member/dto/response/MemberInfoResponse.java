package kr.ac.sejong.ds.palette.member.dto.response;

import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;

public record MemberInfoResponse(
        Long memberId,
        String email,
        String nickname,
        Gender gender,
        String birthOfDate,
        String phone,
        Boolean preferenceYn
) {

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getId(), member.getEmail(), member.getNickname(), member.getGender(), member.getBirthOfDate(), member.getPhone(), member.isPreferenceYn()
        );
    }
}