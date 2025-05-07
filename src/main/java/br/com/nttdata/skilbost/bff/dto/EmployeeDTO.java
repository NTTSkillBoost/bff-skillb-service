package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeDTO {
    private String name;
    private String email;
    private String phone;
    private String documentId;
    private Long employeeCompanyId;
    private UUID addressId;
    private String employeePosition;
    private String employeeStatus;
    private LocalDate hiringDate;
    private String personType;
}
