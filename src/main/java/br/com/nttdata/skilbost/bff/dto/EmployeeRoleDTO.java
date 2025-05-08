package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeRoleDTO {
    private UUID id;
    private String role;
    private UUID employeeId;
}
