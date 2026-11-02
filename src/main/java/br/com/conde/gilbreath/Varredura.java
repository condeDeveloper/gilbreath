package br.com.conde.gilbreath;

import java.util.ArrayList;
import java.util.List;

/**
 * Varre o triangulo linha a linha e confere a primeira coluna.
 *
 * <p>Tambem roda o mesmo teste sobre sequencias que NAO sao os primos, que e o
 * jeito de descobrir se a propriedade e dos primos ou do procedimento.
 */
public final class Varredura {

    /** O que uma sequencia faz quando entra no triangulo. */
    public record Resultado(
            String nome,
            int termos,
            int linhasConferidas,
            int primeiraFalha,
            int valorNaFalha) {

        public boolean passou() {
            return primeiraFalha < 0;
        }
    }

    private Varredura() {
    }

    /**
     * Confere que a primeira entrada de toda linha e 1.
     *
     * @return o resultado, com a linha da primeira falha ou -1 se nao houve
     */
    public static Resultado conferir(String nome, int[] base) {
        int[] primeiras = Triangulo.primeirasEntradas(base);
        for (int nivel = 0; nivel < primeiras.length; nivel++) {
            if (primeiras[nivel] != 1) {
                return new Resultado(nome, base.length, nivel, nivel + 1, primeiras[nivel]);
            }
        }
        return new Resultado(nome, base.length, primeiras.length, -1, 0);
    }

    public static Resultado conferirPrimos(int quantosPrimos) {
        return conferir("primos", Triangulo.primeirosPrimos(quantosPrimos));
    }

    /**
     * Confere o caminho economico contra o triangulo completo, que guarda tudo.
     *
     * @return quantas linhas foram conferidas
     */
    public static int conferirOsDoisCaminhos(int quantosPrimos) {
        int[] base = Triangulo.primeirosPrimos(quantosPrimos);
        int[] economico = Triangulo.primeirasEntradas(base);
        int[][] completo = Triangulo.trianguloCompleto(base);
        for (int nivel = 0; nivel < economico.length; nivel++) {
            if (economico[nivel] != completo[nivel + 1][0]) {
                throw new IllegalStateException("os dois caminhos discordam na linha " + (nivel + 1)
                        + ": economico " + economico[nivel] + ", completo " + completo[nivel + 1][0]);
            }
        }
        return economico.length;
    }

    /**
     * O retrato do que REALMENTE aparece no triangulo, por linha.
     *
     * <p>Corre por ai que, depois da primeira linha de diferencas, o triangulo
     * so tem 0 e 2. Nao tem: ja na segunda linha aparecem 4, e com milhares de
     * primos aparecem valores bem maiores. O que e verdade e a primeira coluna,
     * e so ela.
     *
     * @return {maior quantidade de entradas fora de 0 e 2 numa linha,
     *     linha onde isso aconteceu, maior valor visto no triangulo inteiro}
     */
    public static int[] retratoDasLinhas(int quantosPrimos) {
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(quantosPrimos));
        int maiorQuantidade = 0;
        int ondeFoi = -1;
        int maiorValor = 0;
        for (int nivel = 2; nivel < linhas.length; nivel++) {
            int fora = Triangulo.foraDeZeroEDois(linhas[nivel]);
            if (fora > maiorQuantidade) {
                maiorQuantidade = fora;
                ondeFoi = nivel;
            }
            for (int x : linhas[nivel]) {
                maiorValor = Math.max(maiorValor, x);
            }
        }
        return new int[] {maiorQuantidade, ondeFoi, maiorValor};
    }

    /** Em que linha o triangulo passa a ser SO 0 e 2 (fora o 1 da frente). */
    public static int primeiraLinhaSoComZeroEDois(int quantosPrimos) {
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(quantosPrimos));
        for (int nivel = 2; nivel < linhas.length; nivel++) {
            if (Triangulo.foraDeZeroEDois(linhas[nivel]) == 1) {
                return nivel;
            }
        }
        return -1;
    }

    /** Sequencias que nao sao os primos, para ver se a propriedade viaja. */
    public static List<Resultado> outrasSequencias(int termos) {
        List<Resultado> resultados = new ArrayList<>();

        int[] impares = new int[termos];
        for (int i = 0; i < termos; i++) {
            impares[i] = 2 * i + 1;
        }
        resultados.add(conferir("impares", impares));

        int[] quadrados = new int[termos];
        for (int i = 0; i < termos; i++) {
            quadrados[i] = (i + 1) * (i + 1);
        }
        resultados.add(conferir("quadrados", quadrados));

        int[] fibonacci = new int[termos];
        fibonacci[0] = 1;
        if (termos > 1) {
            fibonacci[1] = 2;
        }
        for (int i = 2; i < termos; i++) {
            fibonacci[i] = fibonacci[i - 1] + fibonacci[i - 2];
        }
        resultados.add(conferir("fibonacci", fibonacci));

        // os primos com o 2 trocado por 3: a sequencia quase igual que quebra
        int[] semODois = Triangulo.primeirosPrimos(termos + 1);
        int[] semODoisRecortado = new int[termos];
        System.arraycopy(semODois, 1, semODoisRecortado, 0, termos);
        resultados.add(conferir("primos sem o 2", semODoisRecortado));

        int[] triangulares = new int[termos];
        for (int i = 0; i < termos; i++) {
            triangulares[i] = (i + 1) * (i + 2) / 2;
        }
        resultados.add(conferir("triangulares", triangulares));

        return resultados;
    }

    /** O primeiro primo de cada potencia de dez, para a tabela de escala. */
    public static List<int[]> escala(int ateExpoente) {
        List<int[]> linhas = new ArrayList<>();
        for (int e = 1; e <= ateExpoente; e++) {
            int quantos = (int) Math.pow(10, e);
            Resultado r = conferirPrimos(quantos);
            linhas.add(new int[] {quantos, r.linhasConferidas(), r.passou() ? 1 : 0});
        }
        return linhas;
    }
}
