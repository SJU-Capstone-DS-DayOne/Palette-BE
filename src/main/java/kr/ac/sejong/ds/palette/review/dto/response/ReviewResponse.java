package kr.ac.sejong.ds.palette.review.dto.response;

import kr.ac.sejong.ds.palette.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        String content,
        LocalDateTime created_at,
        ReviewMemberResponse member
) {

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review.getId(), review.getContent(), review.getCreatedAt(), ReviewMemberResponse.of(review.getMember()));
    }
}
