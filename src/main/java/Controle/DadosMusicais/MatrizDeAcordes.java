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

public class MatrizDeAcordes {
    private ArrayList<ArrayList<ConjuntoOrdenado>> tricordesEmLinhasP = new ArrayList<ArrayList<ConjuntoOrdenado>>(),
                                                  tricordesEmLinhasI = new ArrayList<ArrayList<ConjuntoOrdenado>>(),
                                                  tricordesEmLinhasR = new ArrayList<ArrayList<ConjuntoOrdenado>>(),
                                                  tricordesEmLinhasRI = new ArrayList<ArrayList<ConjuntoOrdenado>>();

    public MatrizDeAcordes(int chordSize, MatrizDodecafonica matriz) {
        for (int k = 0; k < 12; k++) {
            acrescentaLinha(matriz.getP(k), tricordesEmLinhasP, chordSize);
            acrescentaLinha(matriz.getI(k), tricordesEmLinhasI, chordSize);
            acrescentaLinha(matriz.getR(k), tricordesEmLinhasR, chordSize);
            acrescentaLinha(matriz.getRI(k), tricordesEmLinhasRI, chordSize);
        }
    }

    private void acrescentaLinha(ClasseDeAltura[] serie, ArrayList<ArrayList<ConjuntoOrdenado>> matriz, int tamanhoAcorde) {
        ArrayList<ConjuntoOrdenado> corrente = new ArrayList<ConjuntoOrdenado>();
        ConjuntoOrdenado passageiro;
        final int limite = 12 - tamanhoAcorde;

        for (int indice = 0; indice <= limite; indice++) {
            passageiro = new ConjuntoOrdenado();

            for (int j = indice; j < indice + tamanhoAcorde; j++) {
                passageiro.add(serie[j]);
            }

            corrente.add(passageiro);
        }

        matriz.add(corrente);
    }

    public int getTamanhoTotal() {
        int tamanho = 0;
        for (ArrayList<ConjuntoOrdenado> linha : tricordesEmLinhasP) {
            tamanho += linha.size();
        }

        for (ArrayList<ConjuntoOrdenado> linha : tricordesEmLinhasI) {
            tamanho += linha.size();
        }

        return tamanho;
    }

    public ConjuntoOrdenado getProximoElemento() {
        for (ArrayList<ConjuntoOrdenado> linha : tricordesEmLinhasP) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        for (ArrayList<ConjuntoOrdenado> linha : tricordesEmLinhasI) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        return null;
    }

    public ArrayList<ArrayList<ConjuntoOrdenado>> getMatrizP() {
        return tricordesEmLinhasP;
    }

    public ArrayList<ArrayList<ConjuntoOrdenado>> getMatrizI() {
        return tricordesEmLinhasI;
    }

    public ArrayList<ArrayList<ConjuntoOrdenado>> getMatrizR() {
        return tricordesEmLinhasR;
    }

    public ArrayList<ArrayList<ConjuntoOrdenado>> getMatrizRI() {
        return tricordesEmLinhasRI;
    }
}
