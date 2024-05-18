package kr.ac.sejong.ds.palette.couple.service;

import kr.ac.sejong.ds.palette.common.exception.BadRequestException;
import kr.ac.sejong.ds.palette.couple.dto.request.CoupleConnectRequest;
import kr.ac.sejong.ds.palette.couple.dto.response.CoupleCodeResponse;
import kr.ac.sejong.ds.palette.couple.dto.response.IsCoupleResponse;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.couple.entity.CoupleCode;
import kr.ac.sejong.ds.palette.couple.repository.CoupleCodeRepository;
import kr.ac.sejong.ds.palette.couple.repository.CoupleRepository;
import kr.ac.sejong.ds.palette.member.entity.Gender;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static kr.ac.sejong.ds.palette.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleService {
    private final MemberRepository memberRepository;
    private final CoupleCodeRepository coupleCodeRepository;
    private final CoupleRepository coupleRepository;

    @Transactional
    public CoupleCodeResponse getCoupleCode(Long memberId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 커플이라면 커플 코드 생성 요청이 오지 않으므로 커플 여부 확인 필요 없음

        // 해당 멤버의 커플 코드 조회
        Optional<CoupleCode> coupleCode = coupleCodeRepository.findByMemberId(memberId);

        // 커플 코드가 이미 존재하는 경우 해당 커플 코드 응답
        if(coupleCode.isPresent())
            return CoupleCodeResponse.of(coupleCode.get());

        // 존재 하지 않는 경우 새로운 커플 코드 생성
        Integer code = generateCoupleCode();

        CoupleCode newCoupleCode = new CoupleCode(code, member);
        coupleCodeRepository.save(newCoupleCode);

        return CoupleCodeResponse.of(newCoupleCode);
    }

    public IsCoupleResponse checkCouple(Long memberId){
        return IsCoupleResponse.of(coupleRepository.existsByMaleIdOrFemaleId(memberId, memberId));
    }

    @Transactional
    public void createCouple(Long memberId, CoupleConnectRequest coupleConnectRequest){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 해당 멤버 커플 여부 확인 (동시에 코드 입력 시 발생 가능)
        boolean isCouple = member.getGender() == Gender.MALE ?
                coupleRepository.existsByMaleId(memberId) : coupleRepository.existsByFemaleId(memberId);
        if(isCouple)
            throw new BadRequestException(ALREADY_CONNECTED_MEMBER);

        // 연인의 커플 코드 조회 (본인의 커플 코드 제외)
        // (상대가 이미 커플이라면, 커플 코드가 존재하지 않으므로 커플인 멤버에 대한 필터링 필요 없음)
        CoupleCode loverCoupleCode = coupleCodeRepository.findByCodeAndMemberIdNot(coupleConnectRequest.loverCode(), memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_COUPLE_CODE));

        // 연인 엔티티 가져오기
        Member lover = loverCoupleCode.getMember();

        // 커플 생성 (남녀 구분하여 생성)
        Couple couple = member.getGender() == Gender.MALE ? new Couple(member, lover) : new Couple(lover, member);
        coupleRepository.save(couple);

        // 연결되었으므로 남아있는 두 멤버의 커플 코드 삭제
        Optional<CoupleCode> coupleCode = coupleCodeRepository.findByMemberId(memberId);
        if(coupleCode.isPresent())
            coupleCodeRepository.delete(coupleCode.get());
        coupleCodeRepository.delete(loverCoupleCode);
    }

    @Transactional
    public void deleteCouple(Long memberId) {
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // 커플 레코드 조회 및 삭제
        Optional<Couple> optionalCouple = member.getGender() == Gender.MALE ?
                coupleRepository.findByMaleId(memberId) : coupleRepository.findByFemaleId(memberId);
        Couple couple = optionalCouple.orElseThrow(() -> new BadRequestException(NOT_FOUND_COUPLE_OF_MEMBER));
        coupleRepository.delete(couple);
    }

    // 중복되지 않는 6자리 커플 코드를 생성함
    public Integer generateCoupleCode(){
        while(true){
            Integer generatedCode = ThreadLocalRandom.current().nextInt(100000, 1000000);  // 6자리 코드 생성

            if(!coupleCodeRepository.existsByCode(generatedCode))   // 중복되지 않는 코드인지 검증
                return generatedCode;
        }
    }
}
