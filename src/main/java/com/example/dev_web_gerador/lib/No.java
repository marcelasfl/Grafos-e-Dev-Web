/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dev_web_gerador.lib;

/**
 *
 * @author victoriocarvalho
 */
public class No<T> {
    private T valor;
    private No<T> filhoEsquerda;
    private No<T> filhoDireita;
    private int altura; // Adicionamos a altura como um atributo

    public No(T valor) {
        this.valor = valor;
        this.altura = 1; // A altura começa em 1 para um novo nó
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public No<T> getFilhoEsquerda() {
        return filhoEsquerda;
    }

    public void setFilhoEsquerda(No<T> filhoEsquerda) {
        this.filhoEsquerda = filhoEsquerda;
    }

    public No<T> getFilhoDireita() {
        return filhoDireita;
    }

    public void setFilhoDireita(No<T> filhoDireita) {
        this.filhoDireita = filhoDireita;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;

    }
    
    
}
