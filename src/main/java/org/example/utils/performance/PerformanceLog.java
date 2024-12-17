package org.example.utils.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.pipeline.OperationType;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceLog {
    private Integer logId;
    private OperationType operationType;
    private Float operationDuration;
    private LocalDateTime operationTimestamp;
}
