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
import javax.swing.JOptionPane;

public class Controlador {
    private ConjuntoOrdenado numeros = new ConjuntoOrdenado(),
                             segundoConjunto = new ConjuntoOrdenado(),
                             formaCompacta, resultadoMultiplicacao;
    private ArrayList<ConjuntoOrdenado> listaDeFormas;
    private MatrizDodecafonica matriz;
    private ArrayList<String> invarianciaDerivativa;
    private ArrayList<ConjuntoOrdenado> resultadoRotacaoStravinskyana;
    private ArrayList<ArrayList<ConjuntoOrdenado>> resultadosPaleta
        = new ArrayList<ArrayList<ConjuntoOrdenado>>(2);
    private int[] vetorIntervalar = new int[6];

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

    public void substitui_FormaCompacta() {
        numeros = formaCompacta;
    }

    public void substituir_Multiplicacao() {
        numeros = resultadoMultiplicacao;
    }

    public void substitui_DerivacaoSerial() {
        int indice = 0;
        while((indice =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Selecione o \u00edndice da s\u00e9rie a ser usada")))
                >= listaDeFormas.size());

        numeros.clear();
        numeros.add(listaDeFormas.get(indice));
    }

    public void substitui_Paleta() {
        int indice = 0;
        while((indice =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Selecione o \u00edndice")))
                >= resultadoRotacaoStravinskyana.size());

        numeros.clear();
        numeros.add(resultadoRotacaoStravinskyana.get(indice));
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
        listaDeFormas = GeradorDerivacaoSerial.gerar(numeros);
    }

    public ArrayList<ConjuntoOrdenado> getDerivacaoSerial() {
        return listaDeFormas;
    }

    public void geraMatrizDodecafonica() {
        matriz = GeradorMatrizDodecafonica.gerar(numeros);
    }

    public void geraFormaPrimaStraus() {
        formaCompacta = FormasCompactas.formaPrimaStraus(numeros);
    }

    public void geraFormaPrimaForte() {
        formaCompacta = FormasCompactas.formaPrimaForte(numeros);
    }

    public ConjuntoOrdenado getFormaCompacta() {
        return formaCompacta;
    }

    public void geraFormaNormalStraus() {
        formaCompacta = FormasCompactas.formaNormalStraus(numeros);
    }

    public void geraFormaNormalForte() {
        formaCompacta = FormasCompactas.formaNormalForte(numeros);
    }

    public ArrayList<ConjuntoOrdenado> getRotacaoStravinskyana() {
        resultadoRotacaoStravinskyana = RotacaoStravinsky.gerar(numeros);
        return resultadoRotacaoStravinskyana;
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

    public int[] getVetorIntervalar() {
        numeros.vetorIntervalar(vetorIntervalar);
        return vetorIntervalar;
    }

    public ArrayList<ConjuntoOrdenado> getSubconjuntos() {
        int tamanhoSubconjuntos = 0;
        while((tamanhoSubconjuntos =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe o tamanho dos subconjuntos")))
                >= numeros.size());
        return GeradorSubconjuntos.gerar(numeros, tamanhoSubconjuntos);
    }

    public double getSimilaridade() {
        return Similaridade.calcular(numeros, segundoConjunto);
    }

    public void geraInvarianciaTranspositiva() {
        int quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));
        if (quantidadeRepeticoes > numeros.size()) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            invarianciaDerivativa = GeradorInvarianciaTranspositiva.gerar(numeros, quantidadeRepeticoes);
        }
    }

    public ArrayList<String> getInvarianciaDerivativa() {
        return invarianciaDerivativa;
    }

    public void geraInvarianciaInversiva() {
        int tamanhoEntrada = numeros.size(),
            quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));

        if (quantidadeRepeticoes > tamanhoEntrada) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            invarianciaDerivativa = GeradorInvarianciaInversiva.gerar(numeros, tamanhoEntrada);
        }
    }

    public void geraMultiplicacaoBoulez() {
        resultadoMultiplicacao = Multiplicacao.boulez(numeros, segundoConjunto);
    }

    public void geraMultiplicacaoRahn1() {
        resultadoMultiplicacao = Multiplicacao.rahn(numeros, segundoConjunto.get(0).inteiro());
    }

    public void geraMultiplicacaoRahn2() {
        resultadoMultiplicacao = Multiplicacao.rahn(numeros, segundoConjunto);
    }

    public ConjuntoOrdenado getMultiplicacao() {
        return resultadoMultiplicacao;
    }

    public ArrayList<Point> geraCombinatoriedade() {
        return GeradorCombinatoriedade.gerar(matriz);
    }

    public ArrayList<String> getInvariancia(ClasseDeAltura.TipoRepresentacao formato) {
        return GeradorInvarianciaDodecafonica.gerar(matriz, formato);
    }
}
