package br.com.nttdata.skilbost.bff.service;

import br.com.nttdata.skilbost.bff.dto.EmployeeRoleDTO;
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
public class EmployeeRoleService {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;

    // CREATE
    public Mono<ResponseEntity<EmployeeRoleDTO>> create(EmployeeRoleDTO dto) {
        log.info("Criando employee role: {}", dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeRoleDTO.class)
                .doOnNext(response -> log.info("Resposta do microserviço: {}", response))
                .map(ResponseEntity::ok);
    }

    // LIST ALL
    public Flux<EmployeeRoleDTO> listAll() {
        log.info("Listando todos os roles");

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(EmployeeRoleDTO.class);
    }

    // GET BY ID
    public Mono<EmployeeRoleDTO> getById(String id) {
        log.info("Buscando role pelo ID: {}", id);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeRoleDTO.class);
    }

    // UPDATE
    public Mono<ResponseEntity<EmployeeRoleDTO>> update(String id, EmployeeRoleDTO dto) {
        log.info("Atualizando role ID {}: {}", id, dto);

        WebClient clientWebClient = getWebClient();

        return clientWebClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(EmployeeRoleDTO.class)
                .map(ResponseEntity::ok);
    }

    // DELETE
    public Mono<Void> delete(String id) {
        log.info("Deletando role ID: {}", id);

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
                .baseUrl(baseUrl + "api/v1/employee-roles")
                .build();
    }
}
