package customer.config;

import customer.client.WebClientMedicationsClient;
import customer.client.WebClientProductsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype") /* TODO: компоненты которые имеют scope prototype не регистрируются в контексте приложения,
    т.е. могут зависнуть при завершении приложения (при завершении контекста), если это сетевые соединения */
    public WebClient.Builder pharmacyServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrationRepository, authorizedClientRepository);

        filter.setDefaultClientRegistrationId("keycloak");

        return WebClient.builder()
                .filter(filter);
    }

    @Bean
    public WebClientMedicationsClient webClientMedicationsClient(
            @Value("${services.catalogue.uri:http://localhost:8083}") String catalogueBaseUrl,
            WebClient.Builder pharmacyServicesWebClientBuilder) {
        return new WebClientMedicationsClient(pharmacyServicesWebClientBuilder
                .baseUrl(catalogueBaseUrl)
                .build());
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${services.catalogue.uri:http://localhost:8083}") String catalogueBaseUrl,
            WebClient.Builder pharmacyServicesWebClientBuilder) {
        return new WebClientProductsClient(pharmacyServicesWebClientBuilder
                .baseUrl(catalogueBaseUrl)
                .build());
    }
}