package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.ActivityEmployeeDTO;
import br.com.nttdata.skilbost.bff.service.ActivityEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/activity-employees")
@RequiredArgsConstructor
@Slf4j
public class ActivityEmployeeBffController {

    private final ActivityEmployeeService activityEmployeeService;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<ActivityEmployeeDTO>> create(@RequestBody @Valid ActivityEmployeeDTO dto) {
        return activityEmployeeService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<ActivityEmployeeDTO>>> listAll() {
        return Mono.just(activityEmployeeService.listAll())
                .map(list -> ResponseEntity.ok().body(list))
                .onErrorResume(error -> {
                    log.error("Erro ao listar vínculos: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ActivityEmployeeDTO>> getById(@PathVariable String id) {
        return activityEmployeeService.getById(id)
                .map(dto -> ResponseEntity.ok().body(dto))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar vínculo ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ActivityEmployeeDTO>> update(@PathVariable String id, @RequestBody @Valid ActivityEmployeeDTO dto) {
        return activityEmployeeService.update(id, dto)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar vínculo ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        activityEmployeeService.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar vínculo ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.noContent().build());
    }
}
