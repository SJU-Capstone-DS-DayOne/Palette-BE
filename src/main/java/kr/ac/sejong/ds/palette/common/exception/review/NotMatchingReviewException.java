package kr.ac.sejong.ds.palette.common.exception.review;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;

import static kr.ac.sejong.ds.palette.common.exception.ExceptionType.NOT_MATCHING_REVIEW;

public class NotMatchingReviewException extends ApplicationException {
    public NotMatchingReviewException() {
        super(NOT_MATCHING_REVIEW.getHttpStatus(), NOT_MATCHING_REVIEW.getDetail());
    }
}
