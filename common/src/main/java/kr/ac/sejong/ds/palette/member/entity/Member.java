package kr.ac.sejong.ds.palette.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.member.dto.request.MemberUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static kr.ac.sejong.ds.palette.member.entity.PreferenceStatus.INCOMPLETE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull  // DDL 생성 시에도 not null 적용됨
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @NotNull
    private String nickname;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @NotNull
    private String birthOfDate;

    @NotNull
    private String phone;

    @Setter
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PreferenceStatus preferenceStatus;

    public Member(String email, String password, String nickname, Gender gender, String birthOfDate, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
        this.phone = phone;
        this.preferenceStatus = INCOMPLETE;
        this.role = role;
    }

    // Authentication Token 생성을 위한 생성자
    public Member(Long id, String email, String password, String nickname, Gender gender, String birthOfDate, String phone, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
        this.phone = phone;
        this.preferenceStatus = INCOMPLETE;
        this.role = role;
    }

    public void updateMemberInfo(final MemberUpdateRequest memberUpdateRequest){
        this.nickname = memberUpdateRequest.nickname();
        this.birthOfDate = memberUpdateRequest.birthOfDate();
        this.phone = memberUpdateRequest.phone();
    }
}