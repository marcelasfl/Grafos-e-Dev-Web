/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dev_web_gerador.lib;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.example.dev_web_gerador.Model.Epico;

import jakarta.transaction.Transactional;

/**
 *
 * @author Victor, Marcela, Isabella, Hanna
 */

public class ArvoreBinariaExemplo<T> implements IArvoreBinaria<T> {
    // public class ArvoreBinariaExemplo<T extends Comparable<T>> implements
    // IArvoreBinaria<T> {

    protected No<T> raiz = null;
    protected Comparator<T> comparador;

    protected No<T> atual = null;
    private Stack<No<T>> pilha = new Stack<>();

    public ArvoreBinariaExemplo(Comparator<T> comp) {
        comparador = comp;
    }

    public void adicionar(T novoValor) {
        raiz = adicionarRecursivamente(raiz, novoValor);
    }

    private No<T> adicionarRecursivamente(No<T> noAtual, T novoValor) {
        if (noAtual == null) {
            return new No<T>(novoValor);
        }

        // Comparar o novo valor com o valor do nó atual para determinar o
        // ramo onde o novo valor deve ser adicionado.
        int comparacao = comparador.compare(novoValor, noAtual.getValor());
        if (comparacao < 0) { //
            noAtual.setFilhoEsquerda(adicionarRecursivamente(noAtual.getFilhoEsquerda(), novoValor));
        } else if (comparacao > 0) {
            noAtual.setFilhoDireita(adicionarRecursivamente(noAtual.getFilhoDireita(), novoValor));
        } else {
            System.out.println("valor duplicado");
            return noAtual;
        }

        return noAtual;
    }

    @Override
    public T pesquisar(T valor) {
        if (valor == null) {
            return null;
        } else {
            return pesquisarRecursivo(valor, raiz);
        }
    }

    private T pesquisarRecursivo(T valor, No<T> no) {
        if (no == null) {
            return null;
        }

        int comparacao = comparador.compare(valor, no.getValor());

        if (comparacao == 0) {
            return no.getValor();
        } else if (comparacao < 0) {
            return pesquisarRecursivo(valor, no.getFilhoEsquerda());
        } else {
            return pesquisarRecursivo(valor, no.getFilhoDireita());
        }
    }

    @Override
    public void remover(T valor) {
        this.raiz = removerRecursivo(this.raiz, valor);
    }

    private No<T> removerRecursivo(No<T> no, T valor) {
        if (no == null) {
            return no;
        }
        // primeira caso: nao tem nenhum nó na arvore

        if (this.comparador.compare(valor, no.getValor()) < 0) {
            no.setFilhoEsquerda(removerRecursivo(no.getFilhoEsquerda(), valor));
        } else if (this.comparador.compare(valor, no.getValor()) > 0) {
            no.setFilhoDireita(removerRecursivo(no.getFilhoDireita(), valor));
        } else {
            // Caso 2: Nó sem filhos ou com apenas um filho
            if (no.getFilhoEsquerda() == null) {
                return no.getFilhoDireita();
            } else if (no.getFilhoDireita() == null) {
                return no.getFilhoEsquerda();
            }

            // Caso 3: Nó com dois filhos - Pegando o mínimo do lado direito
            no.setValor(encontrarMinimo(no.getFilhoDireita()).getValor());
            no.setFilhoDireita(removerRecursivo(no.getFilhoDireita(), no.getValor()));
        }
        return no;
    }

    public No<T> encontrarMinimo(No<T> no) {
        No<T> atual = no;
        while (atual.getFilhoEsquerda() != null) {
            atual = atual.getFilhoEsquerda();
        }
        return atual;
    }

    @Override
    public int altura() {
        return calcAltura(raiz);
    }

    private int calcAltura(No<T> no) {
        if (no == null) {
            return -1;
        }

        if (no.getFilhoEsquerda() == null && no.getFilhoDireita() == null) {
            return 0;
        }

        int esquerda = calcAltura(no.getFilhoEsquerda());
        int direita = calcAltura(no.getFilhoDireita());

        return Math.max(esquerda, direita) + 1;
    }

    @Override
    public int quantidadeNos() {
        return quantidadeNos(raiz);
    }

    @Override
    public int quantidadeNos(No<T> noAtual) {
        if (noAtual == null) {
            return 0;
        } else {
            return 1 + quantidadeNos(noAtual.getFilhoDireita()) + quantidadeNos(noAtual.getFilhoEsquerda());
        }
    }

    @Transactional
    @Override
    public String caminharEmNivel() {
        if (raiz == null) {
            return "[]"; // caso de arvore vazia
        }

        StringBuilder result = new StringBuilder("[");
        Queue<No<T>> fila = new LinkedList<>();
        fila.offer(raiz); // Adiciona o nó raiz à fila.

        while (!fila.isEmpty()) {
            No<T> noAtual = fila.poll(); // Remove o nó da fila.

            result.append(noAtual.getValor().toString()).append(", \n"); // Adiciona o valor do nó à saída.

            if (noAtual.getFilhoEsquerda() != null) {
                fila.offer(noAtual.getFilhoEsquerda()); // Adiciona o filho esquerdo à fila.
            }

            if (noAtual.getFilhoDireita() != null) {
                fila.offer(noAtual.getFilhoDireita()); // Adiciona o filho direito à fila.
            }
        }

        // finalzinho da fila
        result.setLength(result.length() - 2);
        result.append("]");

        return result.toString();
    }

