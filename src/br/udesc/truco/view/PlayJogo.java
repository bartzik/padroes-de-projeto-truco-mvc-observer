package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.Jogador;
import br.udesc.truco.model.Partida;

import javax.swing.*;

public class PlayJogo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlayJogo::iniciarJogo);
    }

    private static void iniciarJogo() {
        Jogador jogador1 = new Jogador("Jogador 1");
        Jogador jogador2 = new Jogador("Jogador 2"); 
        Partida partida = new Partida(jogador1, jogador2); 
        JogoController controller = new JogoController(partida);
        JanelaPrincipal janela = new JanelaPrincipal(partida, controller);
        janela.setVisible(true);
        controller.iniciar();
       
    }
}
