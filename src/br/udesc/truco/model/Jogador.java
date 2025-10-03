package br.udesc.truco.model;

public class Jogador {
    private final String nome;
    private int pontos = 0;
    private final Mao mao = new Mao();

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void adicionarPontos(int p) {
        pontos += p;
    }

    public Mao getMao() {
        return mao;
    }
}
