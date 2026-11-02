package br.com.conde.gilbreath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrianguloTest {

    @Test
    @DisplayName("o triangulo dos dez primeiros primos, linha por linha")
    void trianguloConhecido() {
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(10));
        assertThat(linhas[0]).containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        assertThat(linhas[1]).containsExactly(1, 2, 2, 4, 2, 4, 2, 4, 6);
        assertThat(linhas[2]).containsExactly(1, 0, 2, 2, 2, 2, 2, 2);
        assertThat(linhas[3]).containsExactly(1, 2, 0, 0, 0, 0, 0);
        assertThat(linhas[4]).containsExactly(1, 2, 0, 0, 0, 0);
    }

    @Test
    @DisplayName("cada linha tem uma entrada a menos que a de cima")
    void linhasEncolhem() {
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(30));
        for (int nivel = 0; nivel < linhas.length; nivel++) {
            assertThat(linhas[nivel]).as("linha %d", nivel).hasSize(30 - nivel);
        }
    }

    @Test
    @DisplayName("toda entrada do triangulo e maior ou igual a zero")
    void diferencaAbsolutaNuncaNegativa() {
        int[][] linhas = Triangulo.trianguloCompleto(Triangulo.primeirosPrimos(200));
        for (int[] linha : linhas) {
            for (int x : linha) {
                assertThat(x).isGreaterThanOrEqualTo(0);
            }
        }
    }

    @Test
    @DisplayName("o caminho economico da as mesmas primeiras entradas que o completo")
    void osDoisCaminhosConcordam() {
        int[] base = Triangulo.primeirosPrimos(300);
        int[] economico = Triangulo.primeirasEntradas(base);
        int[][] completo = Triangulo.trianguloCompleto(base);
        assertThat(economico).hasSize(299);
        for (int nivel = 0; nivel < economico.length; nivel++) {
            assertThat(economico[nivel]).as("linha %d", nivel + 1).isEqualTo(completo[nivel + 1][0]);
        }
    }

    @Test
    @DisplayName("o crivo devolve os primos certos, na ordem")
    void crivo() {
        assertThat(Triangulo.primeirosPrimos(10))
                .containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        assertThat(Triangulo.primeirosPrimos(1)).containsExactly(2);
        int[] mil = Triangulo.primeirosPrimos(1000);
        assertThat(mil).hasSize(1000);
        assertThat(mil[999]).isEqualTo(7919);
        assertThatThrownBy(() -> Triangulo.primeirosPrimos(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a contagem de entradas fora de 0 e 2 conta o que deve")
    void foraDeZeroEDois() {
        assertThat(Triangulo.foraDeZeroEDois(new int[] {0, 2, 0, 2})).isZero();
        assertThat(Triangulo.foraDeZeroEDois(new int[] {1, 2, 0, 4})).isEqualTo(2);
        assertThat(Triangulo.foraDeZeroEDois(new int[] {})).isZero();
    }
}
