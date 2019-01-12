package usuario.app.sim_evolution.services;

import usuario.app.sim_evolution.enums.ConstantesValorAptidao;

/**
 * Created by Josué on 26/01/2017.
 */

public class DefinirQuantidadeFilhotesService {

    // Definir a quantidade máxima de filhotes de duas aves determinadas
    public static int definir(double aptidaoBicoParental1, double aptidaoBicoParental2) {

        /** DEFINIÇÃO DA QUANTIDADE DE FILHOTES COM BASE NA APTIDÃO DO FENÓTIPO BICO **/

        int qtdMaxFilhotes = 0;

        /**
         * As aves geram até 6 filhotes
         *
         * | Faixas valor aptidão  | Qtd Máx. Filhotes  |
         * |        0.00           |         0          |   // BICO_LIMITE_FAIXA_REPRODUCAO_0
         * |    0.01 - 0.05        |         1          |   // BICO_LIMITE_FAIXA_REPRODUCAO_1
         * |    0.06 - 0.11        |         2          |   // BICO_LIMITE_FAIXA_REPRODUCAO_2
         * |    0.12 - 0.16        |         3          |   // BICO_LIMITE_FAIXA_REPRODUCAO_3
         * |    0.17 - 0.22        |         4          |   // BICO_LIMITE_FAIXA_REPRODUCAO_4
         * |    0.23 - 0.27        |         5          |   // BICO_LIMITE_FAIXA_REPRODUCAO_5
         * |    0.28 - 1.00        |         6          |   // BICO_LIMITE_FAIXA_REPRODUCAO_6
         **/

        double menorAptidaoParental = 0;

        // O menor valor de aptidão entre ambos os parentais será o utilizado para a definição da qtd máx. de filhos
        if(aptidaoBicoParental1 < aptidaoBicoParental2)
        {
            menorAptidaoParental = aptidaoBicoParental1;
        }
        else
        {
            menorAptidaoParental = aptidaoBicoParental2;
        }

        // Define-se a qtd máx. de filhos
        if (menorAptidaoParental == ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_0) qtdMaxFilhotes = 0;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_0 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_1) qtdMaxFilhotes =  1;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_1 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_2) qtdMaxFilhotes =  2;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_2 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_3) qtdMaxFilhotes =  3;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_3 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_4) qtdMaxFilhotes =  4;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_4 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_5) qtdMaxFilhotes =  5;

        if(menorAptidaoParental > ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_5 &&
                menorAptidaoParental <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_REPRODUCAO_6) qtdMaxFilhotes =  6;


        return qtdMaxFilhotes;

    }
}
