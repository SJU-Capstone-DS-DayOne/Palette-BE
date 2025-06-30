package kr.ac.sejong.ds.palette.common.exception.couple;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class InvalidGenderForCoupleException extends ApplicationException {

    public InvalidGenderForCoupleException() {
        super(ExceptionType.INVALID_GENDER_FOR_COUPLE.getHttpStatus(), ExceptionType.INVALID_GENDER_FOR_COUPLE.getDetail());
    }
}