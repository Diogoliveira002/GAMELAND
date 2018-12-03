/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameland;

import java.util.Calendar;

/**
 *
 * @author rita
 */
public class utilitários {

    /**
     * Verifica se um determinado nome contem apelido
     *
     * @param nome – nome completo
     * @param apelido – apelido a procurar
     * @return true ou false conforme o nome contem ou não o apelido
     */
    public static boolean verificaSeContemApelido(String nome, String apelido) {
// String implements CharSequence
        return nome.contains(apelido);
    }

    /**
     * Dado o nome completo retorna o apelido e a primeira letra .
     *
     * @param str nome completo
     * @return o String último nome seguido da 1ªletra.
     */
    public static String apelidoELetraInicial(String str) {
        str = str.trim();
        int last = str.lastIndexOf(' '); // posição do último espaço
// substring da posição do último espaço +1 até ao fim.
        String resultado = str.substring(last + 1) + " " + str.substring(0, 1) + ".";
        return resultado;
    }

    /**
     * Converter data no formato dd/mm/ano em aaaammdd
     *
     * @param data data no formato dd/mm/ano
     * @return data no formato aaaammdd
     */
    public static String converterddmmaaaaParaaaammdd(String data) {
        String[] aux = data.trim().split("/");
        String dia = aux[0].length() < 2 ? "0" + aux[0] : aux[0];
        String mes = aux[1].length() < 2 ? "0" + aux[1] : aux[1];
        return aux[2] + mes + dia;
    }

    /**
     * Dada a data de nascimento retorna a idade
     *
     * @param anoMesDia data de nascimento no formato aaaammdd
     * @return idade
     */
    public static int idade(String anoMesDia) {
        int ano = Integer.parseInt(anoMesDia.substring(0, 4));
        int mes = Integer.parseInt(anoMesDia.substring(4, 6));
        int dia = Integer.parseInt(anoMesDia.substring(6, 8));
        Calendar hoje = Calendar.getInstance();
        int diaH = hoje.get(Calendar.DAY_OF_MONTH);
        int mesH = hoje.get(Calendar.MONTH) + 1;
        int anoH = hoje.get(Calendar.YEAR);
        if (mesH > mes || mesH == mes && diaH >= dia) {
            return anoH - ano;
        }
        return anoH - ano - 1;
    }

}
