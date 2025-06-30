package kr.ac.sejong.ds.palette.common.infra.messaging;

import kr.ac.sejong.ds.palette.batch.service.BatchJobService;
import kr.ac.sejong.ds.palette.common.infra.messaging.dto.BatchJobRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {

    private final BatchJobService batchJobService;

    @RabbitListener(queues = "${rabbitmq.queues.batch}")
    public void receiveBatchJobRequest(BatchJobRequest message) {
        log.info("RabbitMQ 배치 작업 요청 메시지 수신 - jobName: {}, jobId: {}, params: {}",
                message.jobName(), message.jobId(), message.params());
        try {
            batchJobService.executeBatch(message);
        } catch (Exception e) {
            log.error("배치 작업 실패: {}", e.getMessage());  // 로깅만 수행 (예외 발생 시 ACK가 전송되지 않아 무한루프 발생)
        }
    }
}