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

public class Acorde {
    private final ClasseDeAltura[] classes;

    public Acorde(ClasseDeAltura[] forma) {
        classes = new ClasseDeAltura[forma.length];
        for (int i = 0; i < forma.length; ++i) {
            classes[i] = forma[i];
        }
    }

    public Acorde(ArrayList<ClasseDeAltura> forma) {
        classes = new ClasseDeAltura[forma.size()];
        forma.toArray(classes);
    }

    public Acorde transpor(int n) {
        ClasseDeAltura[] classesTranspostas = new ClasseDeAltura[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            classesTranspostas[i] = classes[i].transpor(n);
        }
        return new Acorde(classesTranspostas);
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

    public boolean contem(int nota) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == ClasseDeAltura.criar(nota)) {
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
            resultado += classes[i].inteiro() + " ";
        }
        return resultado.substring(0, resultado.length() - 1) + "]";
    }

    public Acorde espelhada() {
        ClasseDeAltura[] classesInvertidas = new ClasseDeAltura[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            classesInvertidas[i] = classes[i].inverter();
        }
        return new Acorde(classesInvertidas);
    }
}
