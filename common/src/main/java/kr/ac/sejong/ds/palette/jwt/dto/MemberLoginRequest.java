package kr.ac.sejong.ds.palette.jwt.dto;

import lombok.Getter;

@Getter
public class MemberLoginRequest {

    private String email;

    private String password;
}
