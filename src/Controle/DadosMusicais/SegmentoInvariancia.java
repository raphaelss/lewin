package Controle.DadosMusicais;


import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bill's
 */
public class SegmentoInvariancia {
    private ArrayList<Integer> segmento = new ArrayList<Integer>();

    public void adicionaNumero(int numero) {
        segmento.add(numero);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SegmentoInvariancia) && 
                segmento.equals(((SegmentoInvariancia)o).segmento);
    }

    public String toString() {
        String resultado = "";
        for (int i : segmento) {
            resultado += i + " ";
        }

        return resultado;
    }
    
    public int tamanhoSegmento() {
        return segmento.size();
    }
    
    public int get(int indice) {
        return segmento.get(indice);
    }
}