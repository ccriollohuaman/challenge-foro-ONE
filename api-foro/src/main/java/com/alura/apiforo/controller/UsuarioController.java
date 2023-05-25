package com.alura.apiforo.controller;

import com.alura.apiforo.domain.usuario.DatosRegistroUsuario;
import com.alura.apiforo.domain.usuario.Usuario;
import com.alura.apiforo.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @Operation(summary = "Registrar un usuario")
    @SecurityRequirement(name = "bearerAuth")
    public void registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(datosRegistroUsuario.contrasena());
        var usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario.nombre(),
                datosRegistroUsuario.email(),passwordEncriptada));
        System.out.println(usuario);
    }

}
