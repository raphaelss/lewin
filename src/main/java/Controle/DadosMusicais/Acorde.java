/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
