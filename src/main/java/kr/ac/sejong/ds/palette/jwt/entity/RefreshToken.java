package kr.ac.sejong.ds.palette.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String refreshToken;
    private String expiration;

    public RefreshToken(String email, String refreshToken, String expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}