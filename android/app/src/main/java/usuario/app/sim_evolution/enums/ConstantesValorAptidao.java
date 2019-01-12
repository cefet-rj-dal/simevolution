package usuario.app.sim_evolution.enums;

/**
 * Created by Josué on 10/01/2017.
 * Updated by Josué on 14/02/2017
 */

public class ConstantesValorAptidao {

    /** VALORES DE APTIDÃO PADRÃO **/

    public static final double COR_CHANCE_BAIXA = 0.07; // (BAIXA chance de ser predado)
    public static final double COR_CHANCE_MEDIA = 0.20; // (MÉDIA chance de ser predado)
    public static final double COR_CHANCE_ALTA = 0.73; // (ALTA chance de ser predado)

    public static final double BICO_CHANCE_BAIXA = 0.07; // (BAIXA chance de alimentação/BAIXA reprodução)
    public static final double BICO_CHANCE_MEDIA = 0.20; // (MÉDIA chance de alimentação/MÉDIA reprodução)
    public static final double BICO_CHANCE_ALTA = 0.73; // (ALTA chance de alimentação/ALTA reprodução)

    /** APTIDÃO BICO - FAIXAS INCREMENTO ENERGIA (ALIMENTAÇÃO)
         * Aqui levou-se como referência o valor de 0.33 como sendo de equilíbrio entre os 3 fenótipos.
         * Assim, aplicou-se regra de três com base na relação (0,33 -> 5), onde 5 é o incremento máximo de energia
     *
     * As aves ganham até 5 pontos de energia
     *
     * | Faixas valor aptidão  | Pontos Energia |
     * |        0.00           |         0      |
     * |    0.01 - 0.07        |        +1      |
     * |    0.08 - 0.13        |        +2      |
     * |    0.14 - 0.20        |        +3      |
     * |    0.21 - 0.26        |        +4      |
     * |    0.27 - 1.00        |        +5      |
     **/

    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_0 = 0.00;
    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_1 = 0.07;
    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_2 = 0.13;
    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_3 = 0.20;
    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_4 = 0.26;
    public static final double BICO_LIMITE_FAIXA_ALIMENTACAO_5 = 1.00;

    /** APTIDÃO BICO - FAIXAS QUANTIDADE DE FILHOTES (REPRODUÇÃO)
     * Aqui levou-se como referência o valor de 0.33 como sendo de equilíbrio entre os 3 fenótipos.
     * Assim, aplicou-se regra de três com base na relação (0,33 -> 6), onde 6 é a quantidade máxima de filhotes
     *
     * As aves geram até 6 filhotes
     *
     * | Faixas valor aptidão  | Qtd Máx. Filhotes  |
     * |        0.00           |         0          |
     * |    0.01 - 0.06        |         1          |
     * |    0.07 - 0.11        |         2          |
     * |    0.12 - 0.17        |         3          |
     * |    0.18 - 0.22        |         4          |
     * |    0.23 - 0.28        |         5          |
     * |    0.29 - 1.00        |         6          |
     **/

    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_0 = 0.00;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_1 = 0.06;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_2 = 0.11;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_3 = 0.17;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_4 = 0.22;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_5 = 0.28;
    public static final double BICO_LIMITE_FAIXA_REPRODUCAO_6 = 1.00;

}
