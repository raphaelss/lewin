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

import Controle.DadosMusicais.MatrizDodecafonica;
import Controle.DadosMusicais.MatrizDeAcordes;
import Controle.DadosMusicais.SerieDodecafonica;
import Controle.DadosMusicais.SegmentoInvariancia;
import Controle.DadosMusicais.Acorde;
import Excecoes.DadosProibidos;
import Controle.ConstrutoresFormasCompactas.ConstrutorFormaNormalForte;
import Controle.ConstrutoresFormasCompactas.ConstrutorFormaNormalStraus;
import Controle.ConstrutoresFormasCompactas.ConstrutorFormaPrimaForte;
import Controle.ConstrutoresFormasCompactas.ConstrutorFormaPrimaStraus;
import Controle.ConstrutoresFormasCompactas.FuncionalidadesFormaCompacta;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Hildegard
 */
public class Controlador {
    private ArrayList<Integer> numeros = new ArrayList<Integer>(),
                               segundoConjunto = new ArrayList<Integer>(),
                               formaCompacta, resultadoMultiplicacao;
    private ArrayList<SerieDodecafonica> listadeFormas;
    private SerieDodecafonica serieEscolhida;
    private MatrizDodecafonica matriz;
    private ArrayList<SegmentoInvariancia> acordesALimpo;
    private ArrayList<String> informacoesAssociada, invarianciaDerivativa, invariancias;
    private ArrayList<Integer[]> resultadoRotacaoStravinskyana;
    private ArrayList<ArrayList<Integer[]>> resultadosPaleta;
    private HashSet<LinkedList<Integer>> subconjuntos;
    private int[] vetorIntervalar = null;
    private double similaridade;

    public void limparDados() {
        numeros.clear();
        segundoConjunto.clear();
    }

    public ArrayList<Integer> getConjuntoPrincipal() {
        return numeros;
    }

    public ArrayList<Integer> getSegundoConjunto() {
        return segundoConjunto;
    }

    public MatrizDodecafonica getMatriz() {
        return matriz;
    }

    public void adicionaEntradaConjuntoPrincipal(int numero) throws DadosProibidos {
        if (numeros.contains(numero)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
        if (numeros.size() == 12) {
            throw new DadosProibidos("N\u00e3o s\u00e3o permitidos mais que 12 n\u00fameros");
        }

        numeros.add(numero);
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
                >= listadeFormas.size());

        numeros.clear();
        for (int i = 0; i < 12; i++) {
            numeros.add(listadeFormas.get(indice).getDado(i));
        }
    }

