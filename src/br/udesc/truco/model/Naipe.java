package br.udesc.truco.model;

public enum Naipe {
    PAUS, COPAS, ESPADAS, OUROS;

    //ordenado por ordem de forca, onde ouros é o mais fraco e paus o mais forte
    public int ordemManilha() {
        switch (this) {
            case PAUS:
                return 4;
            case COPAS:
                return 3;
            case ESPADAS:
                return 2;
            case OUROS:
                return 1;
            default:
                return 0;
        }
    }
}