    @Override
    public String caminharEmOrdem() {
        if (raiz == null) {
            return "[]"; // Retorna uma string vazia se a árvore estiver vazia.
        }

        StringBuilder resultado = new StringBuilder("[");
        caminharEmOrdemRecursivo(raiz, resultado);
        resultado.setLength(resultado.length() - 2);
        resultado.append("]");

        return resultado.toString();
    }

    private void caminharEmOrdemRecursivo(No<T> no, StringBuilder resultado) {
        if (no == null) {
            return;
        }
        caminharEmOrdemRecursivo(no.getFilhoEsquerda(), resultado);

        resultado.append(no.getValor().toString()).append(", \n");

        caminharEmOrdemRecursivo(no.getFilhoDireita(), resultado);
    }

    @Override
    public T obterProximo() {
        while (atual != null || !pilha.isEmpty()) {
            while (atual != null) {
                pilha.push(atual); // Adicionando o valor atual na pilha
                atual = atual.getFilhoEsquerda(); // Pegando todos os filhos à esquerda
            }

            atual = pilha.pop(); // Desempilhando o último valor à esquerda e igualando ao atual
            T valor = atual.getValor();
            atual = atual.getFilhoDireita();

            return valor;
        }

        return null;
    }

    @Override
    public void reiniciarNavegacao() {
        pilha.clear(); // esvazia a pilha
        atual = null; // retorna o valor do no atual para nulo
    }

    // Tentativa de imprimir a árvore
    public Epico imprimirArvore() {
        System.out.println("Árvore Binária:");
        imprimirArvoreRecursivamente(raiz, 0);
        return null;
    }

    private void imprimirArvoreRecursivamente(No<T> no, int nivel) {
        if (no != null) {
            imprimirArvoreRecursivamente(no.getFilhoDireita(), nivel + 1);

            for (int i = 0; i < nivel; i++) {
                System.out.print("  "); // Adiciona espaços para representar o nível na árvore
            }
            System.out.println(no.toString()); // Imprime o valor do nó usando toString

            imprimirArvoreRecursivamente(no.getFilhoEsquerda(), nivel + 1);
        }
    }

    public T pesquisarPorId(long id) { // Pesquisando pelo id
        if (id <= 0) {
            return null;
        } else {
            return pesquisarRecursivoPorId(id, raiz);
        }
    }

    private T pesquisarRecursivoPorId(long id, No<T> no) {
        if (no == null) {
            return null;
        }

        if (id == ((Epico) no.getValor()).getId()) { // Verifica se o no atual corresponde ao id passado como parametro
            return no.getValor();
        } else if (id < ((Epico) no.getValor()).getId()) { // Verificando se o valor do id é menor, se for, a busca vai
                                                           // para o lado esquerdo
            return pesquisarRecursivoPorId(id, no.getFilhoEsquerda());
        } else {
            return pesquisarRecursivoPorId(id, no.getFilhoDireita()); // Senão vai para o direito
        }
    }

    public T removerPorId(long id) { // Remoção de um nó com base no id
        if (id <= 0) {
            return null;
        } else {
            T epicoRemovido = removerRecursivoPorId(raiz, id).getValor();

            return epicoRemovido;
        }
    }

    private No<T> removerRecursivoPorId(No<T> no, long id) {
        if (no == null) {
            return null; // Se o nó for nulo, o épico não foi encontrado
        }
        if (id == ((Epico) no.getValor()).getId()) { // Verifica se o ID do nó atual corresponde ao id do nó fornecido

            if (no.getFilhoEsquerda() == null) {// Verificca se o nó tem filho a esuerda , retornando o filho a direita
                return no.getFilhoDireita();
                // Se não tiver na direita retorna o da esquerda
            } else if (no.getFilhoDireita() == null) {
                return no.getFilhoEsquerda();
            }

            no.setValor(encontrarMinimo(no.getFilhoDireita()).getValor()); // Define o atual como valor mínimo do lado
                                                                           // direito da subarvore, encontrando o minimo
                                                                           // com a função
            no.setFilhoDireita(removerRecursivoPorId(no.getFilhoDireita(), ((Epico) no.getValor()).getId())); // Remove
                                                                                                              // esse
                                                                                                              // valor
                                                                                                              // minimo
                                                                                                              // da
                                                                                                              // subarvore

            return no; // Retorna o nó atualizado
        }

        if (id < ((Epico) no.getValor()).getId()) { // Verifica se o id é menor que o id do nó atual, para ontinuar a
                                                    // recurção do lado esquerdo
            no.setFilhoEsquerda(removerRecursivoPorId(no.getFilhoEsquerda(), id));
        } else {
            no.setFilhoDireita(removerRecursivoPorId(no.getFilhoDireita(), id)); // Senão, continua no lado direito
        }

        return no;
    }

}
