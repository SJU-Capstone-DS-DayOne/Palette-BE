package kr.ac.sejong.ds.palette.jwt.service;

import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 조회
        Optional<Member> member = memberRepository.findByEmail(username);

        if (member.isPresent()) {
            // UserDetails에 담아서 return하면 AutneticationManager가 검증
            return new CustomUserDetails(member.get());
        }
        return null;
    }
}