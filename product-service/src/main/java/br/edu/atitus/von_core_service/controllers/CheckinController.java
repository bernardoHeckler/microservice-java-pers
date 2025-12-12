package br.edu.atitus.von_core_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.von_core_service.entities.RegistroEmocional;
import br.edu.atitus.von_core_service.repositories.RegistroEmocionalRepository;
import br.edu.atitus.von_core_service.services.GeminiService;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    @Autowired
    private RegistroEmocionalRepository repository;

    @Autowired
    private GeminiService geminiService;

    // Rota 1: Criar o Check-in e receber análise da IA
    // Endereço: POST http://localhost:8000/checkin
    @PostMapping
    public ResponseEntity<String> criarCheckin(
            @RequestBody RegistroEmocional registro,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader) {
        
        Long userId = (userIdHeader != null) ? userIdHeader : 1L;
        registro.setUsuarioId(userId);

        repository.save(registro);

        String analiseIa = geminiService.analisarDesabafo(registro.getMotivo());

        return ResponseEntity.status(HttpStatus.CREATED).body(analiseIa);
    }

    // Rota 2: Consultar histórico
    // Endereço: GET http://localhost:8000/checkin/historico
    @GetMapping("/historico")
    public ResponseEntity<List<RegistroEmocional>> verHistorico(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdHeader) {
        
        Long userId = (userIdHeader != null) ? userIdHeader : 1L;
        
        List<RegistroEmocional> lista = repository.findAllByUsuarioIdOrderByDataHoraDesc(userId);
        
        return ResponseEntity.ok(lista);
    }
}