package br.udesc.truco.model;

public enum ValorCarta {
    //ordenado da carta mais fraca para a carta mais forte
    QUATRO(0), CINCO(1), SEIS(2), SETE(3), DEZ(4), VALETE(5), DAMA(6), AS(7), DOIS(8), TRES(9);

    private final int valorDaCarta;

    //construtor
    ValorCarta(int valor) {
        this.valorDaCarta = valor;
    }

    public int getValorDaCarta() {
        return valorDaCarta;
    }

    public static ValorCarta defineManilha(ValorCarta v) {
        switch (v) {
            case QUATRO:
                return CINCO;
            case CINCO:
                return SEIS;
            case SEIS:
                return SETE;
            case SETE:
                return DEZ;
            case DEZ:
                return VALETE;
            case VALETE:
                return DAMA;
            case DAMA:
                return AS;
            case AS:
                return DOIS;
            case DOIS:
                return TRES;
            case TRES:
                return QUATRO;
            default:
                return QUATRO;
        }
    }
}
