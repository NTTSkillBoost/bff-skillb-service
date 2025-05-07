package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.EmployeeRoleDTO;
import br.com.nttdata.skilbost.bff.service.EmployeeRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/employee-roles")
@RequiredArgsConstructor
@Slf4j
public class EmployeeRoleBffController {

    private final EmployeeRoleService employeeRoleService;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<EmployeeRoleDTO>> create(@RequestBody @Valid EmployeeRoleDTO dto) {
        return employeeRoleService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<EmployeeRoleDTO>>> listAll() {
        return Mono.just(employeeRoleService.listAll())
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(error -> {
                    log.error("Erro ao listar roles: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeRoleDTO>> getById(@PathVariable String id) {
        return employeeRoleService.getById(id)
                .map(dto -> ResponseEntity.ok().body(dto))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar role ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeRoleDTO>> update(@PathVariable String id, @RequestBody @Valid EmployeeRoleDTO dto) {
        return employeeRoleService.update(id, dto)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar role ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        employeeRoleService.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar role ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.notFound().build());
    }
}
