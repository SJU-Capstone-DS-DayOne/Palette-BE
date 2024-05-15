package kr.ac.sejong.ds.palette.couple.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoupleConnectRequest(
        @NotNull(message = "연인의 코드가 필요합니다.") Integer loverCode
) {
}
