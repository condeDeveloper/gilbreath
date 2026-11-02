# gilbreath

Escreva os primos numa linha. Embaixo, a diferença absoluta de cada par vizinho. Embaixo
dessa, o mesmo de novo. E de novo.

```
  2  3  5  7 11 13 17 19 23 29 31 37 41 43
    1  2  2  4  2  4  2  4  6  2  6  4  2
      1  0  2  2  2  2  2  2  4  4  2  2
        1  2  0  0  0  0  0  2  0  2  0
          1  2  0  0  0  0  2  2  2  2
            1  2  0  0  0  2  0  0  0
              1  2  0  0  2  2  0  0
                1  2  0  2  0  2  0
```

Olhe a primeira coluna. **1, 1, 1, 1, 1, 1, 1.** Norman Gilbreath reparou nisso em 1958,
rabiscando num guardanapo. Ninguém provou até hoje.

Este repositório constrói o triângulo de dois jeitos diferentes, confere a primeira coluna
linha a linha, e testa se a propriedade é dos primos ou do procedimento.

```bash
mvn -B -ntp verify                              # 14 testes
mvn -B -ntp exec:java -Dexec.args="triangulo"   # as primeiras linhas
mvn -B -ntp exec:java -Dexec.args="escala"      # 10, 100, 1.000, 10.000 primos
mvn -B -ntp exec:java -Dexec.args="outras"      # o mesmo em outras sequências
mvn -B -ntp exec:java -Dexec.args="juiz"        # os dois caminhos
mvn -B -ntp exec:java -Dexec.args="fundo"       # 200.000 primos (job próprio)
```

Java 21, sem dependência fora de JUnit e AssertJ.

## Os dois caminhos

O **econômico** guarda uma linha só, sobrescrevendo o vetor no lugar. É o que permite
chegar a duzentos mil primos sem estourar a memória.

O **completo** guarda o triângulo inteiro, sem sobrescrever nada. Gasta memória quadrática
e não serve para varrer muito — mas num algoritmo que escreve por cima de si mesmo, um
índice trocado some sem deixar rastro, e é exatamente contra isso que ele existe.

```
linhas conferidas uma a uma, com 2.000 primos: 1.999, todas iguais
```

## Até onde aguenta

```
primos     linhas     primeira coluna
10         9          só 1, sem falha
100        99         só 1, sem falha
1.000      999        só 1, sem falha
10.000     9.999      só 1, sem falha
200.000    199.999    só 1, sem falha
```

## É dos primos, não do procedimento

A pergunta óbvia é se qualquer sequência razoável faria isso. Não faz — e nem de perto.

```
sequência         termos   linhas até falhar   o que apareceu
ímpares           200      0                   2 na linha 1
quadrados         200      0                   3 na linha 1
Fibonacci         200      1                   0 na linha 2
triangulares      200      0                   2 na linha 1
primos sem o 2    200      0                   2 na linha 1
```

A última linha é a mais interessante do repositório inteiro. **Tirar um único número do
começo** — o 2, o primeiro primo — derruba a propriedade **na primeira linha**. A
sequência continua sendo os primos, continua tendo 199 dos 200 termos originais, e não
aguenta um passo.

O Fibonacci é o único que chega à segunda linha, e cai com um `0` onde deveria haver `1`.

## O que as medidas me corrigiram

**A lenda de que "depois da primeira linha só aparecem 0 e 2" é falsa.** Eu tinha escrito
isso no medidor como se fosse fato conhecido, apoiado no triângulo de catorze primos ali
em cima, onde de fato parece ser assim. Com 2.000 primos:

```
maior quantidade de entradas fora de 0 e 2 numa linha : 1.308  (na linha 2)
maior valor que aparece em qualquer linha             : 40
primeira linha que fica só com 0 e 2                  : 35
```

O triângulo leva **35 linhas para assentar**, e no caminho aparece um **40**. Olhando de
novo o exemplo pequeno, os `4` da terceira linha já estavam lá, à vista, e eu tinha
escrito a frase mesmo assim. O que vale linha a linha é a **primeira coluna**, e só ela.

**O exemplo pequeno engana.** Catorze primos mostram um triângulo que vira 0 e 2 quase
imediatamente, e a tentação de generalizar dali é enorme. A regra do repositório — a
tabela diz o que foi medido — existe exatamente para esse momento.

**Um milhão de primos custa oito minutos.** O triângulo é quadrático: dobrar os primos
quadruplica o trabalho. A varredura de fundo ficou em duzentos mil, que leva uns vinte
segundos e ainda confere duzentas mil linhas — e a tabela diz **200.000**, não "um
milhão", porque foi isso que rodou.

## Quatro achados

1. **199.999 linhas conferidas** com duzentos mil primos, e a primeira coluna é `1` em
   todas, sem uma única falha.
2. **Tirar só o 2 quebra tudo na primeira linha.** Os primos a partir do 3 — 199 dos 200
   termos — não aguentam um passo sequer.
3. **O triângulo não é só 0 e 2.** Com 2.000 primos, a linha 2 tem **1.308** entradas fora
   disso, aparece um **40** no caminho, e só a partir da **linha 35** o triângulo assenta.
4. Nenhuma das cinco outras sequências testadas — ímpares, quadrados, Fibonacci,
   triangulares, primos sem o 2 — passa da **segunda** linha. A propriedade não é do
   procedimento.
