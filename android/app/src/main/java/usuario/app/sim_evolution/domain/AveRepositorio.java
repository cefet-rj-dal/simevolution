package usuario.app.sim_evolution.domain;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import usuario.app.sim_evolution.model.entities.Ave;
import usuario.app.sim_evolution.model.entities.Bico;
import usuario.app.sim_evolution.model.entities.Cor;
import usuario.app.sim_evolution.model.entities.Fenotipo;
import usuario.app.sim_evolution.model.dao.AveDao;
import usuario.app.sim_evolution.enums.ConstantesValorAptidao;
import usuario.app.sim_evolution.enums.ConstantesGenotipos;
import usuario.app.sim_evolution.enums.ConstantesImagens;
import usuario.app.sim_evolution.services.CombinarGenotiposService;
import usuario.app.sim_evolution.services.DefinirQuantidadeFilhotesService;
import usuario.app.sim_evolution.services.ExibirVariacaoAveService;

/**
 * Created by Josué on 23/11/2016.
 * Updated by Josué on 14/02/2017
 */

public class AveRepositorio {

    /*** MÉTODOS DE ACESSO A DADOS (DAO) ***/

    // Método movido para o momento da criação do BD, uma vez que é somente preenchimento de tabela padrão
    /*public static void inserirAve(Context context) {
        AveDao aveDAO = new AveDao(context);
        aveDAO.inserirAve();
    }*/

    public static String atualizarAve(Context context,int tipoEvento, int tempoEvento, Ave ave){
        AveDao aveDAO = new AveDao(context);
        return aveDAO.atualizarAve(tipoEvento,tempoEvento,ave);
    }

    public static String decrementarEnergiaAves(Context context,int tempoEvento) {
        AveDao aveDAO = new AveDao(context);
        return aveDAO.decrementarEnergiaAves(tempoEvento);
    }

    public static void inserirEventoAve(Context context, int tempoEvento, Ave ave) {
        AveDao aveDAO = new AveDao(context);
        aveDAO.inserirEventoAve(tempoEvento,ave);
    }

    public static ArrayList<Ave> listaEspeciesAves(Context context, int tipoEvento, double valorAptidao) {
        AveDao aveDAO = new AveDao(context);
        return aveDAO.listaEspeciesAves(tipoEvento, valorAptidao);
    }

    public static LinkedHashMap<Integer, String> listaHistoricoQtdAvesSobreviventes(Context context, String tempo) {
        AveDao aveDAO = new AveDao(context);
        return aveDAO.listaHistoricoQtdAvesSobreviventes(tempo);
    }

    public static Integer qtdTotalAvesVivas(Context context) {
        AveDao aveDAO = new AveDao(context);
        return aveDAO.qtdTotalAvesVivas();
    }

    public static void deletaAveEvento(Context context) {
        AveDao aveDAO = new AveDao(context);
        aveDAO.deletaAveEvento();
    }

    /*** MÉTODOS DO NEGÓCIO ***/

