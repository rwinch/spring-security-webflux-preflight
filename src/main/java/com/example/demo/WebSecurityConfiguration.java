package com.example.demo;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.PreFlightHandler;

@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfiguration  {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, PreFlightHandler handler) throws Exception {
        http.authorizeExchange((exchanges) -> {
            exchanges.matchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)).permitAll();
            exchanges.anyExchange().authenticated();
        });
        http.addFilterAt((e,c) -> {
            if (CorsUtils.isPreFlightRequest(e.getRequest())) {
                return handler.handlePreFlight(e);
            } else {
                return c.filter(e);
            }
        }, SecurityWebFiltersOrder.CORS);
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }
}
