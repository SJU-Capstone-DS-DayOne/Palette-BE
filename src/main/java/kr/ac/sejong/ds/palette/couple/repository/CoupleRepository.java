package kr.ac.sejong.ds.palette.couple.repository;

import kr.ac.sejong.ds.palette.couple.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Long> {
    Optional<Couple> findByMaleIdOrFemaleId(Long memberId1, Long memberId2);
    Optional<Couple> findByMaleId(Long memberId);
    Optional<Couple> findByFemaleId(Long memberId);

    boolean existsByMaleIdOrFemaleId(Long memberId1, Long memberId2);
    boolean existsByMaleId(Long memberId);
    boolean existsByFemaleId(Long memberId);
}
