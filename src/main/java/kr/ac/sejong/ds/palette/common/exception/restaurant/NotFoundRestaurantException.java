package kr.ac.sejong.ds.palette.common.exception.restaurant;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundRestaurantException extends ApplicationException {

    public NotFoundRestaurantException() {
        super(ExceptionType.NOT_FOUND_RESTAURANT.getHttpStatus(), ExceptionType.NOT_FOUND_RESTAURANT.getDetail());
    }
}