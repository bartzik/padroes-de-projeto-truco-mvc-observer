package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.Jogador;
import br.udesc.truco.model.Partida;
import br.udesc.truco.util.Observador;

import javax.swing.*;
import java.awt.*;

public class Acoes extends JPanel implements Observador {
    private final JogoController jogoController;
    private final Partida partida;

    private final JButton btnTruco = new JButton("Pedir Truco");
    private final JButton btnAceitar = new JButton("Aceitar");
    private final JButton btnCorrer = new JButton("Correr");
    private final JButton btnJogarNovamente = new JButton("Jogar novamente");

    public Acoes(JogoController jogoController, Partida partida) {
        this.jogoController = jogoController;
        this.partida = partida;

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(btnTruco);
        add(btnAceitar);
        add(btnCorrer);
        add(btnJogarNovamente);

        btnTruco.addActionListener(e -> jogoController.trucar(jogoController.getJogadorAtual()));
        btnAceitar.addActionListener(e -> {
            Jogador atual = jogoController.getJogadorAtual();
            Jogador oponente = (atual == partida.getJogador1() ? partida.getJogador2() : partida.getJogador1());
            jogoController.aceitarTruco(oponente);
        });
        btnCorrer.addActionListener(e -> {
            Jogador atual = jogoController.getJogadorAtual();
            Jogador oponente = (atual == partida.getJogador1() ? partida.getJogador2() : partida.getJogador1());
            jogoController.correrTruco(oponente);
        });
        btnJogarNovamente.addActionListener(e -> jogoController.reiniciarPartida());

        btnJogarNovamente.setVisible(false);

        //adicionando como observador
        partida.adicionarObservador(this);
        atualizar();

        btnTruco.setEnabled(false);
        btnAceitar.setEnabled(false);
        btnCorrer.setEnabled(false);
    }

    @Override
    public void atualizar() {
        boolean partidaEncerrada = partida.isEncerrada();
        boolean pedidoTrucoPendente = partida.isPedidoTrucoPendente();
        boolean habilitaBotaoTruco = !partidaEncerrada && !pedidoTrucoPendente && partida.getEtapaAtual() != Partida.valorRodada.DOZE;
        boolean partidaValendoTruco = pedidoTrucoPendente && partida.getJogadorRespondeAposta() != null;

        btnTruco.setEnabled(habilitaBotaoTruco);
        btnAceitar.setEnabled(!partidaEncerrada && partidaValendoTruco);
        btnCorrer.setEnabled(!partidaEncerrada && partidaValendoTruco);
        btnJogarNovamente.setVisible(partidaEncerrada);

        // atualiza botão de pedir truco
        Partida.valorRodada prox = Partida.proximaValorRodada(partida.getEtapaAtual());
        String valorBotaoTruco;

        switch (prox) {
            case TRUCO:
                valorBotaoTruco = "Truco";
                break;
            case SEIS:
                valorBotaoTruco = "Seis";
                break;
            case NOVE:
                valorBotaoTruco = "Nove";
                break;
            case DOZE:
            default:
                valorBotaoTruco = "Doze";
                break;
        }

        btnTruco.setText(valorBotaoTruco);

        revalidate();
        repaint();
    }
}
