package com.example.dev_web_gerador.Service;

import java.util.List;

public class GrafoDependenciaResult {
    public boolean temCiclo;
    public List<String> ordenacaoTopologica;

    public GrafoDependenciaResult(Boolean temCiclo, List<String> ordenacaoTopologica) {
        this.temCiclo = temCiclo;
        this.ordenacaoTopologica = ordenacaoTopologica;
    }
}