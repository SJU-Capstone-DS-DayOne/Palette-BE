package kr.ac.sejong.ds.palette.common.exception.review;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundReviewException extends ApplicationException {
    public NotFoundReviewException() {
        super(ExceptionType.NOT_FOUND_REVIEW.getHttpStatus(), ExceptionType.NOT_FOUND_REVIEW.getDetail());
    }
}
