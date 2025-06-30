package kr.ac.sejong.ds.palette.batch.service;

import kr.ac.sejong.ds.palette.common.infra.messaging.dto.BatchJobRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public void executeBatch(BatchJobRequest batchJobRequest) {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();  // JobParametersBuilder를 사용하여 JobParameters 생성
        batchJobRequest.params().forEach((key, value) -> jobParametersBuilder.addString(key, value.toString()));
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();

        switch (batchJobRequest.jobName()) {
            case "restaurantIntegrationJob":
                try {
                    log.info("레스토랑 통합 배치 작업 실행 요청: jobName: {}, jobId: {}, district: {}, version: {}", batchJobRequest.jobName(), batchJobRequest.jobId(),
                            batchJobRequest.params().get("district"), batchJobRequest.params().get("version"));

                    JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(batchJobRequest.jobName()), jobParameters);
                    if (jobExecution.getStatus().isUnsuccessful())
                        throw new RuntimeException();

                    log.info("레스토랑 통합 배치 작업 실행 완료: jobName: {}, jobId: {}, district: {}, version: {}", batchJobRequest.jobName(), batchJobRequest.jobId(),
                            batchJobRequest.params().get("district"), batchJobRequest.params().get("version"));
                } catch (Exception e) {
                    log.error("레스토랑 통합 배치 작업 실패: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
                break;
            default:
                log.error("지원하지 않는 배치 작업 요청: {}", batchJobRequest.jobName());
                throw new RuntimeException("지원하지 않는 배치 작업입니다.");
        }
    }
}
