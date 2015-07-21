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
    private ArrayList<ArrayList<SegmentoInvariancia>> tricordesEmLinhasP = new ArrayList<ArrayList<SegmentoInvariancia>>(),
                                                  tricordesEmLinhasI = new ArrayList<ArrayList<SegmentoInvariancia>>(),
                                                  tricordesEmLinhasR = new ArrayList<ArrayList<SegmentoInvariancia>>(),
                                                  tricordesEmLinhasRI = new ArrayList<ArrayList<SegmentoInvariancia>>();

    public MatrizDeAcordes(int chordSize, MatrizDodecafonica matriz) {
        for (int k = 0; k < 12; k++) {
            acrescentaLinha(matriz.getP(k), tricordesEmLinhasP, chordSize);
            acrescentaLinha(matriz.getI(k), tricordesEmLinhasI, chordSize);
            acrescentaLinha(matriz.getR(k), tricordesEmLinhasR, chordSize);
            acrescentaLinha(matriz.getRI(k), tricordesEmLinhasRI, chordSize);
        }
    }

    private void acrescentaLinha(ClasseDeAltura[] serie, ArrayList<ArrayList<SegmentoInvariancia>> matriz, int tamanhoAcorde) {
        ArrayList<SegmentoInvariancia> corrente = new ArrayList<SegmentoInvariancia>();
        SegmentoInvariancia passageiro;
        final int limite = 12 - tamanhoAcorde;

        for (int indice = 0; indice <= limite; indice++) {
            passageiro = new SegmentoInvariancia();

            for (int j = indice; j < indice + tamanhoAcorde; j++) {
                passageiro.adicionaNumero(serie[j]);
            }

            corrente.add(passageiro);
        }

        matriz.add(corrente);
    }

    public int getTamanhoTotal() {
        int tamanho = 0;
        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasP) {
            tamanho += linha.size();
        }

        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasI) {
            tamanho += linha.size();
        }

        return tamanho;
    }

    public SegmentoInvariancia getProximoElemento() {
        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasP) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        for (ArrayList<SegmentoInvariancia> linha : tricordesEmLinhasI) {
            if (linha.size() > 0) {
                return linha.get(0);
            }
        }

        return null;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizP() {
        return tricordesEmLinhasP;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizI() {
        return tricordesEmLinhasI;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizR() {
        return tricordesEmLinhasR;
    }

    public ArrayList<ArrayList<SegmentoInvariancia>> getMatrizRI() {
        return tricordesEmLinhasRI;
    }
}
