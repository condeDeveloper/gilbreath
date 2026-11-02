package br.com.conde.gilbreath;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Os numeros do README, presos. Todos sairam do {@link Medidor}. */
class MedidasTest {

    @Test
    @DisplayName("com dez mil primos, 9.999 linhas e nenhuma falha na primeira coluna")
    void dezMilPrimos() {
        Varredura.Resultado r = Varredura.conferirPrimos(Medidor.PRIMOS);
        assertThat(r.passou()).isTrue();
        assertThat(r.linhasConferidas()).isEqualTo(9_999);
        assertThat(r.primeiraFalha()).isEqualTo(-1);
    }

    @Test
    @DisplayName("a escala: 10, 100, 1.000 e 10.000 primos, todos sem falha")
    void escala() {
        List<int[]> linhas = Varredura.escala(4);
        assertThat(linhas).hasSize(4);
        assertThat(linhas.get(0)).containsExactly(10, 9, 1);
        assertThat(linhas.get(1)).containsExactly(100, 99, 1);
        assertThat(linhas.get(2)).containsExactly(1_000, 999, 1);
        assertThat(linhas.get(3)).containsExactly(10_000, 9_999, 1);
    }

    @Test
    @DisplayName("os dois caminhos concordam em 1.999 linhas")
    void juiz() {
        assertThat(Varredura.conferirOsDoisCaminhos(Medidor.PRIMOS_JUIZ)).isEqualTo(1_999);
    }

    @Test
    @DisplayName("a lenda do 0 e 2 e falsa: 1.308 entradas fora disso, e um 40 no caminho")
    void oTrianguloNaoEhSoZeroEDois() {
        int[] retrato = Varredura.retratoDasLinhas(Medidor.PRIMOS_JUIZ);
        assertThat(retrato[0]).isEqualTo(1_308);
        assertThat(retrato[1]).isEqualTo(2);
        assertThat(retrato[2]).isEqualTo(40);
    }

    @Test
    @DisplayName("o triangulo so assenta em 0 e 2 a partir da linha 35")
    void quandoAssenta() {
        assertThat(Varredura.primeiraLinhaSoComZeroEDois(Medidor.PRIMOS_JUIZ)).isEqualTo(35);
    }

    @Test
    @DisplayName("nenhuma outra sequencia aguenta o procedimento")
    void outrasSequenciasFalham() {
        List<Varredura.Resultado> todas = Varredura.outrasSequencias(200);
        assertThat(todas).hasSize(5);
        for (Varredura.Resultado r : todas) {
            assertThat(r.passou()).as("%s nao deveria passar", r.nome()).isFalse();
        }
    }

    @Test
    @DisplayName("tirar so o 2 quebra a propriedade ja na primeira linha")
    void semODoisQuebraNaHora() {
        Varredura.Resultado semODois = Varredura.outrasSequencias(200).stream()
                .filter(r -> r.nome().equals("primos sem o 2"))
                .findFirst()
                .orElseThrow();
        assertThat(semODois.passou()).isFalse();
        assertThat(semODois.primeiraFalha()).isEqualTo(1);
        assertThat(semODois.valorNaFalha()).isEqualTo(2);
    }

    @Test
    @DisplayName("o Fibonacci aguenta uma linha a mais que os outros, e cai na segunda")
    void fibonacciCaiNaSegunda() {
        Varredura.Resultado fib = Varredura.outrasSequencias(200).stream()
                .filter(r -> r.nome().equals("fibonacci"))
                .findFirst()
                .orElseThrow();
        assertThat(fib.primeiraFalha()).isEqualTo(2);
        assertThat(fib.valorNaFalha()).isZero();
    }
}
