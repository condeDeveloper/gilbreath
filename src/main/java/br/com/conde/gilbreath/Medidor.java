package br.com.conde.gilbreath;

import java.util.List;

/**
 * Roda as tabelas do README.
 *
 * <pre>
 * mvn -B -ntp exec:java -Dexec.args="triangulo"
 * mvn -B -ntp exec:java -Dexec.args="escala"
 * mvn -B -ntp exec:java -Dexec.args="outras"
 * mvn -B -ntp exec:java -Dexec.args="juiz"
 * mvn -B -ntp exec:java -Dexec.args="fundo"     (um milhao de primos, job proprio)
 * </pre>
 */
public final class Medidor {

    public static final int PRIMOS = 10_000;
    public static final int PRIMOS_JUIZ = 2_000;
    public static final int PRIMOS_FUNDO = 200_000;

    public static void main(String[] args) {
        String comando = args.length > 0 ? args[0] : "tudo";
        switch (comando) {
            case "triangulo" -> triangulo();
            case "escala" -> escala();
            case "outras" -> outras();
            case "juiz" -> juiz();
            case "fundo" -> fundo();
            case "tudo" -> {
                triangulo();
                escala();
                outras();
                juiz();
            }
            default -> {
                System.out.println("comando desconhecido: " + comando);
                System.exit(2);
            }
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println(texto);
        System.out.println("-".repeat(texto.length()));
    }

    private static void triangulo() {
        titulo("As primeiras linhas do triangulo, com 14 primos");
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(14));
        for (int nivel = 0; nivel < 8; nivel++) {
            StringBuilder sb = new StringBuilder(" ".repeat(nivel * 2));
            for (int x : linhas[nivel]) {
                sb.append(String.format("%3d", x));
            }
            System.out.println(sb);
        }
        System.out.println();
        System.out.println("A primeira coluna, da segunda linha em diante, e so 1.");
        System.out.println();
        System.out.printf("%-10s %-14s %s%n", "linha", "entradas", "fora de 0 e 2");
        for (int nivel = 2; nivel < 8; nivel++) {
            System.out.printf("%-10d %-14d %d%n",
                    nivel, linhas[nivel].length, Triangulo.foraDeZeroEDois(linhas[nivel]));
        }
    }

    private static void escala() {
        titulo("Quantas linhas aguentam, por quantidade de primos");
        System.out.printf("%-14s %-16s %s%n", "primos", "linhas", "primeira coluna");
        for (int[] linha : Varredura.escala(4)) {
            System.out.printf("%-14d %-16d %s%n",
                    linha[0], linha[1], linha[2] == 1 ? "so 1, sem falha" : "FALHOU");
        }
    }

    private static void outras() {
        titulo("O mesmo procedimento em sequencias que nao sao os primos");
        System.out.printf("%-18s %-10s %-16s %s%n",
                "sequencia", "termos", "linhas ate falhar", "o que apareceu");
        for (Varredura.Resultado r : Varredura.outrasSequencias(200)) {
            if (r.passou()) {
                System.out.printf("%-18s %-10d %-16s %s%n",
                        r.nome(), r.termos(), "nenhuma falha", "so 1");
            } else {
                System.out.printf("%-18s %-10d %-16d %d na linha %d%n",
                        r.nome(), r.termos(), r.linhasConferidas(),
                        r.valorNaFalha(), r.primeiraFalha());
            }
        }
        System.out.println();
        System.out.println("A propriedade nao e do procedimento: e dos primos.");
    }

    private static void juiz() {
        titulo("Os dois caminhos, com " + PRIMOS_JUIZ + " primos");
        int linhas = Varredura.conferirOsDoisCaminhos(PRIMOS_JUIZ);
        System.out.printf("linhas conferidas, uma a uma: %d, todas iguais%n", linhas);
        int[] retrato = Varredura.retratoDasLinhas(PRIMOS_JUIZ);
        System.out.println();
        System.out.println("O que o triangulo REALMENTE tem dentro:");
        System.out.printf("   maior quantidade de entradas fora de 0 e 2 numa linha: %d (linha %d)%n",
                retrato[0], retrato[1]);
        System.out.printf("   maior valor que aparece em qualquer linha           : %d%n", retrato[2]);
        System.out.printf("   primeira linha que fica so com 0 e 2                : %d%n",
                Varredura.primeiraLinhaSoComZeroEDois(PRIMOS_JUIZ));
        System.out.println();
        System.out.println("Ou seja: a lenda de que \"depois da primeira linha so ha 0 e 2\" e falsa.");
        System.out.println("O que vale, linha a linha, e a PRIMEIRA COLUNA.");
    }

    private static void fundo() {
        titulo("Varredura com " + PRIMOS_FUNDO + " primos");
        long comeco = System.nanoTime();
        Varredura.Resultado r = Varredura.conferirPrimos(PRIMOS_FUNDO);
        System.out.printf("primos usados     : %d%n", r.termos());
        System.out.printf("linhas conferidas : %d%n", r.linhasConferidas());
        System.out.printf("primeira coluna   : %s%n",
                r.passou() ? "1 em todas, sem uma falha" : "FALHOU na linha " + r.primeiraFalha());
        System.out.printf("%ntempo: %.1f s%n", (System.nanoTime() - comeco) / 1e9);

        if (!r.passou() || r.linhasConferidas() != PRIMOS_FUNDO - 1) {
            System.out.println("MEDIDA MUDOU: a varredura nao fechou como o README diz");
            System.exit(1);
        }
        System.out.println("a medida do README bateu");
    }

    /** Usado pelos testes para listar as sequencias que falham. */
    public static List<Varredura.Resultado> outrasSequencias() {
        return Varredura.outrasSequencias(200);
    }
}
