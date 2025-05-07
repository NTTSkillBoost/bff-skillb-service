package br.com.nttdata.skilbost.bff.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeRoleDTO {
    private String role;
    private UUID employeeId;
}
