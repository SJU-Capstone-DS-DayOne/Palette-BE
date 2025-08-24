package kr.ac.sejong.ds.palette.member.repository;

import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.entity.PreferenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.preferenceStatus = :newStatus " +
            "WHERE m.updatedAt < :threshold AND m.preferenceStatus = :currentStatus")
    int updatePreferenceStatusBefore(@Param("newStatus") PreferenceStatus newStatus,
                           @Param("currentStatus") PreferenceStatus currentStatus,
                           @Param("threshold") LocalDateTime threshold);
}
