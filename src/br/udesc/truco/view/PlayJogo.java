package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.*;

import javax.swing.*;

public class PlayJogo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jogador j1 = new Jogador("Jogador 1");
            Jogador j2 = new Jogador("Jogador 2");
            Partida partida = new Partida(j1, j2);
            JogoController jogoController = new JogoController(partida);
            JanelaPrincipal janela = new JanelaPrincipal(partida, jogoController);
            janela.setVisible(true);
            jogoController.iniciar();
        });
    }
}
