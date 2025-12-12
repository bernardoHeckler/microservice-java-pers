package br.edu.atitus.von_core_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String apiUrl;

    public String analisarDesabafo(String textoUsuario) {
        // Validação simples
        if (textoUsuario == null || textoUsuario.isEmpty()) return "Sem análise.";

        RestTemplate restTemplate = new RestTemplate();
        
        String promptSistema = """
            Você é o VON, um assistente de regulação emocional pessoal.
            Analise o seguinte desabafo do usuário.
            Responda EXATAMENTE neste formato JSON (sem markdown ```json):
            {
                "resumo": "Resuma o sentimento em 1 frase",
                "diagnostico": "Dê um nome técnico ou simples para o estado emocional",
                "sugestao_acao": "Uma microação prática de até 3 minutos para fazer agora"
            }
            
            Desabafo do Usuário: 
            """ + textoUsuario;

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", promptSistema)
                ))
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "?key=" + apiKey, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("ERRO GOOGLE: " + e.getResponseBodyAsString());
            return "{\"erro\": \"Erro API Google: " + e.getStatusCode() + " - Verifique o console\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"erro\": \"Falha interna na IA\"}";
        }
    }
}