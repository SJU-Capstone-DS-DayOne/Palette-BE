package kr.ac.sejong.ds.palette.common.exception.restaurant;

import kr.ac.sejong.ds.palette.common.exception.ApplicationException;
import kr.ac.sejong.ds.palette.common.exception.ExceptionType;

public class FailToSaveRestaurantPreferenceException extends ApplicationException {

    public FailToSaveRestaurantPreferenceException(){
        super(ExceptionType.FAIL_TO_SAVE_RESTAURANT_PREFERENCE.getHttpStatus(), ExceptionType.FAIL_TO_SAVE_RESTAURANT_PREFERENCE.getDetail());
    }
}
