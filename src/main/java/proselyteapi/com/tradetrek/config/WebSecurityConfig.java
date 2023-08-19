package proselyteapi.com.tradetrek.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import proselyteapi.com.tradetrek.api.GlobalExceptionHandler;
import proselyteapi.com.tradetrek.repository.UserRepository;
import proselyteapi.com.tradetrek.security.AuthenticationManager;
import proselyteapi.com.tradetrek.security.BearerTokenServerAuthenticationConverter;
import proselyteapi.com.tradetrek.security.JwtHandler;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;
    private final String[] publicRoutes = {"/api/register", "/api/login"};
    private final UserRepository userRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(publicRoutes).permitAll()
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> handleAuthenticationError(swe, HttpStatus.UNAUTHORIZED, "unauthorized error"))
                .accessDeniedHandler((swe, e) -> handleAuthenticationError(swe, HttpStatus.FORBIDDEN, "access denied"))
                .and()
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(apiKeyWebFilter(), SecurityWebFiltersOrder.FIRST)
                .build();
    }

    @Bean
    public WebFilter apiKeyWebFilter() {
        List<String> allowedUrls = Arrays.asList("/api/register", "/api/login");

        return (exchange, chain) ->
                userRepository.existsByApiKey(exchange.getRequest().getHeaders().getFirst("API-KEY"))
                        .filter(valid -> allowedUrls.contains(exchange.getRequest().getPath().value()) && valid)
                        .switchIfEmpty(Mono.fromRunnable(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        }))
                        .flatMap(valid -> chain.filter(exchange));
    }

    private Mono<Void> handleAuthenticationError(ServerWebExchange swe, HttpStatus status, String errorMessage) {
        log.error("IN securityWebFilterChain - {}: {}", status, errorMessage);
        swe.getResponse().setStatusCode(status);
        return swe.getResponse().setComplete();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        return bearerAuthenticationFilter;
    }
}
