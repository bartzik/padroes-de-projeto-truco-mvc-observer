package br.udesc.truco.view;

import br.udesc.truco.controller.JogoController;
import br.udesc.truco.model.Jogador;
import br.udesc.truco.model.Partida;
import br.udesc.truco.controller.Observer;

import javax.swing.*;
import java.awt.*;

public class Acoes extends JPanel implements Observer {
    private final JogoController jogoController;
    private final Partida partida;
    private final JButton btnTruco = new JButton("Pedir Truco");
    private final JButton btnJogarNovamente = new JButton("Jogar novamente");

    private boolean janelaTrucoAberta = false;

    public Acoes(JogoController jogoController, Partida partida) {
        this.jogoController = jogoController;
        this.partida = partida;

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(btnTruco);
        add(btnJogarNovamente);

        btnTruco.addActionListener(e -> solicitarTruco());
        btnJogarNovamente.addActionListener(e -> jogoController.reiniciarPartida());
        btnJogarNovamente.setVisible(false);

        partida.anexar(this);
        atualizar();
    }

    private void solicitarTruco() {
        Jogador solicitante = jogoController.getJogadorAtual();
        jogoController.trucar(solicitante);
        mostrarJanelaTruco(); 
    }

    private void mostrarJanelaTruco() {
        if (!partida.isPedidoTrucoPendente() || janelaTrucoAberta) return;

        janelaTrucoAberta = true;

        SwingUtilities.invokeLater(() -> {
            Jogador respondente = partida.getJogadorRespondeAposta();
            if (respondente == null) {
                janelaTrucoAberta = false;
                return;
            }

            int valorAtual = partida.getValorMao();
            int proximoValor = switch (valorAtual) {
                case 3 -> 6;
                case 6 -> 9;
                case 9 -> 12;
                default -> valorAtual;
            };

            Object[] opcoes = (valorAtual < 12)
                    ? new Object[]{"Aceitar", "Correr", "Subir para " + proximoValor}
                    : new Object[]{"Aceitar", "Correr"};
            
            String mensagem = switch (valorAtual) {
            case 3 -> "Você recebeu um pedido de Truco!";
            case 6 -> "Você recebeu um pedido de Seis!";
            case 9 -> "Você recebeu um pedido de Nove!";
            case 12 -> "Você recebeu um pedido de Doze!";
            default -> "Você recebeu um pedido de Truco!";
        };

        JOptionPane optionPane = new JOptionPane(
                mensagem,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                opcoes,
                opcoes[0]
        );
        
        JDialog dialog = optionPane.createDialog(this, "Aposta");
        dialog.setLocation(500, 300); 
        dialog.setVisible(true);

        
        Object selectedValue = optionPane.getValue();
        int escolha = -1;
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].equals(selectedValue)) {
                escolha = i;
                break;
            }
        }

            janelaTrucoAberta = false;

            switch (escolha) {
                case 0 -> {
                	jogoController.aceitarTruco(respondente);// Aceitar
                	btnTruco.setEnabled(false);
                }
                case 1 -> {
                	jogoController.correrTruco(respondente);   // Correr
                }
                case 2 -> {                                           // Subir
                    jogoController.subirTruco(respondente);
                    SwingUtilities.invokeLater(this::mostrarJanelaTruco);
                }
            }
        });
    }

    @Override
    public void atualizar() {
        boolean partidaEncerrada = partida.isEncerrada();
        btnTruco.setEnabled(!partidaEncerrada && !partida.isPedidoTrucoPendente());
        btnJogarNovamente.setVisible(partidaEncerrada);
    }

}

