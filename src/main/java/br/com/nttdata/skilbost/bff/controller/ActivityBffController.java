package br.com.nttdata.skilbost.bff.controller;

import br.com.nttdata.skilbost.bff.dto.ActivityDTO;
import br.com.nttdata.skilbost.bff.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityBffController {

    private final ActivityService activityService;

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<ActivityDTO>> create(@RequestBody @Valid ActivityDTO activityDTO) {
        return activityService.createActivity(activityDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getBody()));
    }

    // LIST ALL
    @GetMapping
    public Mono<ResponseEntity<Flux<ActivityDTO>>> listActivities() {
        return Mono.just(activityService.listActivities())
                .map(activities -> ResponseEntity.ok().body(activities))
                .onErrorResume(error -> {
                    log.error("Erro ao listar atividades: {}", error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Flux.empty()));
                });
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ActivityDTO>> getById(@PathVariable String id) {
        return activityService.getActivityById(id)
                .map(activity -> ResponseEntity.ok().body(activity))
                .onErrorResume(error -> {
                    log.error("Erro ao buscar atividade ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ActivityDTO>> update(@PathVariable String id, @RequestBody @Valid ActivityDTO activityDTO) {
        return activityService.updateActivity(id, activityDTO)
                .map(response -> ResponseEntity.ok().body(response.getBody()))
                .onErrorResume(error -> {
                    log.error("Erro ao atualizar atividade ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        activityService.deleteActivity(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> {
                    log.error("Erro ao deletar atividade ID {}: {}", id, error.getMessage());
                    return Mono.just(ResponseEntity.notFound().build());
                });
        return Mono.just(ResponseEntity.notFound().build());
    }
}
