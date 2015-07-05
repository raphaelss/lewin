/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controle.DadosMusicais;

/**
 *
 * @author Bill's
 */
public class MatrizDodecafonica {
    private int[][] matriz;

    public MatrizDodecafonica() {
        matriz = new int[12][12];
    }

    public void preenchePosicao(int linha, int coluna, int valor) {
        matriz[linha][coluna] = valor;
    }

    public void setLinha(int linha, int[] serie) {
        matriz[linha] = serie;
    }

    public void setColuna(int coluna, int[] serie) {
        for (int i = 0; i < 12; i++) {
            matriz[i][coluna] = serie[i];
        }
    }

    public int getValor(int linha, int coluna) {
        return matriz[linha][coluna];
    }

    public int[] getP(int indice) {
        return matriz[indice];
    }

    public int[] getR(int indice) {
        int[] serie = new int[12];

        for (int i = 11; i >= 0; i--) {
            serie[11 - i] = matriz[indice][i];
        }

        return serie;
    }

    public int[] getI(int indice) {
        int[] serie = new int[12];

        for (int i = 0; i < 12; i++) {
            serie[i] = matriz[i][indice];
        }
        
        return serie;
    }

    public int[] getRI(int indice) {
        int[] serie = new int[12];

        for (int i = 11; i >= 0; i--) {
            serie[11 - i] = matriz[i][indice];
        }

        return serie;
    }
}