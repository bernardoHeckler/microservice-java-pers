package br.edu.atitus.von_core_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.atitus.von_core_service.entities.RegistroEmocional;

@Repository
public interface RegistroEmocionalRepository extends JpaRepository<RegistroEmocional, Long>{

		List<RegistroEmocional> findAllByUsuarioIdOrderByDataHoraDesc(Long usuarioId); // Busca o histórico de um usuário específico
}
