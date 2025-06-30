package kr.ac.sejong.ds.palette.common.exception.restaurant;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class DuplicateRestaurantException extends ApplicationException {

    public DuplicateRestaurantException() {
        super(ExceptionType.DUPLICATED_RESTAURANT.getHttpStatus(), ExceptionType.DUPLICATED_RESTAURANT.getDetail());
    }
}