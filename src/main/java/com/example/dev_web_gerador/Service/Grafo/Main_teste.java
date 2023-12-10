package com.example.dev_web_gerador.Service.Grafo;

public class Main_teste {

    // classe main de teste, inserção de vértices e arestas
    public static void main(String[] args) {
        Grafos<String> grafos = new Grafos<String>();
        grafos.adicionarVertice("1");
        grafos.adicionarVertice("2");
        grafos.adicionarVertice("3");
        grafos.adicionarVertice("4");
        grafos.adicionarVertice("5");
        grafos.adicionarVertice("6");
        grafos.adicionarVertice("7");
        grafos.adicionarVertice("8");
        grafos.adicionarVertice("10");

        grafos.adicionarAresta("1", "4");
        grafos.adicionarAresta("1", "5");
        grafos.adicionarAresta("2", "5");
        grafos.adicionarAresta("4", "5");
        grafos.adicionarAresta("4", "7");
        grafos.adicionarAresta("6", "7");
        grafos.adicionarAresta("6", "8");
        grafos.adicionarAresta("8", "10");
        grafos.adicionarAresta("7", "10");
        grafos.adicionarAresta("3", "6");
        grafos.adicionarAresta("5", "3");

        // prints do grafo, função de verificação de ciclo e a ordenação topológica
        System.out.println("Grupo: Hanna Leticia, Isabella Bissoli, Marcela Starling e Victor Oliveira");

        System.out.println("-----Informações do grafo escolhido----");
        grafos.imprimirGrafo();

        System.out.println("O grafo é:" + grafos.temCiclo());
        if (grafos.temCiclo()) {
            System.out.println("Não dá para fazer ordenação, pois é um grafo cíclico");
        } else {
            System.out.println("Uma possível ordenação topológica é:" + grafos.ordenacaoTopologica());
        }

    }

}