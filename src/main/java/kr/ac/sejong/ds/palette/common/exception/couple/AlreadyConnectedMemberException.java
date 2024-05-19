package kr.ac.sejong.ds.palette.common.exception.couple;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class AlreadyConnectedMemberException extends ApplicationException {

    public AlreadyConnectedMemberException() {
        super(ExceptionType.ALREADY_CONNECTED_MEMBER.getHttpStatus(), ExceptionType.ALREADY_CONNECTED_MEMBER.getDetail());
    }
}