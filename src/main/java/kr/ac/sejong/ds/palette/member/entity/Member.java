package kr.ac.sejong.ds.palette.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String nickname;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    public Member(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }
}
