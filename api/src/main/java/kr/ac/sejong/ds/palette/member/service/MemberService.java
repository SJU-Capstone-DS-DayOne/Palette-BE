package kr.ac.sejong.ds.palette.member.service;

import kr.ac.sejong.ds.palette.common.exception.member.DuplicatedEmailException;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.couple.repository.CoupleCodeRepository;
import kr.ac.sejong.ds.palette.couple.repository.CoupleRepository;
import kr.ac.sejong.ds.palette.datecourse.repository.DateCourseRepository;
import kr.ac.sejong.ds.palette.member.dto.request.MemberJoinRequest;
import kr.ac.sejong.ds.palette.member.dto.request.MemberUpdateRequest;
import kr.ac.sejong.ds.palette.member.dto.response.MemberJoinResponse;
import kr.ac.sejong.ds.palette.member.dto.response.MemberInfoResponse;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.entity.Role;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final CoupleCodeRepository coupleCodeRepository;
    private final DateCourseRepository dateCourseRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest) {
        String email = memberJoinRequest.email();
        String password = bCryptPasswordEncoder.encode(
                memberJoinRequest.password()
        );
        String nickname = memberJoinRequest.nickname();
        Gender gender = memberJoinRequest.gender();
        String birthOfDate = memberJoinRequest.birthOfDate();
        String phone = memberJoinRequest.phone();


        if(memberRepository.existsByEmail(email)){
            throw new DuplicatedEmailException();
        }

        Member member = new Member(email, password, nickname, gender, birthOfDate, phone, Role.ROLE_USER);
        memberRepository.save(member);

        return MemberJoinResponse.of(member);
    }

    public MemberInfoResponse getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        return MemberInfoResponse.of(member);
    }

    @Transactional
    public void updateMemberInfo(Long memberId, final MemberUpdateRequest memberUpdateRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        member.updateMemberInfo(memberUpdateRequest);
    }

    @Transactional
    public void deleteMember(Long memberId){

        if (!memberRepository.existsById(memberId))
            throw new NotFoundMemberException();

        if (coupleRepository.existsByMaleIdOrFemaleId(memberId, memberId)) {  // 커플 정보가 존재하는 경우
            Couple couple = coupleRepository.findByMaleIdOrFemaleId(memberId, memberId).get();
            dateCourseRepository.deleteAllByCoupleId(couple.getId());  // 커플 데이트 코스 삭제
            coupleRepository.delete(couple);  // 커플 정보 삭제
        }
        coupleCodeRepository.deleteByMemberId(memberId);  // 연결 코드 삭제
        reviewRepository.deleteAllByMemberId(memberId);  // 리뷰 삭제
        memberRepository.deleteById(memberId);  // 멤버 삭제
    }
}