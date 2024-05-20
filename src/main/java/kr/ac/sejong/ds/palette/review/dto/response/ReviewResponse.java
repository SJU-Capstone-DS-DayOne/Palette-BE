package kr.ac.sejong.ds.palette.review.dto.response;

import kr.ac.sejong.ds.palette.review.entity.Review;

public record ReviewResponse(
        Long reviewId,
        String content,
        ReviewMemberResponse member
) {

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review.getId(), review.getContent(), ReviewMemberResponse.of(review.getMember()));
    }
}
