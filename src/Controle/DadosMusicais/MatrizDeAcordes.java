/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controle.DadosMusicais;

import java.util.ArrayList;

/**
 *
 * @author Bill's
 */
public class MatrizDeAcordes {
    private ArrayList<ArrayList<SegmentoInvariancia>> tricordesEmLinhasP = new ArrayList<ArrayList<SegmentoInvariancia>>(),
                                                  tricordesEmLinhasI = new ArrayList<ArrayList<SegmentoInvariancia>>(), 
                                                  tricordesEmLinhasR = new ArrayList<ArrayList<SegmentoInvariancia>>(),
                                                  tricordesEmLinhasRI = new ArrayList<ArrayList<SegmentoInvariancia>>();

    public MatrizDeAcordes(int chordSize, MatrizDodecafonica matriz) {
        for (int k = 0; k < 12; k++) {
            acrescentaLinha(matriz.getP(k), tricordesEmLinhasP, chordSize);
            acrescentaLinha(matriz.getI(k), tricordesEmLinhasI, chordSize);
            acrescentaLinha(matriz.getR(k), tricordesEmLinhasR, chordSize);
            acrescentaLinha(matriz.getRI(k), tricordesEmLinhasRI, chordSize);
        }
    }

    private void acrescentaLinha(int[] serie, ArrayList<ArrayList<SegmentoInvariancia>> matriz, int tamanhoAcorde) {
        ArrayList<SegmentoInvariancia> corrente = new ArrayList<SegmentoInvariancia>();
        SegmentoInvariancia passageiro;
        final int limite = 12 - tamanhoAcorde;

        for (int indice = 0; indice <= limite; indice++) {
            passageiro = new SegmentoInvariancia();

            for (int j = indice; j < indice + tamanhoAcorde; j++) {
                passageiro.adicionaNumero(serie[j]);
            }

            corrente.add(passageiro);
        }

        matriz.add(corrente);
    }

    public int getTamanhoTotal() {
        int tamanho = 0;
        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasP) {
            tamanho += linha.size();
        }

        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasI) {
            tamanho += linha.size();
        }

        return tamanho;
    }

    public SegmentoInvariancia getProximoElemento() {
        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasP) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasI) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        return null;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizP() {
        return tricordesEmLinhasP;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizI() {
        return tricordesEmLinhasI;
    }
    
    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizR() {
        return tricordesEmLinhasR;
    }
    
    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizRI() {
        return tricordesEmLinhasRI;
    }
}