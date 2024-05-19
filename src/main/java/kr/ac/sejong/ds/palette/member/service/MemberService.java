package kr.ac.sejong.ds.palette.member.service;

import kr.ac.sejong.ds.palette.common.exception.member.DuplicatedEmailException;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberNicknameUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberInfoResponse;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new DuplicatedEmailException();
        }

        Member member = new Member(email, password, nickname, gender);
        memberRepository.save(member);

        return MemberJoinResponse.of(member);
    }

    public MemberInfoResponse getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        return MemberInfoResponse.of(member);
    }

    @Transactional
    public void updateNickname(Long memberId, final MemberNicknameUpdateRequest memberNicknameUpdateRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        member.updateNickname(memberNicknameUpdateRequest);
    }

    @Transactional
    public void deleteMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        memberRepository.delete(member);
    }
}
