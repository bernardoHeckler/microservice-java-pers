CREATE TABLE tb_usuarios (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    type SMALLINT,
    pontos_acumulados INT DEFAULT 0,
    nivel_atual INT DEFAULT 1,
    tipo_assinatura VARCHAR(50) DEFAULT 'FREE',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);