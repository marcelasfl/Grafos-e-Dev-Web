package com.example.dev_web_gerador.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev_web_gerador.Service.GrafoDependencia;
import com.example.dev_web_gerador.Service.GrafoDependenciaResult;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/dependencias")
public class DependenciasController {

    private final GrafoDependencia grafoDependencia;

    @Autowired
    public DependenciasController(GrafoDependencia grafoDependencia) {
        this.grafoDependencia = grafoDependencia;
    }

    @GetMapping("/epicos")
    public GrafoDependenciaResult gerarGrafoDependenciaEpico() {
        return this.grafoDependencia.epicoGrafoDependencia();
    }

    @GetMapping("/historia-usuario")
    public GrafoDependenciaResult gerarGrafoDependenciaHistoriaUsario() {
        return this.grafoDependencia.historiaUsuarioGrafoDependencia();

    }

    @GetMapping("/tarefa")
    public GrafoDependenciaResult gerarGrafoDependenciaTarefa() {
        return this.grafoDependencia.tarefaGrafoDependencia();

    }
}
