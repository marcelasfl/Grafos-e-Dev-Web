package com.example.dev_web_gerador.Service.Grafo;

import java.util.ArrayList;
import java.util.List;

public class Grafos<TIPO> {

    private ArrayList<Vertice<TIPO>> vertices;
    private ArrayList<Aresta<TIPO>> arestas;

    public Grafos() {
        this.vertices = new ArrayList<Vertice<TIPO>>();
        this.arestas = new ArrayList<Aresta<TIPO>>();

    }

    // Método para adicionar vértice ao grafo
    public void adicionarVertice(TIPO dado) {
        // Verifica se já existe um vértice com o mesmo dado
        if (getVertice(dado) == null) {
            Vertice<TIPO> novoVertice = new Vertice<TIPO>(dado);
            this.vertices.add(novoVertice);
        } else {
            System.out.println("Já existe um vértice com o dado " + dado);
        }
    }

    // Método para adicionar uma aresta ao grafo
    public void adicionarAresta(TIPO dadoInicio, TIPO dadoFim) {
        Vertice<TIPO> inicio = this.getVertice(dadoInicio);
        Vertice<TIPO> fim = this.getVertice(dadoFim);
        Aresta<TIPO> aresta = new Aresta<TIPO>(inicio, fim); // Cria uma nova aresta ligando o vértice de início ao
                                                             // vértice de fim
        inicio.adicionarArestaSaida(aresta); // Adiciona a aresta à lista de arestas de saída do vértice de início
        this.arestas.add(aresta); // Adiciona a aresta a nossa lista global de arestas do grafo
    }

    // Método para buscar um vértice com base no dado associado
    public Vertice<TIPO> getVertice(TIPO dado) { // busca por vertice
        Vertice<TIPO> vertice = null;

        // Itera sobre a lista de vértices para encontrar o vértice com o dado fornecido
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getDado().equals(dado)) {
                // Atribui o vértice atual à variável 'vertice' e encerra o loop
                vertice = this.vertices.get(i);
                break;
            }
        }
        return vertice;
    }

    // Método para verificar se o grafo possui ciclos
    public boolean temCiclo() {
        List<Vertice<TIPO>> pilhaRecursao = new ArrayList<>();

        for (Vertice<TIPO> vertice : this.vertices) {
            if (temCicloRecursivo(vertice, new ArrayList<>(), pilhaRecursao)) { // Chama o método auxiliar para
                                                                                // verificar ciclos a partir do vértice
                                                                                // atual
                return true;
            }
        }

        return false;
    }

    // Método auxiliar recursivo para para verificar se o grafo possui ciclo
    private boolean temCicloRecursivo(Vertice<TIPO> vertice, List<Vertice<TIPO>> visitados,
            List<Vertice<TIPO>> pilhaRecursao) {
        if (pilhaRecursao.contains(vertice)) {
            // Se o vértice já está na pilha de recursão, então há um ciclo
            return true;
        }

        if (visitados.contains(vertice)) {
            // Se o vértice já foi visitado, não há necessidade de explorá-lo novamente
            return false;
        }

        // Marca o vértice como visitado e o adiciona à pilha de recursão
        visitados.add(vertice);
        pilhaRecursao.add(vertice);

        // Explora os vizinhos do vértice
        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            Vertice<TIPO> vizinho = aresta.getFim();
            if (temCicloRecursivo(vizinho, visitados, pilhaRecursao)) {
                return true;
            }
        }

        // Remove o vértice da pilha de recursão após explorar todos os seus vizinhos
        pilhaRecursao.remove(vertice);

        return false;

    }

    // Método para realizar a ordenação topológica do grafo
    public List<TIPO> ordenacaoTopologica() {
        List<Vertice<TIPO>> ordenacao = new ArrayList<>(); // vai ser a lista de epicos,HU ou Tarefa
        List<Vertice<TIPO>> visitados = new ArrayList<>(); // vai ser a lista de dependencia

        for (Vertice<TIPO> vertice : this.vertices) {
            if (!visitados.contains(vertice)) { // Verifica se o vértice não foi visitado ainda
                ordenacaoTopologicaRecursivo(vertice, visitados, ordenacao);
            }
        }

        // Criando uma lista para armazenar o resultado da ordenação topológica
        List<TIPO> resultado = new ArrayList<>();

        // Adicionando à lista
        for (int i = ordenacao.size() - 1; i >= 0; i--) {
            resultado.add(ordenacao.get(i).getDado());
        } // Revertendo a lista de ordenação e colocando os vértices do final-começo para
          // ordenar corretamente

        return resultado;
    }

    // Método auxiliar recursivo para realizar a ordenação topológica a partir de um
    // vértice
    private void ordenacaoTopologicaRecursivo(Vertice<TIPO> vertice, List<Vertice<TIPO>> visitados,
            List<Vertice<TIPO>> ordenacao) {
        visitados.add(vertice);

        for (Aresta<TIPO> aresta : vertice.getArestasSaida()) {
            // Obtém o vértice vizinho ao longo da aresta
            Vertice<TIPO> vizinho = aresta.getFim();
            if (!visitados.contains(vizinho)) {
                ordenacaoTopologicaRecursivo(vizinho, visitados, ordenacao);
            }
        }

        // Adiciona o vértice à lista
        ordenacao.add(vertice);
    }

    // Método criado apenas para verificar se o grafo está sendo construído
    // corretamente
    public void imprimirGrafo() {
        System.out.println("Vértices:");
        for (Vertice<TIPO> vertice : this.vertices) {
            System.out.println(vertice.getDado());
        }

        System.out.println("Arestas:");
        for (Aresta<TIPO> aresta : this.arestas) {
            System.out.println(aresta.getInicio().getDado() + " -> " + aresta.getFim().getDado());
        }
    }

}