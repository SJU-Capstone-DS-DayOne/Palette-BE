package kr.ac.sejong.ds.palette.common.infra.messaging.dto;

import java.util.Map;

public record BatchJobRequest(
        String jobName,
        String jobId,
        Map<String, Object> params
) {
}
