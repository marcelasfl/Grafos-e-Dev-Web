package com.example.dev_web_gerador.Service.Grafo;

import java.util.ArrayList;

public class Vertice<TIPO> {
    private TIPO dado;
    private ArrayList<Aresta<TIPO>> arestasSaida;

    public Vertice(TIPO valor) {
        this.dado = valor;
        this.arestasSaida = new ArrayList<Aresta<TIPO>>(); // Salva as arestas que estão saindo de um determinado
                                                           // vértice para utilizarmos nas funções de Grafo
    }

    public TIPO getDado() {
        return dado;
    }

    public void setDado(TIPO dado) {
        this.dado = dado;
    }

    public void adicionarArestaSaida(Aresta<TIPO> aresta) {
        this.arestasSaida.add(aresta);
    }

    public ArrayList<Aresta<TIPO>> getArestasSaida() {
        return arestasSaida;
    }

}
