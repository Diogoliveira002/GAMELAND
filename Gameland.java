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
import static gameland.utilitários.converterddmmaaaaParaaaammdd;
import static gameland.utilitários.idade;
import static gameland.utilitários.verificarAniversario;

/**
 *
 * @author rita
 */
public class Gameland {

    static Scanner input = new Scanner(System.in);
    static boolean matrizComPontos = false;
    static final int VALOR_EQUIPA = 50;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[] jogos = new String[N_JOGOS];
        String[][] participantes = new String[MAX_PARTICIPANTES][N_CAMPOS_INFO];
        int nParticipantes = 0;
        int[][] pontos = new int[MAX_PARTICIPANTES][N_JOGOS];
        double[][] premios = new double[MAX_PARTICIPANTES][N_JOGOS];
        int op;
        String dataEvento = null;
        String pontosJogo = null;
        double [] numParticipantes = new double [N_JOGOS];
        double [] mediasJogos = new double [N_JOGOS];
        double [] percentagens = new double[N_JOGOS];
        double [] somasPremios = new double [N_JOGOS];
        String [] idJogoColunas = new String [N_JOGOS];
        do {
            op = menu();
            switch (op) {
                case 1:
                    input.nextLine();
                    System.out.println("Qual a data do evento (AAAAMMDD)?");
                    dataEvento = input.nextLine();
                    if (carregarJogosDoEvento(dataEvento + ".txt", jogos) == true) {
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
                    String nomeFich = input.nextLine();
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
                    pontosJogo = input.nextLine();
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
                    premios = preencherPremios(premios, pontos, nParticipantes, jogos, dataEvento, participantes, pontosJogo);
                    break;
                case 9:
                    input.nextLine();
                    System.out.println("Indique o nome da equipa que pretende eliminar os dados.");
                    String equipa = input.nextLine();
                    int aux2 = eliminarEquipa(participantes, nParticipantes, equipa);
                    if (aux2 < nParticipantes) {
                        System.out.println("Equipa eliminada com sucesso");
                        nParticipantes = aux2;
                    } else {
                        System.out.println("Erro no carregamento da equipa. Verifique ficheiro");
                    }
                    break;
                case 10:
                    break;
                case 11:
                    input.nextLine();
                    VisualizarInformJogos(pontos, nParticipantes, mediasJogos, percentagens, idJogoColunas, premios, somasPremios);
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
        String[] tempId = new String[2];
        String[] emailPontos = new String[MAX_PARTICIPANTES];
        tempId = pontosJogo.split("_");
        String idJogo = tempId[tempId.length - 1];
        int linhaId = idJogo(jogos, pontosJogo);
        Scanner fInput = new Scanner(new File(pontosJogo));
        int i = 0;
        while (fInput.hasNextLine() && i < nParticipantes) {
            String linha = fInput.nextLine();
            // Verifica se linha não está em branco
            if ((linha.trim()).length() > 0) {
                if (validarPontos(jogos, pontosJogo, idJogo, linhaId, linha)) {
                    emailPontos = linha.split(";");
                    String em = emailPontos[0].trim();
                    int emLinha = pesquisarElemento(em, nParticipantes, participantes);
                    int nPontos = Integer.parseInt(emailPontos[1].trim());
                    if (pesquisarElemento(em, nParticipantes, participantes) != -1) {
                        preencherMatrizPontos(nPontos, emLinha, pontos, linhaId, nParticipantes);
                    }
                }
                i++;
            }
        }
        fInput.close();
    }

    private static int idJogo(String[] jogos, String pontosJogo) {
        String[] tempId = pontosJogo.split("_");
        String idJogo = tempId[tempId.length - 1];
        String[] idJogoT = new String[N_JOGOS];
        int id = 0;
        for (int i = 0; i < jogos.length; i++) {
            idJogoT[i] = ((jogos[i].split("-"))[0]);
        }
        for (int i = 0; i < idJogoT.length; i++) {
            if (idJogoT[i].equalsIgnoreCase(idJogo)) {
                id = i;
                break;
            }
        }
        return id;
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
        pontos[emLinha][linhaId] = nPontos;
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
                for (int i = 0; i < N_JOGOS; i++) {
                    write.format("|%-10s|", idJogoT[i]);
                }
                write.format("%n");
                for (int i = 0; i < nParticipantes; i++) {
                    write.format("|%-10s| ", participantes[i][0]);
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

    public static double[][] preencherPremios(double[][] premios, int[][] pontos, int nParticipantes, String[] jogos, String dataEvento, String[][] participantes, String pontosJogo) {
        int[] maxPontosJogo = maximoPontos(jogos);
        double[][] premioPrim = primeiroLugar(pontos, nParticipantes, maxPontosJogo);
        int[][] premioEquipas = premioEquipa(pontos, nParticipantes, maxPontosJogo, jogos, pontosJogo);
        int[][] premioAniversarios = premioAniversario(participantes, dataEvento, nParticipantes);
        for (int i = 0; i < nParticipantes; i++) {
            for (int j = 0; j < N_JOGOS; j++) {
                premios[i][j] = ((double) (premioEquipas[i][j] + premioPrim[i][j]));
                if ((premios[i][j] != 0)) {
                    premios[i][j] = ((double) (premios[i][j] + premioAniversarios[i][j]));
                }
            }
        }
        return premios;
    }

    public static int[] maximoPontos(String[] jogos) {
        int[] maxPontosJogo = new int[N_JOGOS];
        for (int i = 0; i < jogos.length; i++) {
            maxPontosJogo[i] = Integer.parseInt((jogos[i].split("-")[2]));
        }
        return maxPontosJogo;
    }

    public static double[][] primeiroLugar(int[][] pontos, int nParticipantes, int[] maxPontosJogo) {
        int[] maxPontosAux = new int[N_JOGOS];
        double[][] premioPrim = new double[nParticipantes][N_JOGOS];
        int[][] maxPontos = new int[nParticipantes][N_JOGOS];
        for (int j = 0; j < N_JOGOS; j++) {
            maxPontosAux[j] = pontos[0][j];
            for (int i = 0; i < nParticipantes; i++) {
                if (pontos[i][j] > maxPontosAux[j]) {
                    maxPontosAux[j] = pontos[i][j];
                }
            }
        }
        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes; i++) {
                if (maxPontosAux[j] == pontos[i][j]) {
                    maxPontos[i][j] = pontos[i][j];
                }
            }

        }
        for (int i = 0; i < nParticipantes; i++) {
            for (int j = 0; j < N_JOGOS; j++) {
                premioPrim[i][j] = ((double) maxPontos[i][j] / maxPontosJogo[j] * 100);
            }
        }
        return premioPrim;
    }

    public static int[][] primeiroLugarEquipa(int[][] pontos, int nParticipantes, int[] maxPontosJogo, String[] jogos, String pontosJogo) {
        int[][] pontosEquipa = new int[nParticipantes / 3][N_JOGOS];
        int id = idJogo(jogos, pontosJogo);
        int k = 0;
        for (int i = 0; i < nParticipantes / 3; i++) {
            pontosEquipa[i][id] = pontos[k][id] + pontos[k + 1][id] + pontos[k + 2][id];
            k = k + 3;
        }
        return pontosEquipa;
    }

    public static int[][] premioEquipa(int[][] pontos, int nParticipantes, int[] maxPontosJogo, String[] jogos, String pontosJogo) {
        int[] maxEquipa = new int[N_JOGOS];
        int[][] maxPontosEquipa = new int[nParticipantes / 3][N_JOGOS];
        int[] posEquipa = new int[N_JOGOS];
        int id = idJogo(jogos, pontosJogo);
        int[][] pontosEquipa = primeiroLugarEquipa(pontos, nParticipantes, maxPontosJogo, jogos, pontosJogo);
        int[][] premiosEquipa = new int[nParticipantes][N_JOGOS];

        for (int j = 0; j < N_JOGOS; j++) {
            maxEquipa[j] = pontosEquipa[0][j];
            for (int i = 0; i < nParticipantes / 3; i++) {
                if (pontosEquipa[i][j] > maxEquipa[j]) {
                    maxEquipa[j] = pontosEquipa[i][j];
                }
            }
        }
        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes / 3; i++) {
                if (maxEquipa[j] == pontosEquipa[i][j]) {
                    maxPontosEquipa[i][j] = pontosEquipa[i][j];
                }
            }

        }
        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes / 3; i++) {
                if (maxPontosEquipa[i][j] != 0) {
                    premiosEquipa[(i * 3)][j] = VALOR_EQUIPA;
                    premiosEquipa[((i * 3) + 1)][j] = VALOR_EQUIPA;
                    premiosEquipa[((i * 3) + 2)][j] = VALOR_EQUIPA;

                }
            }

        }
        return premiosEquipa;
    }

    public static int[][] premioAniversario(String[][] participantes, String dataEventoS, int nParticipantes) {
        String[] dataNasc = new String[MAX_PARTICIPANTES];
        int[][] premioAniv = new int[nParticipantes][N_JOGOS];
        for (int i = 0; i < nParticipantes; i++) {
            dataNasc[i] = participantes[i][2];
            String dataNascimento = converterddmmaaaaParaaaammdd(dataNasc[i]);
            if (verificarAniversario(dataEventoS, dataNascimento)) {
                int anos = idade(dataEventoS, dataNascimento);
                int premioAnos = calculoAniversario(anos);
                for (int j = 0; j < N_JOGOS; j++) {
                    premioAniv[i][j] = premioAnos;
                }
            }
        }
        return premioAniv;
    }

    public static int calculoAniversario(int anos) {
        int premioAniversario = 2 * anos;
        return premioAniversario;
    }

    public static int eliminarEquipa(String[][] participantes, int nParticipantes, String equipa) {
        int nErr = 0;
        for (int i = 0; i < nParticipantes; i = i + 3) {
            if (participantes[i][3].equalsIgnoreCase(equipa)) {
                for (int l = i; l < i + 3; l++) {
                    for (int c = 0; c < 4; c++) {
                        participantes[l][c] = participantes[l + 3][c];
                    }
                }
                nErr = nErr + 3;
            }
        }
        nParticipantes = nParticipantes - nErr;
        return nParticipantes;
    }

    public static void infPremiosEquipa(double premios[][], String EquipaPremios, String participantes[][], int nParticipantes, String jogos[], String pontosJogo, int pontos[][]) {
        int id = (idJogo(jogos, pontosJogo));
        String[] tempId = pontosJogo.split("_");
        String idJogo = tempId[tempId.length - 1];
        String ElementosEquipa[][] = new String[5][2];
        System.out.println("Para o id de jogo " + idJogo + ":");
        for (int i = 0; i < nParticipantes; i = i + 3) {
            if (participantes[i][3].equalsIgnoreCase(EquipaPremios));
            {
                for (int l = 0; l < 3; l++) {
                    System.out.println("O jogador " + participantes[i][i + l] + " da equipa " + EquipaPremios + " tem " + pontos[id][i + l] + ".");
                }
            }
        }
    }

          public static void VisualizarInformJogos(int[][] pontos, int nParticipantes, double[] mediasJogos, double[] percentagens, String [] idJogoColunas, double [][] premios, double []somasPremios) {
        calcularMediaJogo(pontos, nParticipantes, mediasJogos);
        calcularPercentagens(pontos, nParticipantes, percentagens);
        calcularSomaPremios(premios, nParticipantes, somasPremios);
        System.out.println("Qual o código do jogo que pretende visualizar a informação?");
        String idJogo = input.nextLine();
        for (int i = 0; i < N_JOGOS; i++) {
            if (idJogo.equals(idJogoColunas[i])) {
                System.out.println("Média dos pontos: " + mediasJogos[i]);
                System.out.println("Percentagem de quantos nao jogaram: " + percentagens[i]);
                System.out.println("Soma dos prémios: " + somasPremios[i]);
            }
        }
    }

    public static void calcularMediaJogo(int[][] pontos, int nParticipantes, double[] mediasJogos) { //CALCULA AS MEDIAS E GUARDA NO VETOR MEDIASJOGOS
        double[] somas = new double[N_JOGOS];
        double contJogadores = 0;
        double soma = 0;

        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes; i++) {
                if (pontos[i][j] != 0) {
                    soma = soma + pontos[i][j];
                    contJogadores = contJogadores + 1;

                }

            }
            somas[j] = soma;
            double media = somas[j] / contJogadores;
            mediasJogos[j] = media;
            contJogadores = 0;
            soma = 0;

        }
    }

    public static void calcularPercentagens(int[][] pontos, int nParticipantes, double[] percentagens) { //Calcula as percentagens e guarda num vetor 
        double contJogadoresTotal = nParticipantes;
        double contNaoJogaram = 0;

        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes; i++) {
                if (pontos[i][j] == 0) {
                    contNaoJogaram++;
                }

            }
            percentagens[j] = (contNaoJogaram / contJogadoresTotal) * 100;
            contNaoJogaram = 0;
        }

    }
    public static void calcularSomaPremios(double [][]premios, int nParticipantes, double [] somasPremios){
        
        double soma = 0;
        for (int j = 0; j < N_JOGOS; j++) {
            for (int i = 0; i < nParticipantes; i++) {
                soma = soma + premios[i][j];
                
            }
            somasPremios[j] = soma;
            soma = 0;
            }
    }
    public static void listarInformacoesEcra(int[][] pontos, int nParticipantes, double[][] premios) {

    }

    private static void ordenarValor(double[][] premios, String[] nom, String[] dat, int nParticipantes) {
        double[] premiosJogador = premiosTotalJogador(premios, nParticipantes);
        double temp1;
        int email=0, nome=1, equipa=3;
        
        int x;
        for (int i = 0; i < nParticipantes - 1; i++) {
            for (int j = i + 1; j < nParticipantes; j++) {
                if (premiosJogador[i] < premiosJogador[j]) {
                    temp1 = premiosJogador[i];
                    premiosJogador[i] = premiosJogador[j];
                    premiosJogador[j] = temp1;
                    temp = dat[i];
                    dat[i] = dat[j];
                    dat[j] = temp;
                    x = num[i];
                    num[i] = num[j];
                    num[j] = x;
                }
            }
        }
    }

    private static double[] premiosTotalJogador(double[][] premios, int nParticipantes) {
        double[] premiosJogador = new double[nParticipantes];
        emailPontos = participantes.split(";");
                    String em = emailPontos[0].trim();
        
        for (int i = 0; i < nParticipantes; i++) {
            for (int j = 0; j < N_JOGOS; j++) {
                premiosJogador[i] = premiosJogador[i] + premios[j][i];
            }
        }
        return premiosJogador;
    }
    
    private static String[] cortarCamposParticipantes(String[][]participantes) {
        String vec = new vec[N_PARTICIPANTES];
        for (int j=0; j<N_CAMPOS_INFO; j++) {
            if (j = posProcurada) {
                vec
            }
        }
        
    }

}
