package br.edu.atitus.gateway_service.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import br.edu.atitus.gateway_service.components.JwtUtil;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter, Ordered {

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		ServerHttpRequest request = exchange.getRequest();
		
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			Claims payload = JwtUtil.validateToken(jwt);
			if (payload != null) {
				ServerHttpRequest modifiedRequest = request.mutate()
						.header("X-User-Id", String.valueOf(payload.get("id")))
						.header("X-User-Email", payload.get("email", String.class))
						.build();
				return chain.filter(exchange.mutate().request(modifiedRequest).build());
			}
		}
		
		// Se chegou aqui, não é válido
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

}