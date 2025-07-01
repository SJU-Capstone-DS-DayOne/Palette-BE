package kr.ac.sejong.ds.palette.jwt.repository;

import kr.ac.sejong.ds.palette.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JwtRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
