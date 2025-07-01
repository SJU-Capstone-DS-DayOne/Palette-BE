package kr.ac.sejong.ds.palette.common.exception.member;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class DuplicatedEmailException extends ApplicationException {
    
    public DuplicatedEmailException() {
        super(ExceptionType.DUPLICATED_EMAIL.getHttpStatus(), ExceptionType.DUPLICATED_EMAIL.getDetail());
    }
}