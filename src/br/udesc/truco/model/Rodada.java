package br.udesc.truco.model;

import java.util.ArrayList;
import java.util.List;

public class Rodada {

    public static class Jogada {
        private final Jogador jogador;
        private final Carta carta;

        public Jogada(Jogador jogador, Carta carta) {
            this.jogador = jogador;
            this.carta = carta;
        }

        public Jogador getJogador() {
            return jogador;
        }

        public Carta getCarta() {
            return carta;
        }
    }

    private final List<Jogada> jogadas = new ArrayList<>();

    
    public void adicionarJogada(Jogador jogador, Carta carta) {
        if (!completa()) {
            jogadas.add(new Jogada(jogador, carta));
        }
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

    
    public boolean completa() {
        return jogadas.size() == 2;
    }

    public void limpar() {
        jogadas.clear();
    }
}
