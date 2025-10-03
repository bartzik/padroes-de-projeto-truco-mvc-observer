package br.udesc.truco.view;

import br.udesc.truco.model.Partida;
import br.udesc.truco.util.Observador;

import javax.swing.*;
import java.awt.*;

public class PainelPlacar extends JPanel implements Observador {
    private final Partida partida;
    private final JLabel label;

    public PainelPlacar(Partida p) {
        this.partida = p;
        this.label = new JLabel();
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(label);
        p.adicionarObservador(this);
        atualizar();
    }

    @Override
    public void atualizar() {
        if (partida.isEncerrada()) {
            setBackground(new Color(255, 245, 200));
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            String vencedor = partida.getVencedorDaPartida() != null
                    ? partida.getVencedorDaPartida().getNome()
                    : "-";
            label.setText(String.format(
                    "🏆 Partida encerrada! Vencedor: %s — Placar final %d x %d",
                    vencedor,
                    partida.getJogador1().getPontos(),
                    partida.getJogador2().getPontos()
            ));
        } else {
            setBackground(null);
            setBorder(null);

            label.setText(String.format(
                    "Placar: %d x %d | Vira: %s | Manilha: %s | Valor da mão: %d",
                    partida.getJogador1().getPontos(),
                    partida.getJogador2().getPontos(),
                    partida.getVira(),
                    partida.getManilha(),
                    partida.getValorMao()
            ));
        }

        revalidate();
        repaint();
    }
}
