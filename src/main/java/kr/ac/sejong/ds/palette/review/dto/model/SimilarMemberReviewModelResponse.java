package kr.ac.sejong.ds.palette.review.dto.model;

import java.util.List;

public record SimilarMemberReviewModelResponse(
        List<Long> review_ids
) {
}
