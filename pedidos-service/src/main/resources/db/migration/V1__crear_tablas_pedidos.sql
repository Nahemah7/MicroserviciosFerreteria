CREATE TABLE pedidos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    carrito_id VARCHAR(255) NOT NULL,
    nombre_cliente VARCHAR(255) NOT NULL,
    correo_cliente VARCHAR(255) NOT NULL,
    estado VARCHAR(255) NOT NULL,
    total_clp DECIMAL(12, 2) NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    confirmado_en DATETIME(6),
    notificacion_id BIGINT,
    estado_notificacion VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_pedidos_carrito_id UNIQUE (carrito_id)
);

CREATE TABLE pedido_items (
    pedido_id BIGINT NOT NULL,
    posicion INT NOT NULL,
    codigo_producto VARCHAR(255) NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (pedido_id, posicion),
    CONSTRAINT fk_pedido_items_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos (id)
);
