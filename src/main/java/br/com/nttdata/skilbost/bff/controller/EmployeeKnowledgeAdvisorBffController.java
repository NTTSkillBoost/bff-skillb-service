package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.EmployeeKnowledgeAdvisorDTO;
import br.com.nttdata.skilbost.bff.service.EmployeeKnowledgeAdvisorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/employee-knowledge-advisors")
@RequiredArgsConstructor
@Slf4j
public class EmployeeKnowledgeAdvisorBffController {

    private final EmployeeKnowledgeAdvisorService service;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<EmployeeKnowledgeAdvisorDTO>> create(@RequestBody @Valid EmployeeKnowledgeAdvisorDTO dto) {
        return service.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<EmployeeKnowledgeAdvisorDTO>>> listAll() {
        return Mono.just(service.listAll())
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(error -> {
                    log.error("Erro ao listar advisors: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeKnowledgeAdvisorDTO>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(dto -> ResponseEntity.ok().body(dto))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar advisor ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeKnowledgeAdvisorDTO>> update(@PathVariable String id, @RequestBody @Valid EmployeeKnowledgeAdvisorDTO dto) {
        return service.update(id, dto)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar advisor ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
         service.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar advisor ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.notFound().build());
    }
}
