/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.util.ArrayList;

/**
 *
 * @author Bill's
 */
public class GeradorVetorIntervalar {
    public static int[] geraVetor(ArrayList<Integer> entrada) {
        ArrayList<Integer> diferencas = new ArrayList<Integer>();
        
        for (int i = 0; i < entrada.size(); i++) {
            for (int j = i + 1; j < entrada.size(); j++) {
                diferencas.add(Math.abs(entrada.get(i) - entrada.get(j)));
            }
        }
        int[] contadores = new int[6];
        
        for (int numero : diferencas) {
            switch(numero) {
                case 1:
                case 11:
                    contadores[0]++;
                    break;
                case 2:
                case 10:
                    contadores[1]++;
                    break;
                case 3:
                case 9:
                    contadores[2]++;
                    break;
                case 4:
                case 8:
                    contadores[3]++;
                    break;
                case 5:
                case 7:
                    contadores[4]++;
                    break;
                case 6:
                    contadores[5]++;
            }
        }
        
        return contadores;
    }
}
