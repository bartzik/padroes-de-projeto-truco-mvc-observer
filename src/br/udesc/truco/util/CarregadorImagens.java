package br.udesc.truco.util;

import br.udesc.truco.model.Naipe;
import br.udesc.truco.model.ValorCarta;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CarregadorImagens {
    private static final Map<String, ImageIcon> cache = new HashMap<>();
    // Caminho relativo à raiz do projeto (fora de src): assets/imagens
    private static final String BASE = "assets/imagens";

    private static String nomeArquivo(Naipe n, ValorCarta v) {
        // mapeia Valor para número dos arquivos
        int numero;
        switch (v) {
            case AS:
                numero = 1;
                break;
            case DOIS:
                numero = 2;
                break;
            case TRES:
                numero = 3;
                break;
            case QUATRO:
                numero = 4;
                break;
            case CINCO:
                numero = 5;
                break;
            case SEIS:
                numero = 6;
                break;
            case SETE:
                numero = 7;
                break;
            case DEZ:
                numero = 10;
                break;
            case VALETE:
                numero = 11;
                break;
            case DAMA:
                numero = 12;
                break;
            default:
                numero = 0;
        }
        String naipe = n.name().toLowerCase(); // paus/copas/espadas/ouros
        return naipe + "_" + numero + ".png";
    }

    public static ImageIcon iconeCarta(Naipe n, ValorCarta v, int altura) {
        String key = n + "-" + v + "-" + altura;
        if (cache.containsKey(key)) return cache.get(key);
        String path = BASE + "/" + nomeArquivo(n, v);
        ImageIcon raw = new ImageIcon(path);
        if (raw.getIconWidth() <= 0) {
            // fallback (arquivo não encontrado) - gera um retângulo
            Image img = new java.awt.image.BufferedImage(70, altura, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(0, 0, 70, altura, 8, 8);
            g.setColor(Color.DARK_GRAY);
            g.drawString(v.toString() + " de " + n.toString(), 4, altura / 2);
            g.dispose();
            ImageIcon ic = new ImageIcon(img);
            cache.put(key, ic);
            return ic;
        }
        Image scaled = raw.getImage().getScaledInstance(-1, altura, Image.SCALE_SMOOTH);
        ImageIcon ic = new ImageIcon(scaled);
        cache.put(key, ic);
        return ic;
    }
}
