package br.udesc.truco.model;

public class Carta {
    private final Naipe naipe;
    private final ValorCarta valorCarta;
    private boolean encoberta = false;

    public Carta(Naipe n, ValorCarta v) {
        this.naipe = n;
        this.valorCarta = v;
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public ValorCarta getValorCarta() {
        return valorCarta;
    }

    public boolean isEncoberta() {
        return encoberta;
    }

    public void setEncoberta(boolean e) {
        this.encoberta = e;
    }

    @Override
    public String toString() {
        return encoberta ? "[Encoberta]" : (valorCarta + " de " + naipe);
    }

    public int valorParaComparacao(ValorCarta manilha) {
        if (this.valorCarta == manilha) {
            return 100 + naipe.ordinal(); 
        }

        return valorCarta.ordinal();
    }

}
