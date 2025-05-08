package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ActivityEmployeeDTO {
    private UUID id;
    private UUID activityId;
    private UUID employeeId;
    private String description;
    private Integer percentageConcluded;
    private LocalDate concludedDate;
}
