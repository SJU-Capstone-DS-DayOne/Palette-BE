package kr.ac.sejong.ds.palette.common.exception.infra.message;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class FailToPublishMessage extends ApplicationException {

    public FailToPublishMessage() {
        super(ExceptionType.FAIL_TO_PUBLISH_MESSAGE.getHttpStatus(), ExceptionType.FAIL_TO_PUBLISH_MESSAGE.getDetail());
    }
}