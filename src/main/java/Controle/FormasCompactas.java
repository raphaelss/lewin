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

package Controle;

import Controle.DadosMusicais.ClasseDeAltura;

import java.util.ArrayList;
import java.util.Collections;

public abstract class FormasCompactas {
    //Altera o dado de entrada
    private static ArrayList<ClasseDeAltura> transpoeParaZero(ArrayList<ClasseDeAltura> classes) {
        int fatorTransposicao = -classes.get(0).inteiro();
        for (int i = 0; i < classes.size(); i++) {
            classes.set(i, classes.get(i).transpor(fatorTransposicao));
        }
        return classes;
    }

    //Altera o dado de entrada
    private static void rotacionar(ArrayList<ClasseDeAltura> classes) {
        classes.add(classes.remove(0));
    }

    private static ArrayList<ClasseDeAltura> inverter(ArrayList<ClasseDeAltura> classes) {
        ArrayList<ClasseDeAltura> inverso = new ArrayList<ClasseDeAltura>(classes);
        for (int i = 0; i < inverso.size(); ++i) {
            inverso.set(i, inverso.get(i).inverter());
        }
        return inverso;
    }

    private interface IndiceComparador {
        public int indiceComparacao(int i, int ultimoAComparar);
    }

    private static ArrayList<ClasseDeAltura> maisCompacto(ArrayList<ClasseDeAltura> a, ArrayList<ClasseDeAltura> b, IndiceComparador ic) {
        ClasseDeAltura primeiroA = a.get(0);
        ClasseDeAltura primeiroB = b.get(0);
        int ultimo = a.size() - 1;
        int intervaloA = primeiroA.intervalo_ord(a.get(ultimo));
        int intervaloB = primeiroB.intervalo_ord(b.get(ultimo));
        if (intervaloA < intervaloB) {
            return a;
        } else if (intervaloA > intervaloB) {
            return b;
        }
        --ultimo;
        for (int i = 0; i < ultimo; ++i) {
            int indice = ic.indiceComparacao(i, ultimo);
            intervaloA = primeiroA.intervalo_ord(a.get(indice));
            intervaloB = primeiroB.intervalo_ord(b.get(indice));
            if (intervaloA < intervaloB) {
                return a;
            } else if (intervaloA > intervaloB) {
                return b;
            }
        }
        return primeiroA.compareTo(primeiroB) < 0? a : b;
    }


    private final static IndiceComparador comparadorStraus = new IndiceComparador() {
        public int indiceComparacao(int i, int ultimoAComparar) {
            return ultimoAComparar - i;
        }
    };

    private final static IndiceComparador comparadorForte = new IndiceComparador() {
        public int indiceComparacao(int i, int ultimoAComparar) {
            return i + 1;
        }
    };

    private static ArrayList<ClasseDeAltura> algoritmoBase(ArrayList<ClasseDeAltura> classes, IndiceComparador ic) {
        ArrayList<ClasseDeAltura> rot = new ArrayList<ClasseDeAltura>(classes);
        Collections.sort(rot);
        ArrayList<ClasseDeAltura> melhor = new ArrayList<ClasseDeAltura>(rot);
        rotacionar(rot);
        int tamanho = rot.size();
        for (int i = 1; i < rot.size(); ++i) {
            ArrayList<ClasseDeAltura> tmp = maisCompacto(melhor, rot, ic);
            if (tmp != melhor) {
                for (int j = 0; j < tamanho; ++j) {
                    melhor.set(j, tmp.get(j));
                }
            }
            rotacionar(rot);
        }
        return melhor;
    }

    public static ArrayList<ClasseDeAltura> formaNormalStraus(ArrayList<ClasseDeAltura> classes) {
        return algoritmoBase(classes, comparadorStraus);
    }

    public static ArrayList<ClasseDeAltura> formaNormalForte(ArrayList<ClasseDeAltura> classes) {
        return algoritmoBase(classes, comparadorForte);
    }

    public static ArrayList<ClasseDeAltura> formaPrimaStraus(ArrayList<ClasseDeAltura> classes) {
        return transpoeParaZero(maisCompacto(formaNormalStraus(classes),
                                             formaNormalStraus(inverter(classes)),
                                             comparadorStraus));
    }

    public static ArrayList<ClasseDeAltura> formaPrimaForte(ArrayList<ClasseDeAltura> classes) {
        return transpoeParaZero(maisCompacto(formaNormalStraus(classes),
                                             formaNormalStraus(inverter(classes)),
                                             comparadorForte));
    }
}
