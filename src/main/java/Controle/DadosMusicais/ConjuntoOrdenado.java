/*
 * This file is part of Lewin, a compositional calculator.
 * Copyright (C) 2013 Hildegard Paulino Barbosa, hildegardpaulino@gmail.com
 * Copyright (C) 2013 Liduino José Pitombeira de Oliveira, http://www.pitombeira.com
 * Copyright (C) 2015 Raphael Sousa Santos, http://www.raphaelss.com
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

public class ConjuntoOrdenado {
    private final ClasseDeAltura[] classes;

    public ConjuntoOrdenado(ClasseDeAltura[] forma) {
        classes = new ClasseDeAltura[forma.length];
        for (int i = 0; i < forma.length; ++i) {
            classes[i] = forma[i];
        }
    }

    public ConjuntoOrdenado(ArrayList<ClasseDeAltura> forma) {
        classes = new ClasseDeAltura[forma.size()];
        forma.toArray(classes);
    }

    public ConjuntoOrdenado transpor(int n) {
        ClasseDeAltura[] classesTranspostas = new ClasseDeAltura[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            classesTranspostas[i] = classes[i].transpor(n);
        }
        return new ConjuntoOrdenado(classesTranspostas);
    }

    public ClasseDeAltura getDado(int indice) {
        return classes[indice];
    }

    public int tamanho() {
        return classes.length;
    }

    public boolean contem(ClasseDeAltura x) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == x) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(ConjuntoOrdenado forma) {
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
            resultado += classes[i].toString() + " ";
        }
        return resultado.substring(0, resultado.length() - 1) + "]";
    }

    public ConjuntoOrdenado inverter() {
        ClasseDeAltura[] classesInvertidas = new ClasseDeAltura[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            classesInvertidas[i] = classes[i].inverter();
        }
        return new ConjuntoOrdenado(classesInvertidas);
    }

    public int[] vetorIntervalar() {
        int[] contadores = new int[6];
        for (int i = 0; i < classes.length; ++i) {
            for (int j = i + 1; j < classes.length; ++j) {
                ++contadores[classes[i].intervalo_desord(classes[j]) - 1];
            }
        }
        return contadores;
    }
}
