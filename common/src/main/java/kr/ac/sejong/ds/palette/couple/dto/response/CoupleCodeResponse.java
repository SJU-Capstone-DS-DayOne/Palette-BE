package kr.ac.sejong.ds.palette.couple.dto.response;

import kr.ac.sejong.ds.palette.couple.entity.CoupleCode;

public record CoupleCodeResponse(
        Integer code
) {

    public static CoupleCodeResponse of(CoupleCode coupleCode) {
        return new CoupleCodeResponse(coupleCode.getCode());
    }
}
