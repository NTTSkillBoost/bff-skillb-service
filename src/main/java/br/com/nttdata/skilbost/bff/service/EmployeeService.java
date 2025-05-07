package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.EmployeeDTO;
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
public class EmployeeService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<EmployeeDTO>> create(EmployeeDTO dto) {
        log.info("Criando employee: {}", dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<EmployeeDTO> listAll() {
        log.info("Listando todos os employees");

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(EmployeeDTO.class);
    }

    // GET BY ID
    public Mono<EmployeeDTO> getById(String id) {
        log.info("Buscando employee pelo ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<EmployeeDTO>> update(String id, EmployeeDTO dto) {
        log.info("Atualizando employee ID {}: {}", id, dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> delete(String id) {
        log.info("Deletando employee ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private WebClient getWebClient() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("EMPLOYEE-SERVICE", false);
        String baseUrl = instance.getHomePageUrl(); // Exemplo: http://localhost:8881/

        return webClientBuilder
                .baseUrl(baseUrl + "api/v1/employees")
                .build();
    }
}
