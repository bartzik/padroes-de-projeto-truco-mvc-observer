package br.udesc.truco.controller;

import br.udesc.truco.model.*;

import javax.swing.*;

public class JogoController {
    private final Partida partida;
    private Baralho baralho;
    private Jogador jogadorAtual;
    private Jogador proximoQueComeca;

    public JogoController(Partida partida) {
        this.partida = partida;
        this.proximoQueComeca = partida.getJogador1();
        this.jogadorAtual = proximoQueComeca;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    private void alternarJogadorDaVez() {
        jogadorAtual = (jogadorAtual == partida.getJogador1() ? partida.getJogador2() : partida.getJogador1());
    }

    
    public void iniciar() {
        novaMao(proximoQueComeca);
    }

    private void novaMao(Jogador quemComeca) {
        baralho = new Baralho();
        baralho.embaralhar();

        partida.getJogador1().getMao().limpar();
        partida.getJogador2().getMao().limpar();

        for (int i = 0; i < 3; i++) {
            partida.getJogador1().getMao().adicionar(baralho.comprar());
            partida.getJogador2().getMao().adicionar(baralho.comprar());
        }

        partida.setVira(baralho.comprar());
        jogadorAtual = quemComeca;

        partida.iniciarNovaMao();
        partida.notificar();
    }

    
    public void jogarCarta(Jogador jogador, int indiceCarta) {
        if (partida.isEncerrada() || jogador != jogadorAtual) return;

        Carta carta = jogador.getMao().getCartas().remove(indiceCarta);
        partida.rodadaAtual().adicionarJogada(jogador, carta);

        if (partida.rodadaAtual().completa()) {
            
            Jogador vencedorRodada = decidirVencedorDaRodada(partida.rodadaAtual(), partida.getManilha());
            partida.registrarResultadoRodada(vencedorRodada);

            
            if (vencedorRodada != null) {
                jogadorAtual = vencedorRodada;
            }

            
            if (encerrarMaoSeNecessario()) {
                return;
            }

            
            if (!partida.getJogador1().getMao().vazia()) {
                partida.novaRodada();
            }
        } else {
            alternarJogadorDaVez();
        }

        partida.notificar();
    }

    
    private int forcaCarta(Carta c, ValorCarta manilha) {
        if (c.getValorCarta() == manilha) {
            return 100 + c.getNaipe().ordemManilha(); 
        }
        return c.getValorCarta().getValorDaCarta();
    }

   
    private Jogador decidirVencedorDaRodada(Rodada r, ValorCarta manilha) {
        Rodada.Jogada a = r.getJogadas().get(0);
        Rodada.Jogada b = r.getJogadas().get(1);

        int fa = forcaCarta(a.getCarta(), manilha);
        int fb = forcaCarta(b.getCarta(), manilha);

        if (fa > fb) return a.getJogador();
        if (fb > fa) return b.getJogador();
        return null; 
    }

    
    private boolean encerrarMaoSeNecessario() {
        int ganhos1 = 0, ganhos2 = 0;
        Jogador j1 = partida.getJogador1();
        Jogador j2 = partida.getJogador2();

        for (Jogador v : partida.getVencedoresDasRodadas()) {
            if (v == j1) ganhos1++;
            else if (v == j2) ganhos2++;
        }

        if (ganhos1 == 2 || ganhos2 == 2) {
            terminarMao(ganhos1 > ganhos2 ? j1 : j2);
            return true;
        }

        if (partida.getVencedoresDasRodadas().size() >= 3) {
            if (ganhos1 > ganhos2) terminarMao(j1);
            else if (ganhos2 > ganhos1) terminarMao(j2);
            else {
                
                terminarMao(proximoQueComeca);
            }
            return true;
        }

        return false;
    }
    
    private void terminarMao(Jogador vencedor, boolean adicionarPontos) {
        if (vencedor == null) return;

        if (adicionarPontos) {
            vencedor.adicionarPontos(partida.getValorMao());
        }

        if (vencedor.getPontos() >= partida.getMetaPontos()) {
            partida.encerrarPartida(vencedor);
            return;
        }
        proximoQueComeca = vencedor;

        novaMao(proximoQueComeca);
    }


    
    private void terminarMao(Jogador vencedor) {
    	terminarMao(vencedor, true);
    }

   
    public void trucar(Jogador solicitante) {
        if (partida.isEncerrada()) return;
        
        int valorAtual = partida.getValorMao();
        Partida.valorRodada proximaEtapa = Partida.proximaValorRodada(partida.getEtapaAtual());
        
        partida.setValorMaoAntesDoPedido(valorAtual);
        partida.trucar(solicitante);
    }

    public void aceitarTruco(Jogador respondente) {
        if (!partida.isPedidoTrucoPendente()) return;
        if (respondente != partida.getJogadorRespondeAposta()) return;

        partida.aceitarPedidoTruco();
        JOptionPane.showMessageDialog(null,
                "Seu pedido foi aceito. Valor da mão: " + partida.getValorMao(),
                "Truco Aceito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void correrTruco(Jogador respondente) {
        if (!partida.isPedidoTrucoPendente()) return;
        if (respondente != partida.getJogadorRespondeAposta()) return;

        Jogador desafiante = partida.getSolicitanteAposta();
        int valorAnterior = partida.getValorMaoAntesDoPedido();

        partida.limparPedido();
        
        desafiante.adicionarPontos(valorAnterior);

        JOptionPane.showMessageDialog(null,
                respondente.getNome() + " correu. " + desafiante.getNome() + " leva " + valorAnterior + " ponto(s).",
                "Correr Truco",
                JOptionPane.WARNING_MESSAGE);

        terminarMao(desafiante, false);
    }

    public void subirTruco(Jogador respondente) {
        if (!partida.isPedidoTrucoPendente()) return;
        if (respondente != partida.getJogadorRespondeAposta()) return;

        int valorAtual = partida.getValorMao();
        int proximoValor = switch (valorAtual) {
            case 3 -> 6;
            case 6 -> 9;
            case 9 -> 12;
            default -> valorAtual;
        };
        partida.setValorMaoAntesDoPedido(valorAtual);
        partida.setValorMao(proximoValor);

        Partida.valorRodada etapaAtual = Partida.proximaValorRodada(partida.getEtapaAtual());
        partida.setProximaEtapa(etapaAtual);

        Jogador novoSolicitante = respondente;
        Jogador novoRespondente = (novoSolicitante == partida.getJogador1()) ? partida.getJogador2() : partida.getJogador1();

        partida.setPedidoTrucoPendente(true);
        partida.setSolicitanteAposta(novoSolicitante);
        partida.setJogadorRespondeAposta(novoRespondente);

        partida.notificar();
    }


   
    public void reiniciarPartida() {
        partida.reiniciarPartida();
        proximoQueComeca = partida.getJogador1();
        novaMao(proximoQueComeca);
    }
}


