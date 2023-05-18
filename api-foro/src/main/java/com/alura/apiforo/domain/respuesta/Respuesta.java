package com.alura.apiforo.domain.respuesta;

import com.alura.apiforo.domain.topico.Topico;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topico", nullable = false)
    @JsonBackReference
    private Topico topico;

    private LocalDateTime fechaCreacion;
    private String id_autor;
    private Boolean solucion;

    public Respuesta(DatosRegistroRespuesta datosRegistroRespuesta, Topico topico) {
        this.mensaje = datosRegistroRespuesta.mensaje();
        this.topico = topico;
        this.fechaCreacion = LocalDateTime.now();
        this.id_autor = datosRegistroRespuesta.id_autor();
        this.solucion = false;
    }
}
