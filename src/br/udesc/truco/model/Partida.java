package br.udesc.truco.model;

import br.udesc.truco.controller.Observer;
import java.util.*;

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
    private int valorMaoAntesDoPedido = 1;

    private final List<Observer> observers = new ArrayList<>();

    public enum valorRodada {NENHUM, TRUCO, SEIS, NOVE, DOZE}

    public Partida(Jogador j1, Jogador j2) {
        this.jogador1 = j1;
        this.jogador2 = j2;
        rodadas.add(new Rodada());
    }

    public Jogador getJogador1() { return jogador1; }
    public Jogador getJogador2() { return jogador2; }
    public List<Rodada> getRodadas() { return rodadas; }
    public List<Jogador> getVencedoresDasRodadas() { return vencedoresDasRodadas; }
    public Carta getVira() { return vira; }
    public ValorCarta getManilha() { return manilha; }
    public int getValorMao() { return valorMao; }
    public boolean isEncerrada() { return encerrada; }
    public Jogador getVencedorDaPartida() { return vencedorDaPartida; }
    public int getMetaPontos() { return metaPontos; }
    public valorRodada getEtapaAtual() { return valorRodadaAtual; }
    public boolean isPedidoTrucoPendente() { return pedidoTrucoPendente; }
    public valorRodada getProximaEtapa() { return proximaEtapa; }
    public Jogador getSolicitanteAposta() { return solicitanteAposta; }
    public Jogador getJogadorRespondeAposta() { return jogadorRespondeAposta; }
    public Jogador getJogadorSolicitouTruco() { return solicitanteAposta; }

    
    public void setMetaPontos(int m) { metaPontos = m; notificar(); }
    public void setVira(Carta v) {
        vira = v;
        manilha = ValorCarta.defineManilha(v.getValorCarta());
        notificar();
    }
    public void setValorMao(int v) { valorMao = v; notificar(); }

    
    public void trucar(Jogador solicitante) {
        if (encerrada) return;
        
        valorMaoAntesDoPedido = valorMao;
        pedidoTrucoPendente = true;
        solicitanteAposta = solicitante;
        jogadorRespondeAposta = (solicitante == jogador1 ? jogador2 : jogador1);
        proximaEtapa = proximaValorRodada(valorRodadaAtual);
        valorMao = valorDaRodada(proximaEtapa);
        notificar();
    }

    public void aceitarPedidoTruco() {
        if (!pedidoTrucoPendente) return;

        valorRodadaAtual = proximaEtapa;
        valorMao = valorDaRodada(valorRodadaAtual);

        Jogador quemAceitou = jogadorRespondeAposta;
        Jogador quemResponde = solicitanteAposta;

        solicitanteAposta = quemAceitou;
        jogadorRespondeAposta = quemResponde;

        pedidoTrucoPendente = false;
        notificar();
    }


    public void limparPedido() {
        pedidoTrucoPendente = false;
        solicitanteAposta = null;
        jogadorRespondeAposta = null;
        proximaEtapa = proximaValorRodada(valorRodadaAtual);
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

    public void novaRodada() { rodadas.add(new Rodada()); notificar(); }
    public void registrarResultadoRodada(Jogador vencedorOuNull) { vencedoresDasRodadas.add(vencedorOuNull); notificar(); }

    public void encerrarPartida(Jogador vencedor) {
        encerrada = true;
        vencedorDaPartida = vencedor;
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
        rodadas.add(new Rodada());
        notificar();
    }

    public static int valorDaRodada(valorRodada e) {
        return switch (e) {
            case NENHUM -> 1;
            case TRUCO -> 3;
            case SEIS -> 6;
            case NOVE -> 9;
            case DOZE -> 12;
        };
    }

    public static valorRodada proximaValorRodada(valorRodada e) {
        return switch (e) {
            case NENHUM -> valorRodada.TRUCO;
            case TRUCO -> valorRodada.SEIS;
            case SEIS -> valorRodada.NOVE;
            case NOVE -> valorRodada.DOZE;
            case DOZE -> valorRodada.DOZE;
        };
    }

    public void anexar(Observer o) { observers.add(o); }
    public void removerObserver(Observer o) { observers.remove(o); }
    public void notificar() { observers.forEach(Observer::atualizar); }

    public void setPedidoTrucoPendente(boolean b) {
        this.pedidoTrucoPendente = b;
        notificar();
    }

    public void setSolicitanteAposta(Jogador novoSolicitante) {
        this.solicitanteAposta = novoSolicitante;
        notificar();
    }

    public void setJogadorRespondeAposta(Jogador novoRespondente) {
        this.jogadorRespondeAposta = novoRespondente;
        notificar();
    }

    public void setValorMaoAntesDoPedido(int valorAtual) {
        this.valorMaoAntesDoPedido = valorAtual;
        notificar();
    }

	public int getValorMaoAntesDoPedido() {
		return valorMaoAntesDoPedido;
	}

	public void setProximaEtapa(valorRodada etapaAtual) {
		this.proximaEtapa = etapaAtual;
	    notificar();
	}


}

