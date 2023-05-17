ALTER TABLE topicos
ADD CONSTRAINT uk_titulo UNIQUE (titulo);

ALTER TABLE topicos
ADD CONSTRAINT uk_mensaje UNIQUE (mensaje);