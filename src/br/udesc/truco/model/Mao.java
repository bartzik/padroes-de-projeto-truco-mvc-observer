package br.udesc.truco.model;

import java.util.ArrayList;
import java.util.List;

public class Mao {
    private final List<Carta> cartas = new ArrayList<>();

    public void adicionar(Carta c) {
        cartas.add(c);
    }

    public Carta jogar(int indice) {
        return cartas.remove(indice);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public boolean vazia() {
        return cartas.isEmpty();
    }

    public void limpar() {
        cartas.clear();
    }
}
