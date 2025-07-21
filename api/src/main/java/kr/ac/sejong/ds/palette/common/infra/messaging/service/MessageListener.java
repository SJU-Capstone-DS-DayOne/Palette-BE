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

import java.util.Optional;

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

        Optional<Member> optionalMember = memberRepository.findById(message.memberId());
        if (optionalMember.isEmpty()) {  // 멤버를 찾을 수 없는 경우, 메시지를 버림
            log.error("회원 ID를 찾을 수 없음 - memberId: {}", message.memberId());
            return;
        }

        Member member = optionalMember.get();

        if (message.isSuccess()) {
            log.info("임베딩 생성 성공 - memberId: {}", message.memberId());
            member.setPreferenceStatus(PreferenceStatus.COMPLETE);  // 임베딩 생성 성공 시 필드값 COMPLETE로 변경
        } else {
            log.info("임베딩 생성 실패 - memberId: {}", message.memberId());  // 임베딩 생성 실패 시 필드값 INCOMPLETE로 변경
            member.setPreferenceStatus(PreferenceStatus.INCOMPLETE);
        }
    }
}