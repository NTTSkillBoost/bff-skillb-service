package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.ActivityDTO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<ActivityDTO>> createActivity(ActivityDTO activityDTO) {
        log.info("Criando atividade: {}", activityDTO);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(activityDTO)
                .retrieve()
                .bodyToMono(ActivityDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<ActivityDTO> listActivities() {
        log.info("Listando todas as atividades via BFF");

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(ActivityDTO.class);
    }

    // GET BY ID
    public Mono<ActivityDTO> getActivityById(String id) {
        log.info("Buscando atividade por ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ActivityDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<ActivityDTO>> updateActivity(String id, ActivityDTO activityDTO) {
        log.info("Atualizando atividade ID {}: {}", id, activityDTO);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(activityDTO)
                .retrieve()
                .bodyToMono(ActivityDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> deleteActivity(String id) {
        log.info("Deletando atividade ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private WebClient getWebClient() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("ACTIVITY-MANAGEMENT-SERVICE", false);
        String baseUrl = instance.getHomePageUrl(); // Exemplo: http://localhost:8881/

        return webClientBuilder
                .baseUrl(baseUrl + "api/v1/activities")
                .build();
    }
}
