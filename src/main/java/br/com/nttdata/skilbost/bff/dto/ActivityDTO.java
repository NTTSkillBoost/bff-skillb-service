package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActivityDTO {
    private UUID id;
    private String name;
    private UUID courseId;
    private String description;
    private Integer point;
    private LocalDate executeDate;
    private LocalDateTime availabilityDate;
    private String activityType;
    private String activityPath;
}
