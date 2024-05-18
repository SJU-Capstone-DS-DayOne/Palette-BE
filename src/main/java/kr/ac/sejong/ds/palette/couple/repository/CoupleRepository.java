package kr.ac.sejong.ds.palette.couple.repository;

import kr.ac.sejong.ds.palette.couple.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Long> {
    @Query("SELECT c FROM Couple c WHERE c.male.id = :memberId OR c.female.id = :memberId")
    Optional<Couple> findByMaleIdOrFemaleId(Long memberId);

    boolean existsByMaleId(Long memberId);
    boolean existsByFemaleId(Long memberId);
}
