package kr.ac.sejong.ds.palette.review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReviewUpdateRequest(
        @NotBlank String content
) {
}
