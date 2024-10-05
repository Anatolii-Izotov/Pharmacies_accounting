package manager.config;

import manager.client.RestClientProductsRestClient;
import manager.security.OAuthClientRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
/*
методы данного класса, отмеченные анотацией @Bean будут являтся источниками компонентов
для регистрации их в контексте приложения
 */
public class ClientBeans {

    @Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${services.catalogue.registration-id:keycloak}") String registrationId) {
//            @Value("${services.catalogue.username:}") String catalogueUsername,
//            @Value("${services.catalogue.password:}") String cataloguePassword)
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new OAuthClientRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientRepository), registrationId
                ))
                .build());
    }
}
