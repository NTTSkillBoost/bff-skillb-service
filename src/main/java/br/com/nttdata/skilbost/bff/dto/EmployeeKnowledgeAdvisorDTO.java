package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeKnowledgeAdvisorDTO {
    private UUID id;
    private String name;
    private String bio;
    private UUID employeeId;
    private LocalDate startDate;
}
