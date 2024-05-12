package kr.ac.sejong.ds.palette.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberNicknameUpdateRequest(
        @NotBlank String nickname
) {
}
