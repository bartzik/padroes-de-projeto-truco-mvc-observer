package br.udesc.truco.model;

import java.util.*;

public class Baralho {
    private final List<Carta> cartas = new ArrayList<>();
    private final Random random = new Random();

    public Baralho() {
        for (Naipe n : Naipe.values()) {
            for (ValorCarta v : ValorCarta.values()) {
                cartas.add(new Carta(n, v));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(cartas, random);
    }

    public Carta comprar() {
        return cartas.remove(cartas.size() - 1);
    }

    public int tamanho() {
        return cartas.size();
    }
}
