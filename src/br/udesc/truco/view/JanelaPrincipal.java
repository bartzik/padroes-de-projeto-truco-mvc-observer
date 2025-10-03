package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.Partida;
import br.udesc.truco.util.Observador;

import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame implements Observador {
    private final Partida partida;
    private final JogoController jogoController;
    private final PainelMesa painelMesa;
    private final PainelPlacar painelPlacar;
    private final Acoes acoes;

    public JanelaPrincipal(Partida partida, JogoController jogoController) {
        super("Truco");
        this.partida = partida;
        this.jogoController = jogoController;
        painelMesa = new PainelMesa(partida, jogoController);
        painelPlacar = new PainelPlacar(partida);
        this.acoes = new Acoes(jogoController, this.partida);

        partida.adicionarObservador(this);

        setLayout(new BorderLayout());
        add(painelPlacar, BorderLayout.NORTH);
        add(painelMesa, BorderLayout.CENTER);
        add(this.acoes, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 660);
        setLocationRelativeTo(null);
    }

    @Override
    public void atualizar() {
        painelMesa.atualizar();
        painelPlacar.atualizar();
        revalidate();
        repaint();
    }
}
