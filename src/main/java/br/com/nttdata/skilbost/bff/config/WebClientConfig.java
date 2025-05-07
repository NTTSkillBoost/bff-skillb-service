package br.com.nttdata.skilbost.bff.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    //@Value("${client.service-url}")
    //private String clientServiceUrl;

    //@Value("${order.service-url}")
   // private String orderServiceUrl;

    /*@Bean
    @LoadBalanced
    public WebClient clientWebClient() {
        return WebClient.builder()
                .baseUrl("http://client-service")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @LoadBalanced
    public WebClient orderWebClient() {
        return WebClient.builder()
                .baseUrl("http://order-service")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }*/

    @Bean
    //@LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
