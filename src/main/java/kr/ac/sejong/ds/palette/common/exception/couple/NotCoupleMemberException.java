package kr.ac.sejong.ds.palette.common.exception.couple;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotCoupleMemberException extends ApplicationException {

    public NotCoupleMemberException() {
        super(ExceptionType.NOT_COUPLE_MEMBER.getHttpStatus(), ExceptionType.NOT_COUPLE_MEMBER.getDetail());
    }
}