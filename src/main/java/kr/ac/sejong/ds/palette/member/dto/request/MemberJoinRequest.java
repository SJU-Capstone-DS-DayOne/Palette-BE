package kr.ac.sejong.ds.palette.member.dto.request;

import kr.ac.sejong.ds.palette.member.entity.Gender;
import lombok.Getter;

@Getter
public class MemberJoinRequest {

    private String email;

    private String password;

    private String nickname;

    private Gender gender;
}
