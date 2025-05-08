package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EnrollmentDTO {
    private UUID id;
    private UUID courseId;
    private UUID studentId;
    private LocalDate enrollmentDate;
    private String enrollmentStatus;
}
