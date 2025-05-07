package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.EnrollmentDTO;
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
public class EnrollmentService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<EnrollmentDTO>> create(EnrollmentDTO dto) {
        log.info("Criando enrollment: {}", dto);

        WebClient clientWebClient = getWebClient();
        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EnrollmentDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<EnrollmentDTO> listAll() {
        log.info("Listando todos os enrollments");

        WebClient clientWebClient = getWebClient();
        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(EnrollmentDTO.class);
    }

    // GET BY ID
    public Mono<EnrollmentDTO> getById(String id) {
        log.info("Buscando enrollment pelo ID: {}", id);

        WebClient clientWebClient = getWebClient();
        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EnrollmentDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<EnrollmentDTO>> update(String id, EnrollmentDTO dto) {
        log.info("Atualizando enrollment ID {}: {}", id, dto);

        WebClient clientWebClient = getWebClient();
        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EnrollmentDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> delete(String id) {
        log.info("Deletando enrollment ID: {}", id);

        WebClient clientWebClient = getWebClient();
        return clientWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private WebClient getWebClient() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("ENROLLMENT-SERVICE", false);
        String baseUrl = instance.getHomePageUrl(); // Exemplo: http://localhost:8881/

        return webClientBuilder
                .baseUrl(baseUrl + "api/v1/enrollments")
                .build();
    }
}
