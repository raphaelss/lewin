/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controle.DadosMusicais;

import java.util.ArrayList;

/**
 *
 * @author hildegard
 */
public class Acorde {
    private int[] classes;

    public Acorde(ArrayList<Integer> forma) {
        this(forma.toArray(new Integer[forma.size()]));
    }

    private Acorde(Integer[] forma) {
        classes = new int[forma.length];
        
        for (int i = 0; i < forma.length; i++) {
            classes[i] = forma[i];
        }
    }

    public Acorde(int[] forma) {
        classes = forma;
    }

    public int getDado(int indice) {
        return classes[indice];
    }

    public int tamanho() {
        return classes.length;
    }

    public boolean contem(int nota) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == nota) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Acorde forma) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] != forma.classes[i]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String resultado = "[";

        for (int i = 0; i < classes.length; i++) {
            resultado += classes[i] + " ";
        }
        return resultado.substring(0, resultado.length() - 1) + "]";
    }

    public Acorde espelhada() {
        Acorde espelhada;

        if (classes.length == 3) {
            espelhada = new Acorde(new int[] {(12 - classes[0]) % 12, (12 - classes[1]) % 12, (12 - classes[2]) % 12});
        }
        else if (classes.length == 4) {
            espelhada = new Acorde(new int[] {(12 - classes[0]) % 12, (12 - classes[1]) % 12, (12 - classes[2]) % 12, (12 - classes[3]) % 12});
        }
        else {
            espelhada = new Acorde(new int[] {(12 - classes[0]) % 12, (12 - classes[1]) % 12, (12 - classes[2]) % 12, (12 - classes[3]) % 12,
                    (12 - classes[4]) % 12, (12 - classes[5]) % 12});
        }
        
        return espelhada;
    }
}