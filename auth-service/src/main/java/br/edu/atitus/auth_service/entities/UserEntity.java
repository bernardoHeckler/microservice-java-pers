package br.edu.atitus.auth_service.entities;

import java.util.Collection;
import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuarios")
public class UserEntity implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	@JsonIgnore
	private String password;
	
	@Column
	@Enumerated(EnumType.ORDINAL)
	private UserType type;
    
    @Column(name = "pontos_acumulados")
    private Integer pontosAcumulados = 0;

    @Column(name = "nivel_atual")
    private Integer nivelAtual = 1;

    @Column(name = "tipo_assinatura")
    private String tipoAssinatura = "FREE"; // 'FREE', 'PREMIUM'

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Integer getPontosAcumulados() { return pontosAcumulados; }
    public void setPontosAcumulados(Integer pontosAcumulados) { this.pontosAcumulados = pontosAcumulados; }

    public Integer getNivelAtual() { return nivelAtual; }
    public void setNivelAtual(Integer nivelAtual) { this.nivelAtual = nivelAtual; }

    public String getTipoAssinatura() { return tipoAssinatura; }
    public void setTipoAssinatura(String tipoAssinatura) { this.tipoAssinatura = tipoAssinatura; }


	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}