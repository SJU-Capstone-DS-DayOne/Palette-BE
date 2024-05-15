package kr.ac.sejong.ds.palette.couple.repository;

import kr.ac.sejong.ds.palette.couple.entity.CoupleCode;
import kr.ac.sejong.ds.palette.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoupleCodeRepository extends JpaRepository<CoupleCode, Long> {
    Optional<CoupleCode> findByMemberId(Long memberId);
    Optional<CoupleCode> findByCodeAndMemberIdNot(Integer code, Long memberId);
    boolean existsByCode(Integer code);
    void deleteByMemberId(Long memberId);
}
