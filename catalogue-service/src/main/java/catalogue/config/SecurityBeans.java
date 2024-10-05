package catalogue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityBeans {

    @Bean // контекст безопасности
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/catalogue-api/**")
                        .hasRole("SERVICE")
                        .anyExchange().authenticated())
                .httpBasic(withDefaults())
                .build();
    }

//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authorizedHttpRequests -> authorizedHttpRequests
//                        .requestMatchers(HttpMethod.POST,"/catalogue-api/products")
//                        .hasAuthority("SCOPE_edit_catalogue")
//                        .requestMatchers(HttpMethod.PATCH, "/catalogue-api/products/{productId:\\d}")
//                        .hasAuthority("SCOPE_edit_catalogue")
//                        .requestMatchers(HttpMethod.DELETE, "/catalogue-api/products/{productId:\\d}")
//                        .hasAuthority("SCOPE_edit_catalogue")
//                        .requestMatchers(HttpMethod.GET)
//                        .hasAuthority("SCOPE_view_catalogue")
//                        .anyRequest().denyAll())
//                .csrf(CsrfConfigurer::disable)
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
//                        .jwt(Customizer.withDefaults()))
//
//                .build();
//    }

}
