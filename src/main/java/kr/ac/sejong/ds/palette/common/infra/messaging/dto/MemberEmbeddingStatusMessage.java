package kr.ac.sejong.ds.palette.common.infra.messaging.dto;

public record MemberEmbeddingStatusMessage(
        Long memberId,
        boolean isSuccess
) {
}