package kr.ac.sejong.ds.palette.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
        @NotBlank String nickname,
        @NotBlank String birthOfDate,
        @NotBlank String phone
) {
}