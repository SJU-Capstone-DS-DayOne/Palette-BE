package kr.ac.sejong.ds.palette.member.repository;

import kr.ac.sejong.ds.palette.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
