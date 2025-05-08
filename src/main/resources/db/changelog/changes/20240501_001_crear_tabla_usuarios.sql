--liquibase formatted sql

--changeset usuario:1
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre_completo VARCHAR(100),
    fecha_creacion TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    ultimo_login TIMESTAMP,
    token_recuperacion VARCHAR(100),
    expiracion_token_recuperacion TIMESTAMP,
    cuenta_activa BOOLEAN DEFAULT TRUE,
    role VARCHAR(20) NOT NULL
);

--rollback DROP TABLE usuarios;
