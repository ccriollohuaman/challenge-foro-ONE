ALTER TABLE topicos
ADD CONSTRAINT fk_autor_topicos FOREIGN KEY (id_autor) REFERENCES usuarios(id);

ALTER TABLE respuestas
ADD CONSTRAINT fk_autor_respuestas FOREIGN KEY (id_autor) REFERENCES usuarios(id);