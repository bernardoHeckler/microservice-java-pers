package br.edu.atitus.gateway_service.components;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

	private static final String SECRET_KEY = "chaveSuperSecretaParaJWTdeExemplo!@#123"; // Chave secreta (use uma mais segura)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1000 milisegundos * 60 segundos * 60 minutos * 24 horas

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey()) // Define a chave para verificação
                    .build()
                    .parseSignedClaims(token) // Faz o parsing do token
                    .getPayload(); // Retorna as informações do token
//                    .getPayload().getSubject(); // Retorna as informações do token
        } catch (Exception e) {
            return null;
        }
    }
    
}