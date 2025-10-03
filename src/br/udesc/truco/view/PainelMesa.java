package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.*;
import br.udesc.truco.util.CarregadorImagens;
import br.udesc.truco.util.Observador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelMesa extends JPanel implements Observador {
    private final Partida partida;
    private final JogoController controle;
    private final JPanel mao1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel mao2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JTextArea centro = new JTextArea(10, 60);
    private final int ALTURA_CARTA = 120;

    public PainelMesa(Partida p, JogoController c) {
        super(new BorderLayout());
        this.partida = p;
        this.controle = c;
        p.adicionarObservador(this);

        centro.setEditable(false);
        centro.setLineWrap(true);

        JPanel maos = new JPanel(new GridLayout(2, 1));
        maos.add(wrap("Jogador 1", mao1));
        maos.add(wrap("Jogador 2", mao2));

        add(maos, BorderLayout.WEST);
        add(new JScrollPane(centro), BorderLayout.CENTER);
        atualizar();
    }

    private JPanel wrap(String titulo, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(titulo), BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public void atualizar() {
        mao1.removeAll();
        mao2.removeAll();
        renderizarMao(partida.getJogador1(), mao1);
        renderizarMao(partida.getJogador2(), mao2);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partida.getRodadas().size(); i++) {
            sb.append("Rodada ").append(i + 1).append(": ");
            for (Rodada.Jogada jogada : partida.getRodadas().get(i).getJogadas()) {
                sb.append(jogada.jogador.getNome()).append(" -> ").append(jogada.carta).append(" | ");
            }
            if (i < partida.getVencedoresDasRodadas().size()) {
                var v = partida.getVencedoresDasRodadas().get(i);
                sb.append("  Vencedor: ").append(v == null ? "Empate" : v.getNome());
            }
            sb.append("\n");
        }
        centro.setText(sb.toString());
        revalidate();
        repaint();
    }

    private void renderizarMao(Jogador j, JPanel painel) {
        List<Carta> cartas = j.getMao().getCartas();
        for (int i = 0; i < cartas.size(); i++) {
            Carta c = cartas.get(i);
            JButton botao = new JButton();
            botao.setIcon(CarregadorImagens.iconeCarta(c.getNaipe(), c.getValorCarta(), ALTURA_CARTA));
            botao.setContentAreaFilled(false);
            botao.setBorderPainted(false);
            botao.setFocusPainted(false);
            final int idx = i;
            botao.addActionListener(e -> controle.jogarCarta(j, idx));
            painel.add(botao);
        }
    }
}