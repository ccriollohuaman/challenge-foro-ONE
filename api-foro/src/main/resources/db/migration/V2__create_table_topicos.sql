CREATE TABLE topicos (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensaje VARCHAR(250) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    status VARCHAR(50) not null,
    id_autor VARCHAR(50) NOT NULL,
    id_curso INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_curso) REFERENCES cursos(id)
);