package kr.ac.sejong.ds.palette.common.exception.datecourse;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundDateCourseRestaurantException extends ApplicationException {

    public NotFoundDateCourseRestaurantException() {
        super(ExceptionType.NOT_FOUND_DATE_COURSE_RESTAURANT.getHttpStatus(), ExceptionType.NOT_FOUND_DATE_COURSE_RESTAURANT.getDetail());
    }
}