    public void substitui_Paleta() {
        int indice = 0;
        while((indice =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Selecione o \u00edndice")))
                >= resultadoRotacaoStravinskyana.size());

        numeros.clear();
        Integer[] escolhido = resultadoRotacaoStravinskyana.get(indice);
        for (int i = 0; i < escolhido.length; i++) {
            numeros.add(escolhido[i]);
        }
    }

    public void removerConjuntoPrincipal() {
        numeros.remove(numeros.size() - 1);
    }

    public void adicionaEntradaSegundoConjunto(int numero) throws DadosProibidos {
        if (segundoConjunto.contains(numero)) {
            throw new DadosProibidos("Dados repetidos não são permitidos");
        }
        if (segundoConjunto.size() == 12) {
            throw new DadosProibidos("N\u00e3o s\u00e3o permitidos mais que 12 n\u00fameros");
        }

        segundoConjunto.add(numero);
    }

    public boolean removerSegundoConjunto() {
        segundoConjunto.remove(segundoConjunto.size() - 1);

        return !segundoConjunto.isEmpty();
    }

    public void geraDerivacaoSerial() throws DadosProibidos {
        int tamanho = numeros.size();

        if(tamanho == 3 && numeros.get(0) == 0) {
            int primeiro = numeros.get(1), segundo = numeros.get(2);

            if ((primeiro == 4 && segundo == 8) || (primeiro == 3 && segundo == 6)) {
                throw new DadosProibidos();
            }
        }
        else if (tamanho == 4) {
            if (FuncionalidadesFormaCompacta.diferencas(
                    new ConstrutorFormaPrimaStraus(numeros).retornaForma()).contains(4)) {
                throw new DadosProibidos();
            }
        }

        listadeFormas = new GeradorDerivacaoSerial(new Acorde(numeros)).resultado();
        if (listadeFormas.isEmpty()) {
            throw new DadosProibidos();
        }
    }

    public ArrayList<SerieDodecafonica> getDerivacaoSerial() {
        return listadeFormas;
    }

    public void geraMatrizDodecafonica() {
        matriz = new MatrizDodecafonica();

        int[] acorde = new int[12];

        if (numeros.size() < 12) {
            int indice = 0;
            while((indice =
                    Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Selecione o \u00edndice da s\u00e9rie de entrada da matriz")))
                    >= listadeFormas.size());

            serieEscolhida = listadeFormas.get(indice);

            for (int i = 0; i < 12; i++) {
                acorde[i] = serieEscolhida.getDado(i);
            }
        }
        else {
            for (int i = 0; i < 12; i++) {
                acorde[i] = numeros.get(i);
            }

            serieEscolhida = new SerieDodecafonica();
            serieEscolhida.adiciona(new Acorde(acorde));
        }

        try {
            matriz.setLinha(0, acorde);

            int[] inversoProvisorio = new int[12];

            for (int i = 0; i < 12; i++) {
                inversoProvisorio[i] = 12 - serieEscolhida.getDado(i);
            }

            int diferenca = inversoProvisorio[0] - serieEscolhida.getDado(0);

            for (int i = 0; i < 12; i++) {
                matriz.preenchePosicao(i, 0, ((inversoProvisorio[i] - diferenca) + 12) % 12);
            }

            for (int i = 1; i < 12; i++) {
                diferenca = matriz.getValor(i, 0) - matriz.getValor(i - 1, 0);

                for (int j = 1; j < 12; j++) {
                    matriz.preenchePosicao(i, j,
                            ((matriz.getValor(i - 1, j) + diferenca) + 12) % 12);
                }
            }
        }
        catch(NumberFormatException dp) {
            JOptionPane.showMessageDialog(null, "H\u00e1 dados n\u00e3o permitidos. Preencha corretamente");
        }
    }

    private void transpoeParaZero() {
        int primeiro = formaCompacta.get(0), subtracao;
        for (int i = 0; i < formaCompacta.size(); i++) {
            subtracao = formaCompacta.get(i) - primeiro;
            if (subtracao < 0) {
                subtracao = 12 - Math.abs(subtracao);
            }
            formaCompacta.set(i, subtracao);
        }
    }

    public void geraFormaPrimaStraus() {
//        formaCompacta = ((FormaCompacta)new ConstrutorFormaPrimaRahn(numeros).retornaForma()).getClasses();
        formaCompacta = new ConstrutorFormaPrimaStraus(numeros).retornaForma();
        ArrayList<Integer> complemento = new ArrayList<>();
        for (int i : formaCompacta) {
            complemento.add((12 - Math.abs(i)) % 12);
        }

        complemento = new ConstrutorFormaPrimaStraus(complemento).retornaForma();

        int intervalo1 = formaCompacta.get(1) - formaCompacta.get(0),
            intervalo2 = complemento.get(1) - complemento.get(0);
        if (intervalo1 < 0) {
            intervalo1 = (12 - Math.abs(intervalo1) % 12);
        }
        if (intervalo2 < 0) {
            intervalo2 = (12 - Math.abs(intervalo2) % 12);
        }
        if (intervalo1 > intervalo2) {
            formaCompacta = complemento;
        }
        transpoeParaZero();
    }

    public void geraFormaPrimaForte() {
//        formaCompacta = ((FormaCompacta)new ConstrutorFormaPrimaForte(numeros).retornaForma()).getClasses();
        formaCompacta = new ConstrutorFormaPrimaForte(numeros).retornaForma();
        ArrayList<Integer> complemento = new ArrayList<>();
        for (int i : formaCompacta) {
            complemento.add((12 - Math.abs(i)) % 12);
        }

        complemento = new ConstrutorFormaPrimaStraus(complemento).retornaForma();

        int intervalo1 = formaCompacta.get(1) - formaCompacta.get(0),
            intervalo2 = complemento.get(1) - complemento.get(0);
        if (intervalo1 < 0) {
            intervalo1 = (12 - Math.abs(intervalo1) % 12);
        }
        if (intervalo2 < 0) {
            intervalo2 = (12 - Math.abs(intervalo2) % 12);
        }
        if (intervalo1 > intervalo2) {
            formaCompacta = complemento;
        }
        transpoeParaZero();
    }

    public ArrayList<Integer> getFormaCompacta() {
        return formaCompacta;
    }

    public void geraFormaNormalStraus() {
//        formaCompacta = ((FormaCompacta)new ConstrutorFormaNormal(numeros).retornaForma()).getClasses();
        formaCompacta = new ConstrutorFormaNormalStraus(numeros).retornaForma();
    }

    public void geraFormaNormalForte() {
        formaCompacta = new ConstrutorFormaNormalForte(numeros).retornaForma();
    }

    public void geraRotacaoStravinskyana() {
        ArrayList<Integer> segundaParte = new ArrayList<Integer>(numeros.subList(6, 12)),
                           subtraido = new ArrayList<Integer>();
        final int primeiro = segundaParte.get(0);
        int subtrator;

        resultadoRotacaoStravinskyana = new ArrayList<Integer[]>();
        resultadoRotacaoStravinskyana.add(numeros.subList(0, 6).toArray(new Integer[6]));
        resultadoRotacaoStravinskyana.add(segundaParte.toArray(new Integer[6]));

        for (int i = 0; i < 5; i++) {
            segundaParte.add(segundaParte.remove(0));
            subtrator = segundaParte.get(0) - primeiro;

            for (int seg : segundaParte) {
                subtraido.add(((seg - subtrator) + 12) % 12);
            }

            resultadoRotacaoStravinskyana.add(subtraido.toArray(new Integer[6]));
            subtraido.clear();
        }
    }

    public ArrayList<Integer[]> getRotacaoStravinskyana() {
        return resultadoRotacaoStravinskyana;
    }

    public void geraPaleta() {
        int tamanho = numeros.size();
        ArrayList<ArrayList<Integer>> conjuntosDiretos = new ArrayList<ArrayList<Integer>>(),
                                      conjuntosInversos = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> conjuntoReferencia = new ArrayList<Integer>(numeros);

        adicionaTransposicoesDistintas(conjuntosDiretos, conjuntoReferencia);

        for(int i = 0; i < tamanho; i++) {
            conjuntoReferencia.set(i, (12 - conjuntoReferencia.get(i)) % 12);
        }

        adicionaTransposicoesDistintas(conjuntosInversos, conjuntoReferencia);
        resultadosPaleta = new ArrayList<ArrayList<Integer[]>>(2);

        resultadosPaleta.add(new ArrayList<Integer[]>());
        for (ArrayList<Integer> conjunto : conjuntosDiretos) {
            resultadosPaleta.get(0).add(conjunto.toArray(new Integer[tamanho]));
        }

        resultadosPaleta.add(new ArrayList<Integer[]>());
        loop:
        for (ArrayList<Integer> conjunto : conjuntosInversos) {
            for (ArrayList<Integer> direto : conjuntosDiretos) {
                if (direto.containsAll(conjunto)) {
                    continue loop;
                }
            }
            resultadosPaleta.get(1).add(conjunto.toArray(new Integer[tamanho]));
        }
    }

    public ArrayList<ArrayList<Integer[]>> getPaleta() {
        return resultadosPaleta;
    }

    private void adicionaTransposicoesDistintas(ArrayList<ArrayList<Integer>> conjuntos,
                                                ArrayList<Integer> primeiroConjunto) {
        conjuntos.add(new ArrayList<Integer>(primeiroConjunto));
        loop:
        for (int i = 1; i < 12; i++) {
            ArrayList<Integer> atual = new ArrayList<Integer>();
            for (int j = 0; j < primeiroConjunto.size(); j++) {
                atual.add((primeiroConjunto.get(j) + i) % 12);
            }
            for (ArrayList<Integer> c : conjuntos) {
                if (c.containsAll(atual)) {
                    continue loop;
                }
            }
            conjuntos.add(atual);
        }
    }

    public void geraTabelaAdicao() {
        matriz = GeradorTabelaAdicao.geraTabela(numeros);
    }

    public void gerarVetorIntervalar() {
        vetorIntervalar = GeradorVetorIntervalar.geraVetor(numeros);
    }

    public int[] getVetorIntervalar() {
        return vetorIntervalar;
    }

    public void geraSubconjuntos() {
        subconjuntos = new HashSet<LinkedList<Integer>>();

        int tamanhoSubconjuntos = 0;
        while((tamanhoSubconjuntos =
                Integer.parseInt(JOptionPane.showInputDialog(null,
                "Informe o tamanho dos subconjuntos")))
                >= numeros.size());

        constroi(0, tamanhoSubconjuntos, new LinkedList<Integer>(), subconjuntos);
    }

    public HashSet<LinkedList<Integer>> getSubconjuntos() {
        return subconjuntos;
    }

    private void constroi (int indice, int tamanhoSubconjuntos, LinkedList<Integer> subconjunto, HashSet<LinkedList<Integer>> subconjuntos) {
        for (; indice < numeros.size(); indice++) {
            subconjunto.add(numeros.get(indice));
            if (subconjunto.size() == tamanhoSubconjuntos) {
                subconjuntos.add(new LinkedList<Integer>(subconjunto));
                subconjunto.removeLast();
            }
            else {
                constroi(indice + 1, tamanhoSubconjuntos, subconjunto, subconjuntos);
            }
        }

        try {
            subconjunto.removeLast();
        }
        catch(NoSuchElementException nsee) {}
    }

    public void geraSimilaridade() {
        int tamanhoPrimeiro = numeros.size(), tamanhoSegundo = segundoConjunto.size();

        if (vetorIntervalar == null) {
            vetorIntervalar = GeradorVetorIntervalar.geraVetor(numeros);
        }

        int[] vetorIntervalarSegundoConjunto = GeradorVetorIntervalar.geraVetor(segundoConjunto);
        double resultado = 0;

        for (int i = 0; i < 6; i++) {
            resultado += Math.sqrt(vetorIntervalar[i]*vetorIntervalarSegundoConjunto[i]);
        }
        resultado *= 2;
        resultado /= Math.sqrt(tamanhoPrimeiro*(tamanhoPrimeiro - 1)*
                                tamanhoSegundo*(tamanhoSegundo - 1));

        similaridade = resultado;
    }

    public double getSimilaridade() {
        return similaridade;
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
            int [] vetInt = GeradorVetorIntervalar.geraVetor(numeros);
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
                    contadores[tabela.getValor(i, j)]++;
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
        resultadoMultiplicacao = new ArrayList<Integer>();

        for (int doSegundoFator : segundoConjunto) {
            resultadoMultiplicacao.add(doSegundoFator);

            for (int i = 1; i < numeros.size(); i++) {
                doSegundoFator += numeros.get(i) - numeros.get(i - 1);
                resultadoMultiplicacao.add((doSegundoFator + 12) % 12);
            }
        }

        Collections.sort(resultadoMultiplicacao);
        resultadoMultiplicacao = new ArrayList<Integer>(new HashSet<Integer>(resultadoMultiplicacao));
    }

    public void geraMultiplicacaoRahn1() {
        resultadoMultiplicacao = new ArrayList<Integer>();

        int doSegundoFator = segundoConjunto.get(0);

        for (int doPrimeiroFator : numeros) {
            resultadoMultiplicacao.add((doSegundoFator*doPrimeiroFator) % 12);
        }
    }

    public void geraMultiplicacaoRahn2() {
        int tamanho = numeros.size() + segundoConjunto.size() - 1;
        int[] resultadoParcial = new int [tamanho];
        int indiceAPartir = 0, iterandoAPartir;

        for (int doPrimeiroFator : numeros) {
            iterandoAPartir = indiceAPartir++;

            for (int doSegundoFator : segundoConjunto) {
                resultadoParcial[iterandoAPartir++] += doPrimeiroFator*doSegundoFator;
            }
        }

        HashSet<Integer> resultadoSemRepetidos = new HashSet<Integer>();
        for (int i : resultadoParcial) {
            resultadoSemRepetidos.add(i % 12);
        }

        resultadoMultiplicacao = new ArrayList<Integer>(resultadoSemRepetidos);
    }

    public ArrayList<Integer> getMultiplicacao() {
        return resultadoMultiplicacao;
    }

    public ArrayList<Point> geraCombinatoriedade() {
        ArrayList<Point> camposColorir = new ArrayList<Point>();
        ArrayList<Integer> hexacordeCorrente = new ArrayList<Integer>(),
                hexacordeRejeitado = serieEscolhida.toIntegerList(6);

        //procura nas series P
        lacoPrincipal:
        for (int i = 1; i < 12; i++) {
            hexacordeCorrente.clear();

            int[] r = matriz.getP(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 0; j < 6; j++) {
                camposColorir.add(new Point(i, j));
            }
        }

        //procura nas series R
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            int[] r = matriz.getR(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 6; j < 12; j++) {
                camposColorir.add(new Point(i, j));
            }
        }

        //procura nas series I
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            int[] r = matriz.getI(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 0; j < 6; j++) {
                camposColorir.add(new Point(j, i));
            }
        }

        //procura nas series RI
        lacoPrincipal:
        for (int i = 0; i < 12; i++) {
            hexacordeCorrente.clear();

            int[] r = matriz.getRI(i);
            for (int j = 0; j < 6; j++) {
                hexacordeCorrente.add(r[j]);
            }

            for (int j = 0; j < 6; j++) {
                if (hexacordeCorrente.contains(hexacordeRejeitado.get(j)) ||
                        hexacordeRejeitado.contains(hexacordeCorrente.get(j))) {
                    continue lacoPrincipal;
                }
            }

            for (int j = 6; j < 12; j++) {
                camposColorir.add(new Point(j, i));
            }
        }

        return camposColorir;
    }

    public Object geraInvariancia() {
        informacoesAssociada = new ArrayList<String>();
        acordesALimpo = new ArrayList<SegmentoInvariancia>();
        invariancias = new ArrayList<String>();

        encontraInvariancia(3);
        encontraInvariancia(4);
        encontraInvariancia(6);

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

    private void encontraInvariancia(int tamanhoSegmentos) {
        MatrizDeAcordes acordes = new MatrizDeAcordes(tamanhoSegmentos, matriz);
        ArrayList<SegmentoInvariancia> corrente = null;
        int tamanhoLinha;
        String informacaoDoCorrente = "";
        SegmentoInvariancia procurado = null;

        do {
            procurado = acordes.getProximoElemento();

            tamanhoLinha = acordes.getMatrizP().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizP().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "P" + matriz.getP(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "I" + matriz.getI(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizR().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizR().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "R" + matriz.getP(j)[0] + " ";
                }
            }

            tamanhoLinha = acordes.getMatrizRI().size();
            for (int j = 0; j < tamanhoLinha; j++) {
                corrente = acordes.getMatrizRI().get(j);
                if (corrente.remove(procurado)) {
                    informacaoDoCorrente += "RI" + matriz.getI(j)[0] + " ";
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
