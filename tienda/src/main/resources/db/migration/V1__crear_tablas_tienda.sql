CREATE TABLE productos (
    codigo VARCHAR(40) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    subcategoria VARCHAR(100) NOT NULL,
    nombre VARCHAR(180) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    unidad VARCHAR(40) NOT NULL,
    p_compra DECIMAL(12, 2) NOT NULL,
    p_venta DECIMAL(12, 2) NOT NULL,
    stock INT NOT NULL,
    stock_min INT NOT NULL,
    PRIMARY KEY (codigo)
);

CREATE TABLE carritos (
    id VARCHAR(255) NOT NULL,
    creado_en DATETIME(6),
    PRIMARY KEY (id)
);

CREATE TABLE comentarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo_producto VARCHAR(40) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    texto VARCHAR(1000) NOT NULL,
    visible BIT(1) NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE items_carrito (
    id BIGINT NOT NULL AUTO_INCREMENT,
    carrito_id VARCHAR(255) NOT NULL,
    codigo_producto VARCHAR(40) NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_items_carrito_carrito_producto UNIQUE (carrito_id, codigo_producto),
    CONSTRAINT fk_items_carrito_carrito FOREIGN KEY (carrito_id) REFERENCES carritos (id)
);
