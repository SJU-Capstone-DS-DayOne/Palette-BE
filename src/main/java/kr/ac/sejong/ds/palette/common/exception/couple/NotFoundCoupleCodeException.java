package kr.ac.sejong.ds.palette.common.exception.couple;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundCoupleCodeException extends ApplicationException {

    public NotFoundCoupleCodeException() {
        super(ExceptionType.NOT_FOUND_COUPLE_CODE.getHttpStatus(), ExceptionType.NOT_FOUND_COUPLE_CODE.getDetail());
    }
}