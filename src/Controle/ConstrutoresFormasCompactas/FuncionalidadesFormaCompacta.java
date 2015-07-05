/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.ConstrutoresFormasCompactas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Hildegard
 */
public class FuncionalidadesFormaCompacta {
    public static ArrayList<Integer> diferencas(ArrayList<Integer> classes) {
        ArrayList<Integer> resultado = new ArrayList<Integer>();
        int limite = classes.size() - 1;
        
        for (int i = 0; i < limite; i++) {
            resultado.add(Math.abs(classes.get(i) - classes.get(i + 1)));
        }
        
        return resultado;
    }
}
