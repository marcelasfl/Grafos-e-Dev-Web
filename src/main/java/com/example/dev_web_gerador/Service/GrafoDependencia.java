package com.example.dev_web_gerador.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dev_web_gerador.Model.Epico;
import com.example.dev_web_gerador.Model.HistoriaUsuario;
import com.example.dev_web_gerador.Model.Tarefa;
import com.example.dev_web_gerador.Repository.EpicoRepository;
import com.example.dev_web_gerador.Repository.HistoriaUsuarioRepository;
import com.example.dev_web_gerador.Repository.TarefaRepository;
import com.example.dev_web_gerador.Service.Grafo.Grafos;

@Service
public class GrafoDependencia {

  @Autowired
  EpicoRepository epicoRepository;

  @Autowired
  HistoriaUsuarioRepository historiaUsuarioRepository;

  @Autowired
  TarefaRepository tarefaRepository;

  public GrafoDependenciaResult epicoGrafoDependencia() {
    Grafos grafo = new Grafos<String>();

    for (Epico epico : epicoRepository.findAll()) {
      List<Epico> dependentes = this.epicoRepository.findByDepedentes(epico.getId());
      grafo.adicionarVertice(epico.getTitulo());

      for (Epico dependente : dependentes) {
        grafo.adicionarVertice(dependente.getTitulo());
        grafo.adicionarAresta(epico.getTitulo(), dependente.getTitulo());
      }

    }

    return new GrafoDependenciaResult(grafo.temCiclo(), grafo.ordenacaoTopologica());
  }

  public GrafoDependenciaResult historiaUsuarioGrafoDependencia() {
    Grafos grafo = new Grafos<String>();

    for (HistoriaUsuario historiausuario : historiaUsuarioRepository.findAll()) {
      List<HistoriaUsuario> dependentes = this.historiaUsuarioRepository.findByDepedentes(historiausuario.getId());
      grafo.adicionarVertice(historiausuario.getTitulo());

      for (HistoriaUsuario dependente : dependentes) {
        grafo.adicionarVertice(dependente.getTitulo());
        grafo.adicionarAresta(historiausuario.getTitulo(), dependente.getTitulo());
      }

    }

    return new GrafoDependenciaResult(grafo.temCiclo(), grafo.ordenacaoTopologica());
  }

  public GrafoDependenciaResult tarefaGrafoDependencia() {
    Grafos grafo = new Grafos<String>();

    for (Tarefa tarefa : tarefaRepository.findAll()) {
      List<Tarefa> dependentes = this.tarefaRepository.findByDepedentes(tarefa.getId());
      grafo.adicionarVertice(tarefa.getTitulo());

      for (Tarefa dependente : dependentes) {
        grafo.adicionarVertice(dependente.getTitulo());
        grafo.adicionarAresta(tarefa.getTitulo(), dependente.getTitulo());
      }

    }

    return new GrafoDependenciaResult(grafo.temCiclo(), grafo.ordenacaoTopologica());
  }

}
