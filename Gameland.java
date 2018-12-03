/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameland;

import static gameland.configurações.MAX_PARTICIPANTES;
import static gameland.configurações.N_CAMPOS_INFO;
import static gameland.configurações.N_JOGOS;
import static gameland.configurações.MAX_LINHAS_PAGINA;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

/**
 *
 * @author rita
 */
public class Gameland {

    static Scanner input = new Scanner(System.in);
    static boolean matrizComPontos = false;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[] jogos = new String[N_JOGOS];
        String[][] participantes = new String[MAX_PARTICIPANTES][N_CAMPOS_INFO];
        int nParticipantes = 0;
        int[][] pontos = new int[MAX_PARTICIPANTES][N_JOGOS];
        double[][] premios = new double[MAX_PARTICIPANTES][N_JOGOS];
        int op;
        do {
            op = menu();
            switch (op) {
                case 1:
                    input.nextLine();
                    System.out.println("Qual a data do evento (AAAAMMDD)?");
                    String nomeFich = input.nextLine();
                    if (carregarJogosDoEvento(nomeFich + ".txt", jogos) == true) {
                        System.out.println("Jogos carregados com sucesso");
                    } else {
                        System.out.println("Erro no carregamento dos jogos. Verifique ficheiro");
                    }
                    break;
                case 2:
                    visualizarInfoJogos(jogos);
                    break;
                case 3:
                    input.nextLine();
                    System.out.println("Qual é o nome do ficheiro de participantes que pretende carregar (NOMEDAEQUIPA)?");
                    nomeFich = input.nextLine();
                    int aux = lerInfoFicheiro(nomeFich, participantes, nParticipantes);
                    if (aux > nParticipantes) {
                        System.out.println("Equipa carregada com sucesso");
                        nParticipantes = aux;
                    } else {
                        System.out.println("Erro no carregamento da equipa. Verifique ficheiro");
                    }
                    break;
                case 4:
                    listagemPaginada(participantes, nParticipantes);
                    break;
                case 5:
                    input.nextLine();
                    System.out.println("Insira o e-mail do participante que pretende alterar os dados");
                    String email = input.nextLine();
                    actualizarInfoParticipante(email, participantes, nParticipantes);
                    break;
                case 6:
                    input.nextLine();
                    System.out.println("Qual o nome do ficheiro com os resultados do jogo (AAAAMMDD_IDJOGO) que pretende carregar?");
                    String pontosJogo = input.nextLine();
                    carregarPontosJogo(pontos, nParticipantes, participantes, pontosJogo, jogos);
                    if (matrizComPontos = true) {
                        System.out.println("Pontos carregados com sucesso");
                    } else {
                        System.out.println("Erro no carregamento dos pontos do evento. Verifique ficheiro.");
                    }
                    break;
                case 7:
                    if (nParticipantes != 0 && matrizComPontos) {
                        System.out.println("As informações serão guardadas num ficheiro com o nome backup.txt.");
                        guardaDados(nParticipantes, pontos, participantes, jogos);
                    } else {
                        System.out.println("Erro na criação do ficheiro. Insira os dados dos participantes e pontos primeiro.");
                    }

                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static int menu() {
        //Menu da aplicação com as diferentes opções pretendidas
        int op = 0;
        System.out.println("Selecione uma opção:");
        System.out.println("1 - Ler informação dos jogos");
        System.out.println("2 - Visualizar informação dos jogos do evento");
        System.out.println("3 - Adicionar nova equipa ao evento");
        System.out.println("4 - Visualizar informação sobre todos os partipantes inscritos");
        System.out.println("5 - Alterar dados dos participantes");
        System.out.println("6 - Carregar informações da pontuação de um evento");
        System.out.println("7 - Backup da informação existente na memória");
        System.out.println("8 - Cálculo e armazenamento da informação dos prémios obtidos");
        System.out.println("9 - Remover equipa");
        System.out.println("10 - Visualizar prémios por equipa");
        System.out.println("11 - Visualizar pontuação média, percentagem de jogadores que não participaram/desistiram, valor dos prémios atribuídos");
        System.out.println("12 - Informação relativa de todos os participantes");
        System.out.println("0 - Terminar");
        op = input.nextInt();
        return op;
    }

    /**
     * Carregar informação de um ficheiro de texto sobre os jogos do evento
     *
     * @param nomeFichJogos - Nome do ficheiro .txt
     * @param jogos - array com a informação sobre os jogos no formato
     * {ID-Tipodejogo-Máximodepontos;....}
     * @return true se o jogo for carregado false se o ficheiro não for
     * carregado
     */
    private static boolean carregarJogosDoEvento(String nomeFichJogos, String[] jogos) throws FileNotFoundException {
        Scanner fInput = new Scanner(new File(nomeFichJogos));
        int i = 0;
        while (fInput.hasNextLine() && i < N_JOGOS) {
            String linha = fInput.nextLine();
            // Verifica se linha não está em branco
            if ((linha.trim()).length() > 0) {
                jogos[i] = linha;
                i++;
            }
        }
        fInput.close();
        if (i == N_JOGOS) {
            return true;
        }
        return false;
    }

    /**
     * Visualizar informação carregada sobre os jogos do evento
     *
     * @param jogos - array com a informação sobre os jogos no formato
     * {ID-Tipodejogo-Máximodepontos;....} Faz a separação do ID, tipo de jogo e
     * máximo de pontos, e apresenta em forma de tabela
     */
    private static void visualizarInfoJogos(String[] jogos) {
        System.out.println("Jogos do evento");
        System.out.printf("%15s%15s%15s%n",
                "ID do jogo", "Tipo de jogo", "Max. de pontos");
        for (int i = 0; i < jogos.length; i++) {
            String[] temp = jogos[i].split("-");
            System.out.printf("%15s%15s%15s%n", temp[0], temp[1], temp[2]);
        }
    }

    /**
     * Carrega informação dos participantes
     *
     * @param jogos - array com a informação sobre os jogos no formato
     * {ID-Tipodejogo-Máximodepontos;....} Faz a separação do ID, tipo de jogo e
     * máximo de pontos, e apresenta em forma de tabela
     */
    public static int lerInfoFicheiro(String nomeFich, String[][] info, int nElems) throws FileNotFoundException {
        Scanner fInput = new Scanner(new File(nomeFich));
        int nElemsInic = nElems;
        while (fInput.hasNext() && nElems < MAX_PARTICIPANTES) {
            String linha = fInput.nextLine();
            // Verifica se linha não está em branco
            if (linha.trim().length() > 0) {
                nElems = guardarDados(linha, info, nElems);
            }
        }
        fInput.close();
        if (nElems - nElemsInic != 3) {
            nElems = nElemsInic;
        }
        return nElems;
    }

    private static int guardarDados(String linha, String[][] info, int nElems) {
        String[] temp = linha.split(";");
        if (temp.length == N_CAMPOS_INFO) {
            String num = temp[0].trim();
            int pos = pesquisarElemento(num, nElems, info);
            if (pos == -1) {
                for (int i = 0; i < N_CAMPOS_INFO; i++) {
                    info[nElems][i] = temp[i].trim();
                }
                nElems++;
            }
        }
        return nElems;
    }

    /**
     * Pesquisar linha de matriz por primeiro elemento da linha
     *
     * @param valor - elemento a pesquisar
     * @param nEl - nº de elementos da matriz
     * @param mat - matriz com a informação
     * @return -1 se não existe linha com esse valor ou o nº da linha cujo
     * primeiro elemento é esse valor
     */
    public static int pesquisarElemento(String valor, int nEl, String[][] mat) {
        for (int i = 0; i < nEl; i++) {
            if (mat[i][0].equals(valor)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Visualizar toda a informação ( paginada)dos participantes existente em
     * memória
     *
     * @param matriz- matriz com a informação a listar
     * @param nEl – nº de linhas com informação
     */
    private static void listagemPaginada(String[][] matriz, int nEl) {
        int contPaginas = 0;
        for (int i = 0; i < nEl; i++) {
            if (i % MAX_LINHAS_PAGINA == 0) {
                if (contPaginas > 0) {
                    pausa();
                }
                contPaginas++;
                System.out.println("\nPÁGINA: " + contPaginas);
                cabecalho();
            }
            for (int j = 0; j < N_CAMPOS_INFO; j++) {
                if (j == 1) {
                    System.out.printf("%30s", matriz[i][j]);
                } else {
                    System.out.printf("%10s", matriz[i][j]);
                }
            }
            System.out.println("");
        }
        pausa();
    }

    private static void cabecalho() {
        System.out.printf("%50s%n", "PARTICIPANTES");
        System.out.printf("%75s%n", "==================================================");
    }

    private static void pausa() {
        System.out.println("\n\nPara continuar digite ENTER\n");
        input.nextLine();
    }

    /**
     * Atualiza informação alterável de um participante
     *
     * @param eMail – mail do participante
     * @param - matriz com toda a informação dos participantes
     * @param nElems - número de elementos
     * @return false se o eMail não foi encontrado ou true se foi encontrado e
     * atualizado
     */
    public static boolean actualizarInfoParticipante(String eMail, String[][] matriz, int nElems) {
        int pos;
        if ((pos = pesquisarElemento(eMail, nElems, matriz)) > -1) {
            int op;
            do {
                Formatter out = new Formatter(System.out);
                mostrarParticipante(out, matriz[pos]);
                op = menuInfoParticipante();
                switch (op) {
                    case 1:
                        System.out.println("Novo EMail:");
                        matriz[pos][0] = input.nextLine();
                        break;
                    case 2:
                        System.out.println("Novo nome:");
                        matriz[pos][1] = input.nextLine();
                        break;
                    case 3:
                        System.out.println("Nova data nascimento:");
                        matriz[pos][2] = input.nextLine();
                        break;
                    case 0:
                        System.out.println("FIM");
                        break;
                    default:
                        System.out.println("Opção incorreta");
                        break;
                }
            } while (op != 0);
            return true;
        }
        System.out.printf(
                "O participante %s não foi encontrado!", eMail);
        return false;
    }

    /**
     * Mostrar a informação de um participante separada por ;
     *
     * @param out - instancia de formatter associado ao stream pretendido
     * @param participante - array de Strings com a informação do participante
     */
    private static void mostrarParticipante(Formatter out,
            String[] participante) {
        for (int j = 0; j < N_CAMPOS_INFO; j++) {
            if (j == 1) {
                out.format("%30s;", participante[j]);
            } else {
                out.format("%12s;", participante[j]);
            }
        }
    }

    /**
     * Apresenta o menu de opções de atualização de dados atualizáveis do
     * partipante
     *
     * @return um inteiro que é a opção escolhida
     */
    private static int menuInfoParticipante() {
        String texto = "ATUALIZAR INFORMAÇÃO DE PARTICIPANTE"
                + "\n EMail ... 1"
                + "\n NOME ... 2"
                + "\n DATA NASCIMENTO ... 3"
                + "\n TERMINAR ... 0"
                + "\n\nQUAL A SUA OPÇÃO?";
        System.out.printf("%n%s%n", texto);
        int op = input.nextInt();
        input.nextLine();
        return op;
    }

    private static void carregarPontosJogo(int[][] pontos, int nParticipantes, String[][] participantes, String pontosJogo, String[] jogos) throws FileNotFoundException {
        String[] tempId = pontosJogo.split("_");
        String idJogo = tempId[tempId.length - 1];
        String[] idJogoT = new String[N_JOGOS];
        String[] emailPontos = new String[MAX_PARTICIPANTES];
        int linhaId = 0;
        for (int i = 0; i < jogos.length; i++) {
            idJogoT[i] = ((jogos[i].split("-"))[0]);
        }
        for (int i = 0; i < idJogoT.length; i++) {
            if (idJogoT[i].equalsIgnoreCase(idJogo)) {
                linhaId = i;
                break;
            }
        }
        int i = 0;
        Scanner fInput = new Scanner(new File(pontosJogo));
        while (fInput.hasNextLine() && i < nParticipantes) {
            String linha = fInput.nextLine();
            // Verifica se linha não está em branco
            if ((linha.trim()).length() > 0) {
                if (validarPontos(jogos, pontosJogo, idJogo, linhaId, linha) == true) {
                    emailPontos = linha.split(";");
                    String em = emailPontos[0].trim();
                    int emLinha = pesquisarElemento(emailPontos[0], nParticipantes, participantes);
                    int nPontos = Integer.parseInt(emailPontos[1].trim());
                    preencherMatrizPontos(nPontos, emLinha, pontos, linhaId, nParticipantes);
                }
            }
            i++;
        }
    }

    private static boolean validarPontos(String[] jogos, String pontosJogo, String idJogo, int linhaId, String linha) throws FileNotFoundException {
        String[] temp = jogos[linhaId].split("-");
        int pontosMax = Integer.parseInt(temp[temp.length - 1]);
        String[] tempLinha = linha.split(";");
        int pontosLinha = Integer.parseInt(tempLinha[tempLinha.length - 1].trim());
        if (pontosMax < pontosLinha) {
            return false;
        }
        return true;
    }

    private static void preencherMatrizPontos(int nPontos, int emLinha, int[][] pontos, int linhaId, int nParticipantes) {
        pontos[linhaId][emLinha] = nPontos;
        matrizComPontos = true;

    }

    public static void guardaDados(int nParticipantes, int[][] pontos, String[][] participantes, String[] jogos) throws IOException {
        File ficheiro = new File("backup.txt");
        String[] idJogoT = new String[N_JOGOS];
        for (int i = 0; i < jogos.length; i++) {
            idJogoT[i] = ((jogos[i].split("-"))[0]);
        }
        try {
            try (Formatter write = new Formatter(ficheiro)) {
                write.format("|%-10s|", " ");
                for (int i = 0; i < nParticipantes; i++) {
                    write.format("|%-10s| ", participantes[i][0]);
                }
                write.format("%n");
                for (int i = 0; i < nParticipantes; i++) {
                    write.format("|%-10s|", idJogoT[i]);
                    for (int j = 0; j < N_JOGOS; j++) {
                        write.format("|%-10s| ", pontos[i][j]);
                    }
                    write.format("%n");
                }
            }
        } catch (IOException err) {
            System.out.println(err);
        }
    }

    public static void preencherPremios(double[][] premios, int[][] pontos, int nParticipantes) {
        for (int i = 0; i < nParticipantes; i++);

    }

    public static int[] maximoPontos(String[] jogos) {
        int[] maxPontosJogo = new int[N_JOGOS];
        for (int i = 0; i < jogos.length; i++) {
            maxPontosJogo[i] = Integer.parseInt((jogos[i].split("-")[2]));
        }
        return maxPontosJogo;
    }

    public static double[] primeiroLugar(int[][] pontos, int nParticipantes, int[] maxPontosJogo) {
        int[] maxPontos = new int[N_JOGOS];
        double[] premioPrim = new double[N_JOGOS];
        for (int i = 0; i < nParticipantes; i++) {
            maxPontos[i] = pontos[i][0];
            for (int j = 0; j < N_JOGOS; j++) {
                if (pontos[i][j] > maxPontos[i]) {
                    maxPontos[i] = pontos[i][j];
                }
            }
        }
        for (int i = 0; i < N_JOGOS; i++) {
            premioPrim[i] = (double) maxPontos[i] / maxPontosJogo[i] * 100;
        }
        return premioPrim;
    }   

    public static double[] premioEquipa(int[][] pontos, int nParticipantes, int[] maxPontosJogo) {

    

    

    public static double somaPremios() {

    }
}