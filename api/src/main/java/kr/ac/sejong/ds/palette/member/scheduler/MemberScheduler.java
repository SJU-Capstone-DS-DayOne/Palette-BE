package kr.ac.sejong.ds.palette.member.scheduler;

import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.entity.PreferenceStatus;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberScheduler {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 * * * * *")  // 매 분 0초에 실행
    @Transactional
    public void rescuePendingMembers() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);
        int updatedCount = memberRepository.updatePreferenceStatusBefore(
                PreferenceStatus.INCOMPLETE,
                PreferenceStatus.PENDING,
                threshold
        );

        if (updatedCount > 0) {
            log.info("무한 PENDING 해결 스케줄러 - {}건 상태 변경됨 (PENDING → INCOMPLETE)", updatedCount);
        }
    }
}