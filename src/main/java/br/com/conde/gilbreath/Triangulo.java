package br.com.conde.gilbreath;

/**
 * O triangulo de diferencas absolutas.
 *
 * <p>Escreva uma sequencia. Embaixo dela, a diferenca absoluta de cada par
 * vizinho. Embaixo dessa, o mesmo de novo. Com os primos:
 *
 * <pre>
 * 2  3  5  7 11 13 17 19 23 29
 *  1  2  2  4  2  4  2  4  6
 *   1  0  2  2  2  2  2  2
 *    1  2  0  0  0  0  0
 *     1  2  0  0  0  0
 *      1  2  0  0  0
 * </pre>
 *
 * <p>Norman Gilbreath reparou em 1958 que o primeiro numero de toda linha, da
 * segunda em diante, e <b>1</b>. Ninguem provou. Aqui o triangulo e construido
 * de dois jeitos diferentes e a primeira coluna e conferida linha a linha.
 */
public final class Triangulo {

    private Triangulo() {
    }

    /**
     * Constroi o triangulo guardando uma linha de cada vez, sobrescrevendo o
     * vetor. Gasta memoria de uma linha so, e e o caminho que a varredura usa.
     *
     * @return a primeira entrada de cada linha, da linha 1 (a primeira de
     *     diferencas) ate a ultima que couber
     */
    public static int[] primeirasEntradas(int[] base) {
        int[] linha = base.clone();
        int[] primeiras = new int[base.length - 1];
        for (int nivel = 0; nivel < primeiras.length; nivel++) {
            int tamanho = base.length - nivel - 1;
            for (int i = 0; i < tamanho; i++) {
                linha[i] = Math.abs(linha[i + 1] - linha[i]);
            }
            primeiras[nivel] = linha[0];
        }
        return primeiras;
    }

    /**
     * O juiz: constroi o triangulo INTEIRO, guardando todas as linhas, sem
     * sobrescrever nada. Gasta memoria quadratica e por isso nao serve para
     * varrer milhoes, mas nao tem como um indice errado passar despercebido.
     */
    public static int[][] trianguloCompleto(int[] base) {
        int[][] linhas = new int[base.length][];
        linhas[0] = base.clone();
        for (int nivel = 1; nivel < base.length; nivel++) {
            int tamanho = base.length - nivel;
            linhas[nivel] = new int[tamanho];
            for (int i = 0; i < tamanho; i++) {
                linhas[nivel][i] = Math.abs(linhas[nivel - 1][i + 1] - linhas[nivel - 1][i]);
            }
        }
        return linhas;
    }

    /**
     * Quantas entradas de uma linha nao sao 0 nem 2. Depois da primeira linha
     * de diferencas, a resposta observada e sempre 1 — so o proprio 1 da
     * frente.
     */
    public static int foraDeZeroEDois(int[] linha) {
        int quantos = 0;
        for (int x : linha) {
            if (x != 0 && x != 2) {
                quantos++;
            }
        }
        return quantos;
    }

    /** O crivo de Eratostenes: os n primeiros primos. */
    public static int[] primeirosPrimos(int quantos) {
        if (quantos < 1) {
            throw new IllegalArgumentException("precisa de pelo menos um primo: " + quantos);
        }
        int teto = quantos < 6 ? 15 : (int) (quantos * (Math.log(quantos) + Math.log(Math.log(quantos))) + 10);
        boolean[] composto = new boolean[teto + 1];
        int[] primos = new int[quantos];
        int achados = 0;
        for (int i = 2; i <= teto && achados < quantos; i++) {
            if (composto[i]) {
                continue;
            }
            primos[achados++] = i;
            for (long j = (long) i * i; j <= teto; j += i) {
                composto[(int) j] = true;
            }
        }
        if (achados < quantos) {
            throw new IllegalStateException("o crivo nao alcancou " + quantos + " primos");
        }
        return primos;
    }
}
