package kr.ac.sejong.ds.palette.common.exception.member;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class NoPreferenceMemberException extends ApplicationException {

    public NoPreferenceMemberException() {
        super(ExceptionType.NO_PREFERENCE_MEMBER.getHttpStatus(), ExceptionType.NO_PREFERENCE_MEMBER.getDetail());
    }
}