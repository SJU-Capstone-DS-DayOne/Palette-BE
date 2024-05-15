package kr.ac.sejong.ds.palette.couple.service;

import kr.ac.sejong.ds.palette.common.exception.BadRequestException;
import kr.ac.sejong.ds.palette.couple.dto.response.CoupleCodeResponse;
import kr.ac.sejong.ds.palette.couple.entity.CoupleCode;
import kr.ac.sejong.ds.palette.couple.repository.CoupleCodeRepository;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static kr.ac.sejong.ds.palette.common.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@RequiredArgsConstructor
public class CoupleService {
    private final MemberRepository memberRepository;
    private final CoupleCodeRepository coupleCodeRepository;

    public CoupleCodeResponse getCoupleCode(Long memberId){
        Member member = memberRepository.findById(memberId)  // 멤버 존재 여부 확인
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        Optional<CoupleCode> coupleCode = coupleCodeRepository.findByMember(member);  // 해당 멤버의 커플 코드 조회

        // 커플 코드가 이미 존재하는 경우 해당 커플 코드 응답
        if(coupleCode.isPresent())
            return CoupleCodeResponse.of(coupleCode.get());

        // 존재 하지 않는 경우 새로운 커플 코드 생성
        Integer code = generateCoupleCode();

        CoupleCode newCoupleCode = new CoupleCode(code, member);
        coupleCodeRepository.save(newCoupleCode);

        return CoupleCodeResponse.of(newCoupleCode);
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
