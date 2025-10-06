package br.udesc.truco.view;

import br.udesc.truco.model.Partida;
import br.udesc.truco.util.CarregadorImagens;
import br.udesc.truco.controller.Observer;

import javax.swing.*;
import java.awt.*;

public class PainelPlacar extends JPanel implements Observer {
    private final Partida partida;
    private final JLabel label;
    private final JLabel labelVira = new JLabel();

    public PainelPlacar(Partida p) {
        this.partida = p;
        this.label = new JLabel();
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(label);
        add(labelVira);
        p.anexar(this);
        atualizar();
    }

    @Override
    public void atualizar() {
        // Atualiza a carta vira
        if (partida.getVira() != null) {
            labelVira.setIcon(CarregadorImagens.iconeCarta(
                    partida.getVira().getNaipe(),
                    partida.getVira().getValorCarta(),
                    80
            ));
        } else {
            labelVira.setIcon(null);
        }

        // Placar da partida
        String nome1 = partida.getJogador1().getNome();
        String nome2 = partida.getJogador2().getNome();
        int pontos1 = partida.getJogador1().getPontos();
        int pontos2 = partida.getJogador2().getPontos();

        if (partida.isEncerrada()) {
            setBackground(new Color(255, 245, 200));
            String vencedor = partida.getVencedorDaPartida() != null
                    ? partida.getVencedorDaPartida().getNome()
                    : "-";
            label.setText(String.format(
                    "Partida encerrada! Vencedor: %s — Placar final: %s: %d x %s: %d",
                    vencedor, nome1, pontos1, nome2, pontos2
            ));
        } else {
            setBackground(null);
            label.setText(String.format(
                    "Placar: %s: %d x %s: %d | Manilha: %s | Valor da mão: %d",
                    nome1, pontos1, nome2, pontos2,
                    partida.getManilha(),
                    partida.getValorMao()
            ));
        }

        revalidate();
        repaint();
    }

}
