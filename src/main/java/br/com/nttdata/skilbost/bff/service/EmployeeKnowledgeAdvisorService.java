package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.EmployeeKnowledgeAdvisorDTO;
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
public class EmployeeKnowledgeAdvisorService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<EmployeeKnowledgeAdvisorDTO>> create(EmployeeKnowledgeAdvisorDTO dto) {
        log.info("Criando advisor: {}", dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeKnowledgeAdvisorDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<EmployeeKnowledgeAdvisorDTO> listAll() {
        log.info("Listando todos os advisors");

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(EmployeeKnowledgeAdvisorDTO.class);
    }

    // GET BY ID
    public Mono<EmployeeKnowledgeAdvisorDTO> getById(String id) {
        log.info("Buscando advisor por ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeKnowledgeAdvisorDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<EmployeeKnowledgeAdvisorDTO>> update(String id, EmployeeKnowledgeAdvisorDTO dto) {
        log.info("Atualizando advisor ID {}: {}", id, dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeKnowledgeAdvisorDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> delete(String id) {
        log.info("Deletando advisor ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private WebClient getWebClient() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("COURSE-SERVICE", false);
        String baseUrl = instance.getHomePageUrl(); // Exemplo: http://localhost:8881/

        return webClientBuilder
                .baseUrl(baseUrl + "api/v1/employee-knowledge-advisors")
                .build();
    }
}
