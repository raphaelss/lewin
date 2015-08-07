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
import Controle.DadosMusicais.ConjuntoOrdenado;

public abstract class FormasCompactas {
    private interface IndiceComparador {
        public int indiceComparacao(int i, int ultimoAComparar);
    }

    private static ConjuntoOrdenado maisCompacto(ConjuntoOrdenado a, ConjuntoOrdenado b, IndiceComparador ic) {
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

    private static ConjuntoOrdenado algoritmoBase(ConjuntoOrdenado classes, IndiceComparador ic) {
        ConjuntoOrdenado rot = new ConjuntoOrdenado(classes);
        rot.sort();
        ConjuntoOrdenado melhor = new ConjuntoOrdenado(rot);
        rot.rotacionar();
        int tamanho = rot.size();
        for (int i = 1; i < tamanho; ++i) {
            ConjuntoOrdenado tmp = maisCompacto(melhor, rot, ic);
            if (tmp != melhor) {
                melhor = new ConjuntoOrdenado(tmp);
            }
            rot.rotacionar();
        }
        return melhor;
    }

    public static ConjuntoOrdenado formaNormalStraus(ConjuntoOrdenado classes) {
        return algoritmoBase(classes, comparadorStraus);
    }

    public static ConjuntoOrdenado formaNormalForte(ConjuntoOrdenado classes) {
        return algoritmoBase(classes, comparadorForte);
    }

    public static ConjuntoOrdenado formaPrimaStraus(ConjuntoOrdenado classes) {
        ConjuntoOrdenado tmp = new ConjuntoOrdenado(classes);
        tmp.inverter();
        tmp = maisCompacto(formaNormalStraus(classes),
                           formaNormalStraus(tmp),
                           comparadorStraus);
        tmp.transporParaZero();
        return tmp;
    }

    public static ConjuntoOrdenado formaPrimaForte(ConjuntoOrdenado classes) {
        ConjuntoOrdenado tmp = new ConjuntoOrdenado(classes);
        tmp.inverter();
        tmp = maisCompacto(formaNormalForte(classes),
                           formaNormalForte(tmp),
                           comparadorForte);
        tmp.transporParaZero();
        return tmp;
    }
}
