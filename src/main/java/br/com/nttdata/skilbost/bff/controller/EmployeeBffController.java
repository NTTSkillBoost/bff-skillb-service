package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.EmployeeDTO;
import br.com.nttdata.skilbost.bff.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeBffController {

    private final EmployeeService employeeService;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<EmployeeDTO>> create(@RequestBody @Valid EmployeeDTO dto) {
        return employeeService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<EmployeeDTO>>> listAll() {
        return Mono.just(employeeService.listAll())
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(error -> {
                    log.error("Erro ao listar employees: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeDTO>> getById(@PathVariable String id) {
        return employeeService.getById(id)
                .map(dto -> ResponseEntity.ok().body(dto))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar employee ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeDTO>> update(@PathVariable String id, @RequestBody @Valid EmployeeDTO dto) {
        return employeeService.update(id, dto)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar employee ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        employeeService.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar employee ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.notFound().build());
    }
}