    //Monta lista com todas as aves possíveis do simulador
    public static List<Ave> criarAves(){

        List<Ave> listaAves = new ArrayList<>();

        // **** CRIA VARIAÇÃO ESPÉCIE 1 (A)  - Verde/Bico Grande | 4422 (AABB) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        //    (1 -> cor, 1 -> verde, {4,4})
        Cor cor = new Cor(1,1,ConstantesGenotipos.GENOTIPO_COR_VERDE); // 1 (cor) | 1 (verde)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        //    (2 -> bico, 1 -> grande, {2,2})
        Bico bico = new Bico(2,1,ConstantesGenotipos.GENOTIPO_BICO_GRANDE); // 2 (bico) | 1 (grande)

        // Ave(int variacaoEspecie, Cor cor, Bico bico, int nivelEnergia)
        Ave ave1 = new Ave(1,cor,bico,100);  // variacaoEspecie 1 (A) (Verde/Bico Grande - AABB)
        ave1.setImageID(ConstantesImagens.IMAGENS_AVES[0]); // seta a imagem da ave como R.drawable.ave_4422 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 2 (B)  - Verde/Bico Pequeno | 4421 (AABb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        //    (1 -> cor, 1 -> verde, {4,4})
        cor = new Cor(1,1,ConstantesGenotipos.GENOTIPO_COR_VERDE); // 1 (cor) , 1 (verde)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        //    (2 -> bico, 2 -> pequeno, {2,1})
        bico = new Bico(2,2,ConstantesGenotipos.GENOTIPO_BICO_PEQUENO); // 2 (bico) , 2 (pequeno)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave2 = new Ave(2,cor,bico,100); // variacaoEspecie 2 (B) (Verde/Bico Médio - AABb))
        ave2.setImageID(ConstantesImagens.IMAGENS_AVES[1]); // seta a imagem da ave como R.drawable.ave_4421 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 3 (C)  - Verde/Bico Alicate | 4411 (AAbb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,1,ConstantesGenotipos.GENOTIPO_COR_VERDE); // 1 (cor) , 1 (verde)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,3,ConstantesGenotipos.GENOTIPO_BICO_ALICATE); // 2 (bico) , 3 (alicate)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave3 = new Ave(3,cor,bico,100); // variacaoEspecie 3 (C) (Verde/Bico Pequeno - AAbb))
        ave3.setImageID(ConstantesImagens.IMAGENS_AVES[2]); // seta a imagem da ave como R.drawable.ave_4411 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 4 (D)  - Mesclado/Bico Grande | 4322 (AaBB) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,2,ConstantesGenotipos.GENOTIPO_COR_MESCLADO); // 1 (cor) , 2 (mesclado)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,1,ConstantesGenotipos.GENOTIPO_BICO_GRANDE); // 2 (bico) , 1 (grande)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave4 = new Ave(4,cor,bico,100); // variacaoEspecie 4 (D) (Mesclado/Bico Grande - AaBB)
        ave4.setImageID(ConstantesImagens.IMAGENS_AVES[3]); // seta a imagem da ave como R.drawable.ave_4322 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 5 (E)  - Mesclado/Bico Pequeno | 4321 (AaBb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,2,ConstantesGenotipos.GENOTIPO_COR_MESCLADO); // 1 (cor) , 2 (mesclado)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,2,ConstantesGenotipos.GENOTIPO_BICO_PEQUENO); // 2 (bico) , 2 (pequeno)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave5 = new Ave(5,cor,bico,100); // variacaoEspecie 5 (E) (Mesclado/Bico Médio - AaBb)
        ave5.setImageID(ConstantesImagens.IMAGENS_AVES[4]); // seta a imagem da ave como R.drawable.ave_4321 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 6 (F)  - Mesclado/Bico Alicate | 4311 (Aabb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,2, ConstantesGenotipos.GENOTIPO_COR_MESCLADO); // 1 (cor) , 2 (mesclado)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,3,ConstantesGenotipos.GENOTIPO_BICO_ALICATE); // 2 (bico) , 3 (alicate)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave6 = new Ave(6,cor,bico,100); // variacaoEspecie 6 (F) (Mesclado/Bico Pequeno - Aabb)
        ave6.setImageID(ConstantesImagens.IMAGENS_AVES[5]); // seta a imagem da ave como R.drawable.ave_4311 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 7 (G)  - Amarelo/Bico Grande | 3322 (aaBB) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,3, ConstantesGenotipos.GENOTIPO_COR_AMARELO); // 1 (cor) , 3 (amarelo)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,1,ConstantesGenotipos.GENOTIPO_BICO_GRANDE); // 2 (bico) , 1 (grande)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave7 = new Ave(7,cor,bico,100); // variacaoEspecie 7 (G) (Amarelo/Bico Grande - aaBB)
        ave7.setImageID(ConstantesImagens.IMAGENS_AVES[6]); // seta a imagem da ave como R.drawable.ave_3322 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 8 (H)  - Amarelo/Bico Pequeno | 3321 (aaBb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,3, ConstantesGenotipos.GENOTIPO_COR_AMARELO); // 1 (cor) , 3 (amarelo)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,2,ConstantesGenotipos.GENOTIPO_BICO_PEQUENO); // 2 (bico) , 2 (pequeno)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave8 = new Ave(8,cor,bico,100); // variacaoEspecie 8 (H) (Amarelo/Bico Médio - aaBb)
        ave8.setImageID(ConstantesImagens.IMAGENS_AVES[7]); // seta a imagem da ave como R.drawable.ave_3321 (layout activity_aves.xml)


        // **** CRIA VARIAÇÃO ESPÉCIE 9 (I)  - Amarelo/Bico Alicate | 3311 (aabb) ****

        // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        cor = new Cor(1,3, ConstantesGenotipos.GENOTIPO_COR_AMARELO); // 1 (cor) , 3 (amarelo)

        // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
        bico = new Bico(2,3,ConstantesGenotipos.GENOTIPO_BICO_ALICATE); // 2 (bico) , 3 (alicate)

        // Ave(variacaoEspecie, Cor, Bico, nivelEnergia)
        Ave ave9 = new Ave(9,cor,bico,100); // variacaoEspecie 9 (I) (Amarelo/Bico Pequeno - aabb)
        ave9.setImageID(ConstantesImagens.IMAGENS_AVES[8]); // seta a imagem da ave como R.drawable.ave_3311 (layout activity_aves.xml)

        // Popula a lista com todas as aves
        listaAves.add(ave1);
        listaAves.add(ave2);
        listaAves.add(ave3);
        listaAves.add(ave4);
        listaAves.add(ave5);
        listaAves.add(ave6);
        listaAves.add(ave7);
        listaAves.add(ave8);
        listaAves.add(ave9);

        return listaAves;
    }

