package kr.ac.sejong.ds.palette.member.service;

import kr.ac.sejong.ds.palette.common.exception.BadRequestException;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberNicknameUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberResponse;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ac.sejong.ds.palette.common.exception.ExceptionCode.DUPLICATED_MEMBER_EMAIL;
import static kr.ac.sejong.ds.palette.common.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest) {
        String email = memberJoinRequest.email();
        String password = bCryptPasswordEncoder.encode(
                memberJoinRequest.password()
        );
        String nickname = memberJoinRequest.nickname();
        Gender gender = memberJoinRequest.gender();


        if(memberRepository.existsByEmail(email)){
            throw new BadRequestException(DUPLICATED_MEMBER_EMAIL);
        }

        Member member = new Member(email, password, nickname, gender);
        memberRepository.save(member);

        return MemberJoinResponse.of(member);
    }

    public MemberResponse getMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateNickname(Long memberId, final MemberNicknameUpdateRequest memberNicknameUpdateRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        member.updateNickname(memberNicknameUpdateRequest);
    }

    @Transactional
    public void deleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        memberRepository.delete(member);
    }
}
