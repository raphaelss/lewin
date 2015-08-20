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
import Controle.DadosMusicais.MatrizDeAcordes;
import Excecoes.DadosProibidos;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;

public class Controlador {
    private ConjuntoOrdenado numeros = new ConjuntoOrdenado(),
                             segundoConjunto = new ConjuntoOrdenado(),
                             formaCompacta, resultadoMultiplicacao;
    private ArrayList<ConjuntoOrdenado> listaDeFormas;
    private ConjuntoOrdenado serieEscolhida = new ConjuntoOrdenado();
    private MatrizDodecafonica matriz;
    private ArrayList<ConjuntoOrdenado> acordesALimpo;
    private ArrayList<String> invarianciaDerivativa, invariancias;
    private ArrayList<ConjuntoOrdenado> resultadoRotacaoStravinskyana;
    private ArrayList<ArrayList<ConjuntoOrdenado>> resultadosPaleta
        = new ArrayList<ArrayList<ConjuntoOrdenado>>(2);
    private ArrayList<ConjuntoOrdenado> subconjuntos;
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
        matriz = new MatrizDodecafonica();

        serieEscolhida.clear();
        if (numeros.size() < 12) {
            int indice = 0;
            while((indice =
                    Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Selecione o \u00edndice da s\u00e9rie de entrada da matriz")))
                    >= listaDeFormas.size());
            serieEscolhida.add(listaDeFormas.get(indice));
        }
        else {
            serieEscolhida.add(numeros);
        }

        ConjuntoOrdenado inverso = new ConjuntoOrdenado(serieEscolhida).inverter();
        for (int i = 11; i >= 0; --i) {
            matriz.setLinha(i, serieEscolhida.transporPara(inverso.get(i)));
        }
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

    public void geraRotacaoStravinskyana() {
        resultadoRotacaoStravinskyana = RotacaoStravinsky.gerar(numeros);
    }

    public ArrayList<ConjuntoOrdenado> getRotacaoStravinskyana() {
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

    public void gerarVetorIntervalar() {
        numeros.vetorIntervalar(vetorIntervalar);
    }

    public int[] getVetorIntervalar() {
        return vetorIntervalar;
    }

    public void geraSubconjuntos() {
        HashSet<ConjuntoOrdenado> tmp = new HashSet<ConjuntoOrdenado>();

        int tamanhoSubconjuntos = 0;
        while((tamanhoSubconjuntos =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe o tamanho dos subconjuntos")))
                >= numeros.size());

        constroi(0, tamanhoSubconjuntos, new ConjuntoOrdenado(), tmp);
        subconjuntos = new ArrayList<ConjuntoOrdenado>(tmp);
    }

    public ArrayList<ConjuntoOrdenado> getSubconjuntos() {
        return subconjuntos;
    }

    private void constroi(int indice, int tamanhoSubconjuntos, ConjuntoOrdenado subconjunto, HashSet<ConjuntoOrdenado> subconjuntos) {
        for (; indice < numeros.size(); indice++) {
            subconjunto.add(numeros.get(indice));
            if (subconjunto.size() == tamanhoSubconjuntos) {
                subconjuntos.add(new ConjuntoOrdenado(subconjunto));
                subconjunto.remove(subconjunto.size() - 1);
            }
            else {
                constroi(indice + 1, tamanhoSubconjuntos, subconjunto, subconjuntos);
            }
        }

        try {
            subconjunto.remove(subconjunto.size() - 1);
        }
        catch(NoSuchElementException nsee) {}
    }

    public double getSimilaridade() {
        return Similaridade.calcular(numeros, segundoConjunto);
    }

    public void geraInvarianciaTranspositiva() {
        invarianciaDerivativa = new ArrayList<String>();

        int tamanhoEntrada = numeros.size(),
            quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));

        if (quantidadeRepeticoes > tamanhoEntrada) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            int [] vetInt = numeros.vetorIntervalar();
            for (int i = 0; i < 5; i++) {
                if (vetInt[i] == quantidadeRepeticoes) {
                    invarianciaDerivativa.add("T" + (i + 1) + "  ");
                    invarianciaDerivativa.add("T" + (11 - i) + "  ");
                }

            }

            if (quantidadeRepeticoes == 2*vetInt[5]) {
                invarianciaDerivativa.add("T6");
            }

            if (quantidadeRepeticoes == tamanhoEntrada) {
                invarianciaDerivativa.add("T0");
            }
        }
    }

