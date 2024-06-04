package kr.ac.sejong.ds.palette.review.dto.response;

import kr.ac.sejong.ds.palette.review.entity.Review;

public record ReviewContentResponse(
        String content
) {
    public static ReviewContentResponse of(Review review) {
        return new ReviewContentResponse(review.getContent());
    }
}
