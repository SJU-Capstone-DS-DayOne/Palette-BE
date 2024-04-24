package kr.ac.sejong.ds.palette.couple.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoupleCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couple_code_id")
    private Long id;

    @NotNull
    private Integer coupleCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CoupleCode(Integer coupleCode) {
        this.coupleCode = coupleCode;
    }

    // 랜덤 코드 생성은 ThreadLocalRandom 사용 예정
    // 이미 코드가 존재하는지 확인하고 생성하는 로직 필요 (있다면 해당 코드 반환)
    // 이미 커플인 경우는 생성하지 X
    // 나중에 커플 연결이 이루어지면 존재하는 연인 코드 서로 삭제
}
