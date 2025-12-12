package br.edu.atitus.von_core_service.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "registros_emocionais")
public class RegistroEmocional {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long usuarioId;
	
	@Column(nullable = false)
	private String humor;
	
	@Column(nullable = false)
	private Integer intensidade;
	
	@Column(columnDefinition = "TEXT")
	private String motivo;
	
	private String audioURL;
	
	private LocalDateTime dataHora;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getHumor() {
		return humor;
	}

	public void setHumor(String humor) {
		this.humor = humor;
	}

	public Integer getIntensidade() {
		return intensidade;
	}

	public void setIntensidade(Integer intensidade) {
		this.intensidade = intensidade;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getAudioURL() {
		return audioURL;
	}

	public void setAudioURL(String audioURL) {
		this.audioURL = audioURL;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	@PrePersist
	public void prePersist() {
		this.dataHora = LocalDateTime.now();
	}
	
}
