package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.ActivityEmployeeDTO;
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
public class ActivityEmployeeService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<ActivityEmployeeDTO>> create(ActivityEmployeeDTO dto) {
        log.info("Criando vínculo atividade-empregado: {}", dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ActivityEmployeeDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<ActivityEmployeeDTO> listAll() {
        log.info("Listando todos os vínculos atividade-empregado");

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(ActivityEmployeeDTO.class);
    }

    // GET BY ID
    public Mono<ActivityEmployeeDTO> getById(String id) {
        log.info("Buscando vínculo pelo ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ActivityEmployeeDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<ActivityEmployeeDTO>> update(String id, ActivityEmployeeDTO dto) {
        log.info("Atualizando vínculo ID {}: {}", id, dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ActivityEmployeeDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> delete(String id) {
        log.info("Deletando vínculo ID: {}", id);

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
                .baseUrl(baseUrl + "api/v1/activity-employees")
                .build();
    }
}
