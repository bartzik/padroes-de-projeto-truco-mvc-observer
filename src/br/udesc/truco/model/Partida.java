package br.udesc.truco.model;


import br.udesc.truco.util.Observador;

import java.util.*;

//objeto que esta sendo observado
public class Partida {
    private final Jogador jogador1, jogador2;
    private final List<Rodada> rodadas = new ArrayList<>();
    private final List<Jogador> vencedoresDasRodadas = new ArrayList<>(); // null = empate
    private Carta vira;
    private ValorCarta manilha;
    private int valorMao = 1;
    private boolean encerrada = false;
    private Jogador vencedorDaPartida = null;
    private int metaPontos = 12;
    private valorRodada valorRodadaAtual = valorRodada.NENHUM;
    private boolean pedidoTrucoPendente = false;
    private valorRodada proximaEtapa = valorRodada.TRUCO;
    private Jogador solicitanteAposta = null;
    private Jogador jogadorRespondeAposta = null;

    public Partida(Jogador j1, Jogador j2) {
        this.jogador1 = j1;
        this.jogador2 = j2;
    }

    //apostas truco/6/9/12
    public enum valorRodada {NENHUM, TRUCO, SEIS, NOVE, DOZE}

    public valorRodada getEtapaAtual() {
        return valorRodadaAtual;
    }

    public boolean isPedidoTrucoPendente() {
        return pedidoTrucoPendente;
    }

    public valorRodada getProximaEtapa() {
        return proximaEtapa;
    }

    public Jogador getSolicitanteAposta() {
        return solicitanteAposta;
    }

    public Jogador getJogadorRespondeAposta() {
        return jogadorRespondeAposta;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public List<Jogador> getVencedoresDasRodadas() {
        return vencedoresDasRodadas;
    }

    public Carta getVira() {
        return vira;
    }

    public ValorCarta getManilha() {
        return manilha;
    }

    public int getValorMao() {
        return valorMao;
    }

    public boolean isEncerrada() {
        return encerrada;
    }

    public Jogador getVencedorDaPartida() {
        return vencedorDaPartida;
    }

    public int getMetaPontos() {
        return metaPontos;
    }

    public void setMetaPontos(int m) {
        metaPontos = m;
        notificar();
    }

    public void setVira(Carta v) {
        vira = v;
        manilha = ValorCarta.defineManilha(v.getValorCarta());
        notificar();
    }

    public void setValorMao(int v) {
        valorMao = v;
        notificar();
    }

    public void iniciarNovaMao() {
        valorRodadaAtual = valorRodada.NENHUM;
        pedidoTrucoPendente = false;
        proximaEtapa = valorRodada.TRUCO;
        solicitanteAposta = null;
        jogadorRespondeAposta = null;
        valorMao = 1;
        rodadas.clear();
        vencedoresDasRodadas.clear();
        rodadas.add(new Rodada());
        notificar();
    }

    public Rodada rodadaAtual() {
        if (rodadas.isEmpty()) rodadas.add(new Rodada());
        return rodadas.get(rodadas.size() - 1);
    }

    public void novaRodada() {
        rodadas.add(new Rodada());
        notificar();
    }

    public void registrarResultadoRodada(Jogador vencedorOuNull) {
        vencedoresDasRodadas.add(vencedorOuNull);
        notificar();
    }

    public void encerrarPartida(Jogador vencedor) {
        this.encerrada = true;
        this.vencedorDaPartida = vencedor;
        notificar();
    }

    public void reiniciarPartida() {
        jogador1.adicionarPontos(-jogador1.getPontos());
        jogador2.adicionarPontos(-jogador2.getPontos());
        valorMao = 1;
        encerrada = false;
        vencedorDaPartida = null;
        rodadas.clear();
        vencedoresDasRodadas.clear();
        notificar();
    }

    public static int valorDaRodada(valorRodada e) {
        switch (e) {
            case NENHUM:
                return 1;
            case TRUCO:
                return 3;
            case SEIS:
                return 6;
            case NOVE:
                return 9;
            case DOZE:
                return 12;
        }
        return 1;
    }

    public static valorRodada proximaValorRodada(valorRodada e) {
        switch (e) {
            case NENHUM:
                return valorRodada.TRUCO;
            case TRUCO:
                return valorRodada.SEIS;
            case SEIS:
                return valorRodada.NOVE;
            case NOVE:
                return valorRodada.DOZE;
            case DOZE:
                return valorRodada.DOZE;
        }
        return valorRodada.TRUCO;
    }

    public void trucar(Jogador solicitanteAposta) {
        this.pedidoTrucoPendente = true;
        this.solicitanteAposta = solicitanteAposta;
        this.jogadorRespondeAposta = (solicitanteAposta == jogador1 ? jogador2 : jogador1);
        this.proximaEtapa = proximaValorRodada(this.valorRodadaAtual);
        setValorMao(valorDaRodada(this.proximaEtapa));
        notificar();
    }

    public void aceitarPedidoTruco() {
        if (!pedidoTrucoPendente) return;
        this.valorRodadaAtual = this.proximaEtapa;
        this.pedidoTrucoPendente = false;
        this.solicitanteAposta = null;
        this.jogadorRespondeAposta = null;
        setValorMao(valorDaRodada(this.valorRodadaAtual));
        notificar();
    }

    public void limparPedido() {
        this.pedidoTrucoPendente = false;
        this.solicitanteAposta = null;
        this.jogadorRespondeAposta = null;
        this.proximaEtapa = proximaValorRodada(this.valorRodadaAtual);
        notificar();
    }

    private final List<Observador> observadores = new ArrayList<>();

    public void adicionarObservador(Observador o) {
        observadores.add(o);
    }

    public void removerObservador(Observador o) {
        observadores.remove(o);
    }

    public void notificar() {
        for (Observador o : observadores) o.atualizar();
    }

}
