package kr.ac.sejong.ds.palette.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.ac.sejong.ds.palette.member.entity.Gender;

public record MemberJoinRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nickname,
        Gender gender
) {

}
