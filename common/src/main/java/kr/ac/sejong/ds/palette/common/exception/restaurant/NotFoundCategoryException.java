package kr.ac.sejong.ds.palette.common.exception.restaurant;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundCategoryException extends ApplicationException {

    public NotFoundCategoryException() {
        super(ExceptionType.NOT_FOUND_CATEGORY.getHttpStatus(), ExceptionType.NOT_FOUND_CATEGORY.getDetail());
    }
}