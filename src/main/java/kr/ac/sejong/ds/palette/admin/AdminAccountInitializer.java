package kr.ac.sejong.ds.palette.admin;

import kr.ac.sejong.ds.palette.common.exception.member.DuplicatedEmailException;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.entity.Role;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${admin.default.username}")
    private String defaultUsername;

    @Value("${admin.default.password}")
    private String defaultPassword;

    @Override
    public void run(String... args) {

        if (!memberRepository.existsByEmail(defaultUsername)) {

            Member admin = new Member(
                    defaultUsername,
                    bCryptPasswordEncoder.encode(defaultPassword),
                    "super_admin",
                    Gender.MALE,
                    "20000101",
                    "01000000000",
                    Role.ROLE_ADMIN
            );

            memberRepository.save(admin);
            System.out.println("관리자 계정 생성됨: " + defaultUsername);
        }
    }
}
