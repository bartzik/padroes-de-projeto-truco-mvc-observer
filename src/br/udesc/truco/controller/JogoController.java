package br.udesc.truco.controller;

import br.udesc.truco.model.*;

public class JogoController {
    private final Partida partida;
    private Baralho baralho;
    private Jogador jogadorAtual;
    private Jogador proximoQueComeca;

    public JogoController(Partida p) {
        this.partida = p;
        this.proximoQueComeca = p.getJogador1();
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
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public void alternarJogadorDaVez() {
        jogadorAtual = (jogadorAtual == partida.getJogador1() ? partida.getJogador2() : partida.getJogador1());
    }

    private int forcaCarta(Carta c, ValorCarta manilha) {
        boolean ehManilha = c.getValorCarta() == manilha;
        if (ehManilha) {
            return 100 + c.getNaipe().ordemManilha();
        }
        return c.getValorCarta().getValorDaCarta();
    }

    private Jogador decidirVencedorDaRodada(Rodada r, ValorCarta manilha) {
        Rodada.Jogada a = r.getJogadas().get(0);
        Rodada.Jogada b = r.getJogadas().get(1);
        int fa = forcaCarta(a.carta, manilha);
        int fb = forcaCarta(b.carta, manilha);
        if (fa > fb) return a.jogador;
        if (fb > fa) return b.jogador;
        return null; // empate
    }

    public void jogarCarta(Jogador j, int indice) {
        if (partida.isEncerrada()) return;
        if (partida.isEncerrada()) return;
        if (j != jogadorAtual) return;
        Carta c = j.getMao().jogar(indice);
        partida.rodadaAtual().adicionarJogada(j, c);
        alternarJogadorDaVez();
        if (partida.rodadaAtual().completa()) {
            Jogador vencedor = decidirVencedorDaRodada(partida.rodadaAtual(), partida.getManilha());
            partida.registrarResultadoRodada(vencedor);
            if (vencedor != null) jogadorAtual = vencedor;
            if (encerrarMaoSeNecessario()) {
                return;
            }
            if (!partida.getJogador1().getMao().vazia()) {
                partida.novaRodada();
            }
        } else {
            partida.notificar();
        }
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
            Jogador w1 = partida.getVencedoresDasRodadas().get(0);
            Jogador w2 = partida.getVencedoresDasRodadas().get(1);
            Jogador w3 = partida.getVencedoresDasRodadas().get(2);
            if (ganhos1 > ganhos2) {
                terminarMao(j1);
                return true;
            }
            if (ganhos2 > ganhos1) {
                terminarMao(j2);
                return true;
            }
            if (w3 == null) {
                if (w1 != null) {
                    terminarMao(w1);
                    return true;
                }
                if (w2 != null) {
                    terminarMao(w2);
                    return true;
                }
                terminarMao(proximoQueComeca);
                return true;
            } else {
                terminarMao(w3);
                return true;
            }
        }
        return false;
    }

    private void terminarMao(Jogador vencedor) {
        vencedor.adicionarPontos(partida.getValorMao());
        // Verifica se a partida acabou
        if (vencedor.getPontos() >= partida.getMetaPontos()) {
            partida.encerrarPartida(vencedor);
            return;
        }
        // se nao acabou, comeca outra mao
        proximoQueComeca = vencedor;
        novaMao(proximoQueComeca);
    }

    public void reiniciarPartida() {
        partida.reiniciarPartida();
        // quem começa a nova partida pode ser Jogador 1 por padrão
        proximoQueComeca = partida.getJogador1();
        // recomeça normalmente
        novaMao(proximoQueComeca);
    }

    // --- APOSTAS (Truco/6/9/12) ---
    private boolean podePedirAposta(Jogador j) {
        if (partida.isEncerrada()) return false;
        if (partida.isPedidoTrucoPendente()) return false;
        if (partida.getEtapaAtual() == Partida.valorRodada.DOZE) return false;
        return j == getJogadorAtual();
    }

    public void trucar(Jogador j) {
        if (!podePedirAposta(j)) return;
        partida.trucar(j);
        // opcional: dialogo
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null,
                    j.getNome() + " pediu " + proximaEtapaRotulo() + " (vale " + Partida.valorDaRodada(partida.getProximaEtapa()) + ")!",
                    "Aposta", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private String proximaEtapaRotulo() {
        switch (partida.getProximaEtapa()) {
            case TRUCO:
                return "Truco";
            case SEIS:
                return "Seis";
            case NOVE:
                return "Nove";
            case DOZE:
                return "Doze";
            default:
                return "";
        }
    }

    public void aceitarTruco(Jogador j) {
        if (!partida.isPedidoTrucoPendente()) return;
        if (j != partida.getJogadorRespondeAposta()) return;
        partida.aceitarPedidoTruco();
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Aposta aceita! Agora vale " + partida.getValorMao(),
                    "Aposta", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void correrTruco(Jogador j) {
        if (!partida.isPedidoTrucoPendente()) return;
        if (j != partida.getJogadorRespondeAposta()) return;
        Jogador desafiante = partida.getSolicitanteAposta();
        Partida.valorRodada anterior = partida.getEtapaAtual();
        int pontos = Partida.valorDaRodada(anterior);
        int valorOriginal = partida.getValorMao();
        partida.setValorMao(pontos);
        terminarMao(desafiante);
        partida.setValorMao(valorOriginal);
        partida.limparPedido();
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JOptionPane.showMessageDialog(null,
                    j.getNome() + " correu. " + desafiante.getNome() + " leva " + pontos + " ponto(s).",
                    "Aposta", javax.swing.JOptionPane.WARNING_MESSAGE);
        });
    }
}
