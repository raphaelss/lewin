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

import Controle.DadosMusicais.ConjuntoOrdenado;
import Controle.DadosMusicais.ClasseDeAltura;
import Controle.DadosMusicais.MatrizDodecafonica;
import Excecoes.DadosProibidos;
import java.awt.Point;
import java.util.*;

public class Controlador {
    private ConjuntoOrdenado numeros = new ConjuntoOrdenado(),
                             segundoConjunto = new ConjuntoOrdenado(),
                             resultadoSimples;
    private ArrayList<ConjuntoOrdenado> resultadoLista;
    private MatrizDodecafonica matriz;
    private ArrayList<String> invarianciaDerivativa;
    private ArrayList<ArrayList<ConjuntoOrdenado>> resultadosPaleta
        = new ArrayList<ArrayList<ConjuntoOrdenado>>(2);
    private int[] vetorIntervalar = new int[6];
    private double similaridade;

    public Controlador() {
        resultadosPaleta.add(new ArrayList<ConjuntoOrdenado>(12));
        resultadosPaleta.add(new ArrayList<ConjuntoOrdenado>(12));
    }

    public void limparDados() {
        numeros.clear();
        segundoConjunto.clear();
    }

    public ConjuntoOrdenado getConjuntoPrincipal() {
        return numeros;
    }

    public ConjuntoOrdenado getSegundoConjunto() {
        return segundoConjunto;
    }

    public MatrizDodecafonica getMatriz() {
        return matriz;
    }

    public void adicionaEntradaConjuntoPrincipal(ClasseDeAltura c) throws DadosProibidos {
        if (!numeros.add(c)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
    }

    public void substitui(ConjuntoOrdenado co) {
        numeros.clear();
        numeros.add(co);
    }

    public void removerConjuntoPrincipal() {
        numeros.remove(numeros.size() - 1);
    }

    public void adicionaEntradaSegundoConjunto(ClasseDeAltura c) throws DadosProibidos {
        if (!segundoConjunto.add(c)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
    }

    public boolean removerSegundoConjunto() {
        segundoConjunto.remove(segundoConjunto.size() - 1);
        return !segundoConjunto.isEmpty();
    }

    public void geraDerivacaoSerial() throws DadosProibidos {
        resultadoLista = GeradorDerivacaoSerial.gerar(numeros);
    }

    public ArrayList<ConjuntoOrdenado> getResultadoLista() {
        return resultadoLista;
    }

    public void geraMatrizDodecafonica() {
        matriz = GeradorMatrizDodecafonica.gerar(numeros);
    }

    public void geraFormaPrimaStraus() {
        resultadoSimples = FormasCompactas.formaPrimaStraus(numeros);
    }

    public void geraFormaPrimaForte() {
        resultadoSimples = FormasCompactas.formaPrimaForte(numeros);
    }

    public ConjuntoOrdenado getResultadoSimples() {
        return resultadoSimples;
    }

    public void geraFormaNormalStraus() {
        resultadoSimples = FormasCompactas.formaNormalStraus(numeros);
    }

    public void geraFormaNormalForte() {
        resultadoSimples = FormasCompactas.formaNormalForte(numeros);
    }

    public void geraRotacaoStravinskyana() {
        resultadoLista = RotacaoStravinsky.gerar(numeros);
    }

    public void geraPaleta() {
        resultadosPaleta.get(0).clear();
        resultadosPaleta.get(1).clear();
        GeradorPaleta.gerar(resultadosPaleta, numeros);
    }

    public ArrayList<ArrayList<ConjuntoOrdenado>> getPaleta() {
        return resultadosPaleta;
    }

    public void geraTabelaAdicao() {
        matriz = GeradorTabelaAdicao.geraTabela(numeros);
    }

    public void geraVetorIntervalar() {
        numeros.vetorIntervalar(vetorIntervalar);
    }

    public int[] getVetorIntervalar() {
        return vetorIntervalar;
    }

    public void geraSubconjuntos(int n) throws DadosProibidos {
        if (n < 1) {
            throw new DadosProibidos("Não é possível gerar subconjuntos com zero ou menos elementos.");
        } else if (n > numeros.size()) {
            throw new DadosProibidos("Não é possível gerar subconjuntos com um número maior de elementos que o conjunto de origem.");
        }
        resultadoLista = GeradorSubconjuntos.gerar(numeros, n);
    }

    public void geraSimilaridade() {
        similaridade = Similaridade.calcular(numeros, segundoConjunto);
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void geraInvarianciaTranspositiva(int quantidadeRepeticoes) throws DadosProibidos {
        if (quantidadeRepeticoes > numeros.size()) {
            throw new DadosProibidos("Não é possível retornar este resultado.");
        }
        else {
            invarianciaDerivativa = GeradorInvarianciaTranspositiva.gerar(numeros, quantidadeRepeticoes);
        }
    }

    public ArrayList<String> getInvarianciaDerivativa() {
        return invarianciaDerivativa;
    }

    public void geraInvarianciaInversiva(int quantidadeRepeticoes) throws DadosProibidos {
        int tamanhoEntrada = numeros.size();
        if (quantidadeRepeticoes > tamanhoEntrada) {
            throw new DadosProibidos("Não é possível retornar este resultado.");
        }
        else {
            invarianciaDerivativa = GeradorInvarianciaInversiva.gerar(numeros, tamanhoEntrada);
        }
    }

    public void geraMultiplicacaoBoulez() {
        resultadoSimples = Multiplicacao.boulez(numeros, segundoConjunto);
    }

    public void geraMultiplicacaoRahn1() {
        resultadoSimples = Multiplicacao.rahn(numeros, segundoConjunto.get(0).inteiro());
    }

    public void geraMultiplicacaoRahn2() {
        resultadoSimples = Multiplicacao.rahn(numeros, segundoConjunto);
    }

    public ArrayList<Point> geraCombinatoriedade() {
        return GeradorCombinatoriedade.gerar(matriz);
    }

    public ArrayList<String> getInvariancia(ClasseDeAltura.TipoRepresentacao formato) {
        return GeradorInvarianciaDodecafonica.gerar(matriz, formato);
    }
}
