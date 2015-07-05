/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Controle.DadosMusicais.MatrizDodecafonica;
import java.util.ArrayList;

/**
 *
 * @author Hildegard
 */
public class GeradorTabelaAdicao {
    public static MatrizDodecafonica geraTabela(ArrayList<Integer> numeros) {
        int i, j, tamanho = numeros.size();
        MatrizDodecafonica matriz = new MatrizDodecafonica();
        
        for (i = 0; i < tamanho; i++) {
            for (j = 0; j < tamanho; j++) {
                matriz.preenchePosicao(i, j, (numeros.get(i) + numeros.get(j)) % 12);
            }
            for (; j < 12; j++) {
                matriz.preenchePosicao(i, j, -1);
            }
        }
        
        for (; i < 12; i++) {
            for (j = 0; j < 12; j++) {
                matriz.preenchePosicao(i, j, -1);
            }
        }
        
        return matriz;
    }
}