    public ArrayList<String> getInvarianciaDerivativa() {
        return invarianciaDerivativa;
    }

    public void geraInvarianciaInversiva() {
        invarianciaDerivativa = new ArrayList<String>();

        int tamanhoEntrada = numeros.size(),
            quantidadeRepeticoes = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe quantidade de repeti\u00e7\u00f5es desejada"));

        if (quantidadeRepeticoes > tamanhoEntrada) {
            JOptionPane.showMessageDialog(null, "N\u00e3o \u00e9 poss\u00edvel retornar este resultado");
        }
        else {
            MatrizDodecafonica tabela = GeradorTabelaAdicao.geraTabela(numeros);
            int[] contadores = new int[12];

            for (int i = 0; i < tamanhoEntrada; i++) {
                for (int j = 0; j < tamanhoEntrada; j++) {
                    contadores[tabela.get(i, j).inteiro()]++;
                }
            }

            for (int i = 0; i < 12; i++) {
                if (contadores[i] == quantidadeRepeticoes) {
                    invarianciaDerivativa.add("T" + i + "I  ");
                }
            }
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
        ArrayList<Point> camposColorir = new ArrayList<Point>();
        ConjuntoOrdenado hexacordeCorrente, hexacordeRejeitado = serieEscolhida.subSeq(0, 6);

        for (int i = 1; i < 12; i++) {
            hexacordeCorrente = matriz.getP(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(i, j));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getR(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(i, 11 - j));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getI(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(j, i));
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente = matriz.getRI(i).subSeq(0, 6);
            if (hexacordeCorrente.disjuntos(hexacordeRejeitado)) {
                for (int j = 0; j < 6; j++) {
                    camposColorir.add(new Point(11 - j, i));
                }
            }
        }
        return camposColorir;
    }

    public Object geraInvariancia() {
        ArrayList<String> informacoesAssociada = new ArrayList<String>();
        acordesALimpo = new ArrayList<ConjuntoOrdenado>();
        invariancias = new ArrayList<String>();

        encontraInvariancia(informacoesAssociada, 3);
        encontraInvariancia(informacoesAssociada, 4);
        encontraInvariancia(informacoesAssociada, 6);

        int segmentos = acordesALimpo.size();
        String informacaoCorrente, finalLinha, inicio = "";

        for (int i = 0; i < segmentos; i++) {
            informacaoCorrente = informacoesAssociada.get(i);
            finalLinha = informacaoCorrente + "  " + "(" +
                    new StringTokenizer(informacaoCorrente).countTokens() + ")\n\n";

            invariancias.add(acordesALimpo.get(i).toString() + inicio + finalLinha);
        }

        return invariancias;
    }

    public ArrayList<String> getInvariancia() {
        return invariancias;
    }

    private void encontraInvariancia(ArrayList<String> informacoesAssociada, int tamanhoSegmentos) {
        MatrizDeAcordes acordes = new MatrizDeAcordes(tamanhoSegmentos, matriz);
        ArrayList<ConjuntoOrdenado> corrente = null;
        int tamanhoLinha;
        String informacaoDoCorrente = "";
        ConjuntoOrdenado procurado = null;

        do {
            procurado = acordes.getProximoElemento();

            tamanhoLinha = acordes.getMatrizP().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizP().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "P" + matriz.getP(j).get(0) + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "I" + matriz.getI(j).get(0) + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizR().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizR().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "R" + matriz.getP(j).get(0) + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizRI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizRI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "RI" + matriz.getI(j).get(0) + " ";
                }
            }

            if (informacaoDoCorrente.length() >= 6) {
                acordesALimpo.add(procurado);
                informacoesAssociada.add(informacaoDoCorrente);
            }

            informacaoDoCorrente = "";
        }
        while (acordes.getTamanhoTotal() > 0);
    }


}
