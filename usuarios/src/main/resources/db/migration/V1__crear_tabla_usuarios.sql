CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    run VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    clave VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(255) NOT NULL,
    region VARCHAR(255),
    comuna VARCHAR(255),
    direccion VARCHAR(255),
    activo BIT(1) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_run UNIQUE (run),
    CONSTRAINT uk_usuarios_mail UNIQUE (mail)
);
