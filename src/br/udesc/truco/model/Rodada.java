package br.udesc.truco.model;

import java.util.ArrayList;
import java.util.List;

//cada rodada é composta por duas jogadas, uma de cada jogador, apos cada jogador jogar uma vez, a rodada fica completa.
public class Rodada {
    //jogadas individuais de cada um dos jogadores
    public static class Jogada {
        public final Jogador jogador;
        public final Carta carta;

        public Jogada(Jogador j, Carta c) {
            jogador = j;
            carta = c;
        }
    }

    private final List<Jogada> jogadas = new ArrayList<>();

    public void adicionarJogada(Jogador j, Carta c) {
        jogadas.add(new Jogada(j, c));
    }

    public List<Jogada> getJogadas() {
        return jogadas;
    }

    public boolean completa() {
        return jogadas.size() == 2;
    }
}
