RENAME TABLE comentarios TO contactos;

ALTER TABLE contactos
    CHANGE COLUMN codigo_producto correo VARCHAR(254) NOT NULL,
    CHANGE COLUMN texto comentario VARCHAR(1000) NOT NULL;
