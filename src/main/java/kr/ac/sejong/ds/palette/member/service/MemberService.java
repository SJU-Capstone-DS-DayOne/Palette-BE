package kr.ac.sejong.ds.palette.member.service;

import kr.ac.sejong.ds.palette.common.exception.BadRequestException;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ac.sejong.ds.palette.common.exception.ExceptionCode.DUPLICATED_MEMBER_EMAIL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(MemberJoinRequest memberJoinRequest) {
        String email = memberJoinRequest.getEmail();
        String password = bCryptPasswordEncoder.encode(
                memberJoinRequest.getPassword()
        );
        String nickname = memberJoinRequest.getNickname();
        Gender gender = memberJoinRequest.getGender();


        if(memberRepository.existsByEmail(email)){
            throw new BadRequestException(DUPLICATED_MEMBER_EMAIL);
        }

        Member member = new Member(email, password, nickname, gender);
        memberRepository.save(member);

        return member.getId();
    }
}
