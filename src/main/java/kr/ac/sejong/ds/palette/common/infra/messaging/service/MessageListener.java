package kr.ac.sejong.ds.palette.common.infra.messaging.service;

import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.infra.messaging.dto.MemberEmbeddingStatusMessage;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.entity.PreferenceStatus;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {

    private final MemberRepository memberRepository;

    /**
     * 1. Queue 에서 메세지를 구독
     **/
    @RabbitListener(queues = "${rabbitmq.queues.embeddingStatus}")
    @Transactional
    public void receiveMemberEmbeddingStatusMessage(MemberEmbeddingStatusMessage message) {

        log.info("RabbitMQ 유저 임베딩 생성 여부 메시지 수신 - memberId: {}, isSuccess: {}", message.memberId(), message.isSuccess());
        Member member = memberRepository.findById(message.memberId())
                .orElseThrow(NotFoundMemberException::new);
        if (message.isSuccess()) {
            log.info("임베딩 생성 성공 - memberId: {}", message.memberId());
            member.setPreferenceStatus(PreferenceStatus.COMPLETE);
        } else {
            log.warn("임베딩 생성 실패 - memberId: {}", message.memberId());  // ML 서버에서 처리 실패 시, 선호 레스토랑 선택 여부 값을 다시 INCOMPLETE로 설정
            member.setPreferenceStatus(PreferenceStatus.INCOMPLETE);
        }
    }
}