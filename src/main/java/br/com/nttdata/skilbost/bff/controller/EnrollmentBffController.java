package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.EnrollmentDTO;
import br.com.nttdata.skilbost.bff.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentBffController {

    private final EnrollmentService enrollmentService;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<EnrollmentDTO>> create(@RequestBody @Valid EnrollmentDTO dto) {
        return enrollmentService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<EnrollmentDTO>>> listAll() {
        return Mono.just(enrollmentService.listAll())
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(error -> {
                    log.error("Erro ao listar enrollments: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentDTO>> getById(@PathVariable String id) {
        return enrollmentService.getById(id)
                .map(dto -> ResponseEntity.ok().body(dto))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar enrollment ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentDTO>> update(@PathVariable String id, @RequestBody @Valid EnrollmentDTO dto) {
        return enrollmentService.update(id, dto)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar enrollment ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        enrollmentService.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar enrollment ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.notFound().build());
    }
}
