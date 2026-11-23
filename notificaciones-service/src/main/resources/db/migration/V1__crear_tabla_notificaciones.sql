CREATE TABLE notificaciones (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    destinatario VARCHAR(255) NOT NULL,
    mensaje VARCHAR(1000) NOT NULL,
    estado VARCHAR(255) NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_notificaciones_pedido_id UNIQUE (pedido_id)
);
