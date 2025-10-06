package br.udesc.truco.util;

import br.udesc.truco.model.Naipe;
import br.udesc.truco.model.ValorCarta;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.io.File;

public class CarregadorImagens {
    private static final Map<String, ImageIcon> cache = new HashMap<>();
 
    private static final String RESOURCE_BASE = "/assets/imagens";
    
    private static final String FS_BASE = "assets/imagens";

    private static String nomeArquivo(Naipe n, ValorCarta v) {
        int numero;
        switch (v) {
            case AS: numero = 1; break;
            case DOIS: numero = 2; break;
            case TRES: numero = 3; break;
            case QUATRO: numero = 4; break;
            case CINCO: numero = 5; break;
            case SEIS: numero = 6; break;
            case SETE: numero = 7; break;
            case DEZ: numero = 10; break;
            case VALETE: numero = 11; break;
            case DAMA: numero = 12; break;
            default: numero = 0;
        }
        String naipe = n.name().toLowerCase(); // paus/copas/espadas/ouros
        return naipe + "_" + numero + ".png";
    }

    public static ImageIcon iconeCarta(Naipe n, ValorCarta v, int altura) {
        String key = n + "-" + v + "-" + altura;
        if (cache.containsKey(key)) return cache.get(key);

        String filename = nomeArquivo(n, v);
        
        String resourcePath = RESOURCE_BASE + "/" + filename;
        URL url = CarregadorImagens.class.getResource(resourcePath);
        ImageIcon raw = null;

        if (url != null) {
            raw = new ImageIcon(url);
        } else {
           
            String fsPath = FS_BASE + File.separator + filename;
            File f = new File(fsPath);
            if (f.exists()) {
                raw = new ImageIcon(fsPath);
            }
        }

        if (raw == null || raw.getIconWidth() <= 0) {
            
            Image img = new java.awt.image.BufferedImage(70, altura, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(0, 0, 70, altura, 8, 8);
            g.setColor(Color.DARK_GRAY);
            String texto = v.toString() + " de " + n.toString();
            
            int y = altura / 2;
            g.drawString(texto, 4, y);
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

