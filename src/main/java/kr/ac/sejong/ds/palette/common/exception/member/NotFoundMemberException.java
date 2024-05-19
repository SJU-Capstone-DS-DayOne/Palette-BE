package kr.ac.sejong.ds.palette.common.exception.member;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NotFoundMemberException extends ApplicationException {

    public NotFoundMemberException() {
        super(ExceptionType.NOT_FOUND_MEMBER.getHttpStatus(), ExceptionType.NOT_FOUND_MEMBER.getDetail());
    }
}