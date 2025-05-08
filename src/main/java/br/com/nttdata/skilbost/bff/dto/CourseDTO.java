package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CourseDTO {
    private UUID id;
    private String name;
    private String description;
    private String syllabus;
    private Integer goalPoints;
    private LocalDate makeDate;
    private String courseStatus;
    private String positionLevel;
    private UUID employeeKnowledgeAdvisorId;
}