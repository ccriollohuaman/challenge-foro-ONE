CREATE TABLE respuestas (
    id INT NOT NULL AUTO_INCREMENT,
    mensaje VARCHAR(255),
    id_topico INT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    id_autor VARCHAR(50) NOT NULL,
    solucion BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_topico) REFERENCES topicos(id)
);