    public static String[] gerarFilhotes(Context context,int tempoEvento, int tipoAmbiente)     {

        String[] arrayRetorno = new String[3]; // {letra_parental_1, letra_parental_2, qtd_filhotes}

        Random geradorNumeroAleatorio = new Random();

        ArrayList<Fenotipo> listaGenotipoCorPossiveisFilhos;
        ArrayList<Fenotipo> listaGenotipoBicoPossiveisFilhos;
        ArrayList<Ave> listaCombinadaPossiveisFilhos = new ArrayList<>();

        // Lista todas as aves vivas
                                // listaEspeciesAves(Context context, int tipoEvento, double valorAptidao)
        ArrayList<Ave> ArrayAves = listaEspeciesAves(context, 0,0);

        // Trata aqui a escolha de duas aves do array para o cruzamento
        if (ArrayAves.size() >= 2) // A lista tem de possuir pelo menos duas aves
        {

            int indexAve1 = 0;
            int indexAve2 = 0;

            /** CRUZAMENTO DAS AVES
             *
             * Gera dois números inteiros aleatórios entre 0 (zero) e o tamanho do array de aves
             *
             * - Esses números definirão as 2 posições do array que contêm as aves a serem cruzadas.
             * (as variações de espécie com mais ocorrências no array têm naturalmente mais probabilidade de serem escolhidas)
             *
             */

            // Escolhe a posição da 1a. ave no array para o cruzamento
            indexAve1 = geradorNumeroAleatorio.nextInt(ArrayAves.size());

            // Escolhe a posição da 2a. ave para o cruzamento, garantindo que será diferente da 1a. ave
            do {
                indexAve2 = geradorNumeroAleatorio.nextInt(ArrayAves.size());
                if (indexAve2 != indexAve1)
                    break;
            }while (indexAve2 == indexAve1);

            //Combina os genótipos de cor dos pais, gerando todos genótipos de cor possíveis
            listaGenotipoCorPossiveisFilhos = CombinarGenotiposService.combinar((Cor)ArrayAves.get(indexAve1).getCor(), (Cor)ArrayAves.get(indexAve2).getCor());

            //Combina os genótipos de bico dos pais, gerando todos genótipos de bico possíveis
            listaGenotipoBicoPossiveisFilhos = CombinarGenotiposService.combinar((Bico)ArrayAves.get(indexAve1).getBico(), (Bico)ArrayAves.get(indexAve2).getBico());

            //Combina os arrays de genótipos de cor e bico dos filhos, gerando todos os filhotes possíveis
            for(int pCor = 0; pCor < listaGenotipoCorPossiveisFilhos.size(); pCor++)
            {
                for(int pBico = 0; pBico < listaGenotipoBicoPossiveisFilhos.size(); pBico++) {

                    int variacaoEspecieAve = 0;

                    if(((Cor)listaGenotipoCorPossiveisFilhos.get(pCor)).getGenotipo()[0] == 4) //cor verde ou mesclada (4_)
                    {
                        if(((Cor)listaGenotipoCorPossiveisFilhos.get(pCor)).getGenotipo()[1] == 4) //cor verde (44)
                        {
                            if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[0] == 2)  //bico grande ou médio (2_)
                            {
                                if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[1] == 2) //bico grande (22)
                                    variacaoEspecieAve = 1; //Verde/Bico Grande     - 4422  (AABB)
                                else //bico médio (21)
                                    variacaoEspecieAve = 2; //Verde/Bico Médio      - 4421  (AABb)
                            }
                            else //bico pequeno (11)
                                variacaoEspecieAve = 3; //Verde/Bico Pequeno    - 4411  (AAbb)

                        }
                        else //cor mesclada (43)
                        {
                            if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[0] == 2)  //bico grande ou médio (2_)
                            {
                                if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[1] == 2) //bico grande (22)
                                    variacaoEspecieAve = 4; //Mesclado/Bico Grande  - 4322  (AaBB)
                                else //bico médio (21)
                                    variacaoEspecieAve = 5; //Mesclado/Bico Médio  - 4321  (AaBb)

                            }
                            else //bico pequeno (11)
                                variacaoEspecieAve = 6; //Mesclado/Bico Pequeno    - 4311  (Aabb)

                        }
                    }
                    else //cor amarela (33)
                    {
                        if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[0] == 2)  //bico grande ou médio (2_)
                        {
                            if(((Bico)listaGenotipoBicoPossiveisFilhos.get(pBico)).getGenotipo()[1] == 2) //bico grande (22)
                                variacaoEspecieAve = 7; //Amarelo/Bico Grande   - 3322  (aaBB)
                            else //bico médio (21)
                                variacaoEspecieAve = 8; //Amarelo/Bico Médio  - 3321  (aaBb)

                        }
                        else //bico pequeno (11)
                            variacaoEspecieAve = 9; //Amarelo/Bico Pequeno    - 3311  (aabb)

                    }

                    if (variacaoEspecieAve != 0) {
                        // Ave(int variacaoEspecie, Cor cor, Bico bico, int nivelEnergia)
                        Ave filho = new Ave(variacaoEspecieAve,(Cor)(listaGenotipoCorPossiveisFilhos.get(pCor)),(Bico)(listaGenotipoBicoPossiveisFilhos.get(pBico)),100);
                        listaCombinadaPossiveisFilhos.add(filho);
                    }
                    else {
                        Toast.makeText(context,"Ops! Parece que houve um problema na definição da espécie de um dos filhos. Por favor, feche o aplicativo e tente novamente.",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            Ave aveParental1 = ArrayAves.get(indexAve1);
            Ave aveParental2 = ArrayAves.get(indexAve2);

            double aptidaoBicoParental1 = aveParental1.getValorAptidaoBico();
            double aptidaoBicoParental2 = aveParental2.getValorAptidaoBico();

            // Define a quantidade máxima de filhotes
            int qtdMaxFilhotes = 0;
            int qtdFilhotes = 0;
            qtdMaxFilhotes = DefinirQuantidadeFilhotesService.definir(aptidaoBicoParental1, aptidaoBicoParental2);

            // Tratamento para corrigir a fronteira do método geradorNumeroAleatorio.nextInt(n) abaixo
            // O método retorno um valor inteiro entre 0 (inclusive) and n (exclusive)
            if (qtdMaxFilhotes > 0)
            {
                qtdMaxFilhotes = (qtdMaxFilhotes + 1);

                // Define a quantidade de filhotes a ser gerada, sendo um número aleatório entre 0 e qtdMaxFilhotes
                qtdFilhotes = geradorNumeroAleatorio.nextInt(qtdMaxFilhotes);
            }

            //Array para armazenar a posição da última ave escolhida no array de possíveis filhos, de forma a evitar a seleção do mesmo filho mais de uma vez
            ArrayList<Integer> arrayUltimosFilhosEscolhidos = new ArrayList<>();
            int pAtualFilhoEscolhido = 0;

            if(qtdFilhotes > 0)
            {
                //Loop na lista de filhos possíveis com número de laços = qtdFilhotes
                for(int pFilho = 0; pFilho < qtdFilhotes; pFilho++) {

                    //Em cada salto, escolhe aleatoriamente um filhote na lista de filhos possíveis e adiciona-o no array de aves
                    //(Os filhos com mais ocorrências no array de filhos possíveis terão naturalmente mais chances de serem escolhidos)
                    //O laço abaixo garante que um mesmo filho não seja escolhido mais de uma vez
                    do {

                        //Escolhe um filho aleatoriamente no array de filhos possíveis
                        Ave novaAve = (Ave)(listaCombinadaPossiveisFilhos.get(geradorNumeroAleatorio.nextInt(listaCombinadaPossiveisFilhos.size())));

                        // Guarda a posição do filho escolhido
                        pAtualFilhoEscolhido = listaCombinadaPossiveisFilhos.indexOf((Ave)novaAve);

                        //Mantém o filho do 1o. laço e a partir dos laços seguintes, somente se o filho for diferente dos anteriormente selecionados
                        if(pFilho == 0 || (pFilho > 0 && !(arrayUltimosFilhosEscolhidos.contains(pAtualFilhoEscolhido)))) {

                            arrayUltimosFilhosEscolhidos.add(pAtualFilhoEscolhido);

                            /**
                             * Tratamento para inserção dos valores de aptidão do filhote, uma vez que os objetos de listaCombinadaPossiveisFilhos foram criados sem essas informações                         *
                             */

                            if(tipoAmbiente == 1) //cenário "Floresta": trazer os valores das constantes
                            {
                                if(novaAve.getCor().getTipo() == 1) // COR
                                {
                                    if(novaAve.getCor().getVariacao() == 1) //VERDE
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_BAIXA); // (BAIXA chance de ser predado)
                                    if(novaAve.getCor().getVariacao() == 2) //MESCLADO
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_MEDIA); // (MÉDIA chance de ser predado)
                                    if(novaAve.getCor().getVariacao() == 3) //AMARELO
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_ALTA); // (ALTA chance de ser predado)
                                }

                                if(novaAve.getBico().getTipo() == 2) // BICO
                                {
                                    if(novaAve.getBico().getVariacao() == 1) //GRANDE
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_BAIXA); // (BAIXA chance de alimentação/BAIXA reprodução)
                                    if(novaAve.getBico().getVariacao() == 2) //PEQUENO
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_MEDIA); // (MÉDIA chance de alimentação/MÉDIA reprodução)
                                    if(novaAve.getBico().getVariacao() == 3) //ALICATE
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_ALTA); // (ALTA chance de alimentação/ALTA reprodução)
                                }

                            }

                            if(tipoAmbiente == 2) //cenário "Savana": trazer os valores das constantes
                            {
                                if(novaAve.getCor().getTipo() == 1) // COR
                                {
                                    if(novaAve.getCor().getVariacao() == 1) //VERDE
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_ALTA); // (ALTA chance de ser predado)
                                    if(novaAve.getCor().getVariacao() == 2) //MESCLADO
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_MEDIA); // (MÉDIA chance de ser predado)
                                    if(novaAve.getCor().getVariacao() == 3) //AMARELO
                                        novaAve.setValorAptidaoCor(ConstantesValorAptidao.COR_CHANCE_BAIXA); // (BAIXA chance de ser predado)
                                }

                                if(novaAve.getBico().getTipo() == 2) // BICO
                                {
                                    if(novaAve.getBico().getVariacao() == 1) //GRANDE
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_ALTA); // (ALTA chance de alimentação/ALTA reprodução)
                                    if(novaAve.getBico().getVariacao() == 2) //PEQUENO
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_MEDIA); // (MÉDIA chance de alimentação/MÉDIA reprodução)
                                    if(novaAve.getBico().getVariacao() == 3) //ALICATE
                                        novaAve.setValorAptidaoBico(ConstantesValorAptidao.BICO_CHANCE_BAIXA); // (BAIXA chance de alimentação/BAIXA reprodução)
                                }

                            }

                            if(tipoAmbiente == 3) //cenário "Genérico": trazer os valores do BD
                            {
                                double cor_VERDE_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,1,1).getValorAptidao(); // 1 (cor) | 1 (verde)
                                double cor_MESCLADO_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,1,2).getValorAptidao(); // 1 (cor) | 2 (mesclado)
                                double cor_AMARELO_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,1,3).getValorAptidao(); // 1 (cor) | 3 (amarelo)

                                double bico_GRANDE_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,2,1).getValorAptidao(); // 2 (bico) | 1 (grande)
                                double bico_PEQUENO_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,2,2).getValorAptidao(); // 2 (bico) | 2 (pequeno)
                                double bico_ALICATE_ValorAptidaoCustomizado = FenotipoRepositorio.getFenotipoAptidao(context,2,3).getValorAptidao(); // 2 (bico) | 3 (alicate)


                                if(novaAve.getCor().getTipo() == 1) // COR
                                {
                                    if(novaAve.getCor().getVariacao() == 1) //VERDE
                                        novaAve.setValorAptidaoCor(cor_VERDE_ValorAptidaoCustomizado);
                                    if(novaAve.getCor().getVariacao() == 2) //MESCLADO
                                        novaAve.setValorAptidaoCor(cor_MESCLADO_ValorAptidaoCustomizado);
                                    if(novaAve.getCor().getVariacao() == 3) //AMARELO
                                        novaAve.setValorAptidaoCor(cor_AMARELO_ValorAptidaoCustomizado);
                                }

                                if(novaAve.getBico().getTipo() == 2) // BICO
                                {
                                    if(novaAve.getBico().getVariacao() == 1) //GRANDE
                                        novaAve.setValorAptidaoBico(bico_GRANDE_ValorAptidaoCustomizado);
                                    if(novaAve.getBico().getVariacao() == 2) //PEQUENO
                                        novaAve.setValorAptidaoBico(bico_PEQUENO_ValorAptidaoCustomizado);
                                    if(novaAve.getBico().getVariacao() == 3) //ALICATE
                                        novaAve.setValorAptidaoBico(bico_ALICATE_ValorAptidaoCustomizado);
                                }

                            }

                            /** Fim de tratamento para inserção dos valores de aptidão da ave **/

                            inserirEventoAve(context,tempoEvento,novaAve);

                            break;
                        }

                    } while (pFilho > 0 && arrayUltimosFilhosEscolhidos.contains(pAtualFilhoEscolhido));

                }
            }

            arrayRetorno[0] = ExibirVariacaoAveService.exibir(aveParental1.getVariacaoEspecie());
            arrayRetorno[1] = ExibirVariacaoAveService.exibir(aveParental2.getVariacaoEspecie());
            arrayRetorno[2] = String.valueOf(qtdFilhotes);

        }
        else {
            arrayRetorno[2] = "N"; // "N" indica que não há mais aves suficientes no ambiente
        }

        return arrayRetorno;
    }

    public static String predarAve(Context context, String tempo, ArrayList<Double> listaValoresAptidaoFenotiposCor) {

        String avePredada = "N"; // "N": valor default. Indica que não há aves disponíveis para predação.

        // Tratamento para garantir que haverá aves vivas para predação.
        // Este método, diferentemente dos métodos alimentarAve e gerarFilhotes, não lista, mas apenas conta as aves disponíveis antes de prosseguir
        int qtd_total_aves = AveRepositorio.qtdTotalAvesVivas(context);

        if(qtd_total_aves > 0) // Há aves vivas no ambiente
        {
            Random geradorNumeroAleatorio = new Random();

            double valorSelecionado = 0;

            /** DEFINIÇÃO DE PROBABILIDADES DE PREDAÇÃO **/

            //Definição aleatória do valor de probabilidade: um número entre 0.01 e 1.00
            do {
                // doubleValue = minimum + (maximum - minimum) * random.nextDouble()
                valorSelecionado =  1.00 * geradorNumeroAleatorio.nextDouble();

                BigDecimal bd = new BigDecimal(valorSelecionado).setScale(2, RoundingMode.HALF_EVEN);
                valorSelecionado = bd.doubleValue();

            } while (valorSelecionado == 0.00 || valorSelecionado > 1.00);

            ArrayList<Ave> ArrayAves;

            double aptidaoCorBaixa = listaValoresAptidaoFenotiposCor.get(0);
            double aptidaoCorMedia = listaValoresAptidaoFenotiposCor.get(1);
            double aptidaoCorAlta = listaValoresAptidaoFenotiposCor.get(2);

            /*** -- Verificação com valores diferentes --
             *
             * aptidaoCorBaixa = 0.11; // (BAIXA chance de ser predado)
             * aptidaoCorMedia = 0.26; // (MÉDIA chance de ser predado)
             * aptidaoCorAlta = 0.63; // (ALTA chance de ser predado)
             *
             * - SE (0.00 < valorSelecionado <= 0.11): busca aves com valorAptidaoCor == 0.11 (11% probabilidade)
             * - SENAO_SE (valorSelecionado <= 0.26): busca aves com valorAptidaoCor == 0.26 (15% probabilidade)
             * - SENAO busca aves com valorAptidaoCor == 0.63 (74% de probabilidade)
             *
             * -- Verificação com valores iguais --
             *
             * aptidaoCorBaixa = 0.33; // Chances iguais
             * aptidaoCorMedia = 0.33;
             * aptidaoCorAlta = (0.33 ou 0.34);
             *
             *
             * - busca aves com valorAptidaoCor <= 1.00 (100% probabilidade)
             *
             ***/

            // Tratamento para ocorrências do valor médio 0.33
            int qtdOcorrenciasValorMedio = 0;

            for(int i = 0; i < listaValoresAptidaoFenotiposCor.size(); i++) {
                if(listaValoresAptidaoFenotiposCor.get(i) == 0.33) {
                    qtdOcorrenciasValorMedio += 1;
                }
            }

            // Se houver duas ou mais ocorrências do valor médio, garantir que todas as aves sejam listadas
            if (qtdOcorrenciasValorMedio >= 2)
            {
                ArrayAves = listaEspeciesAves(context, 0, 0); // 0: lista todas as aves vivas
            }
            else
            {
                if (valorSelecionado <= aptidaoCorBaixa) // Faixa: 0.00 < valorSelecionado <= aptidaoCorBaixa
                {
                    ArrayAves = listaEspeciesAves(context, 1, aptidaoCorBaixa); // 1: lista aves vivas e com valorAptidaoCor = aptidaoCorBaixa
                }
                else
                {
                    if(valorSelecionado <= aptidaoCorMedia) // Faixa: aptidaoCorBaixa < valorSelecionado <= aptidaoCorMedia
                    {
                        ArrayAves = listaEspeciesAves(context, 1, aptidaoCorMedia); // 1: lista aves vivas e com valorAptidaoCor = aptidaoCorMedia
                    }
                    else // Faixa: aptidaoCorMedia < valorSelecionado <= 1.00
                    {
                        //if(aptidaoCorAlta != 0)
                        //{
                        ArrayAves = listaEspeciesAves(context, 1, aptidaoCorAlta); // 1: lista aves vivas e com valorAptidaoCor = aptidaoCorAlta
                        //}
                    }
                }
            }

            // Trata aqui a escolha da ave para predação
            if(ArrayAves.size() > 0) // Há aves com os critérios: predar uma!
            {
                // "Mata" a ave selecionada (seta nivelEnergia = 0)
                //atualizarAve(int tipoEvento, int tempoEvento, ArrayList<Double> listaValoresAptidaoFenotiposBico, Ave ave)
                String retornoAvePredada = atualizarAve(context, 0, Integer.parseInt(tempo), (Ave)ArrayAves.get(0));

                if(retornoAvePredada != null) // predação com êxito
                {
                    avePredada = ExibirVariacaoAveService.exibir(ArrayAves.get(0).getVariacaoEspecie());
                }

                // Decrementa o valor de nivelEnergia de todas as demais aves do ambiente
                String retornoDecrementoAves = decrementarEnergiaAves(context,Integer.parseInt(tempo));
            }
            else
                avePredada = "0"; // Não há aves com os critérios

        }

        return avePredada;
    }

    public static String alimentarAve(Context context,String tempo) {

        String avesAlimentadas = "N"; // "N" é o default. Indica que não há mais aves suficientes no ambiente
        String[] arrayRetorno = new String[3]; // {, , }

        // Lista todas as aves vivas (nivelEnergia > 0)
        ArrayList<Ave> ArrayAves = listaEspeciesAves(context, 0,0);

        if(ArrayAves.size() > 1) {

            // Incrementa a energia de todas as demais aves do ambiente
            for(int i = 0; i < ArrayAves.size(); i++) {
                               // atualizarAve(int tipoEvento, int tempoEvento, int tipoAmbiente, Ave ave)
                avesAlimentadas = atualizarAve(context, 2, Integer.parseInt(tempo), (Ave)ArrayAves.get(i));
            }
        }

        return avesAlimentadas;
    }

}
