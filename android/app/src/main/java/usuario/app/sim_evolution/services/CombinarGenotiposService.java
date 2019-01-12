package usuario.app.sim_evolution.services;

import java.util.ArrayList;

import usuario.app.sim_evolution.model.entities.Bico;
import usuario.app.sim_evolution.model.entities.Cor;
import usuario.app.sim_evolution.model.entities.Fenotipo;

/**
 * Created by Josué on 11/01/2017.
 */

public class CombinarGenotiposService {

     //Combina os genótipos de duas aves
     public static ArrayList<Fenotipo> combinar(Fenotipo gametaAve1, Fenotipo gametaAve2) {

        ArrayList<Fenotipo> listaGenotiposPossiveisFilhos = new ArrayList<>();

        if(gametaAve1.getTipo() == 1) //é genótipo de cor
        {
            int[] genotipoCorCombinado = new int[2];
            int[] genotipoCorCombinado1 = new int[2];
            int[] genotipoCorCombinado2 = new int[2];
            int[] genotipoCorCombinado3 = new int[2];
            int[] genotipoCorCombinado4 = new int[2];

            int contador = 0;

            for(int i = 0; i < gametaAve1.getGenotipo().length; i++) {

                for(int j = 0; j < gametaAve2.getGenotipo().length; j++) {

                    //POG criada para resolver o problema de atualização de genotipoCorCombinado após o insert no array listaGenotiposPossiveisFilhos
                    // A reutilização de uma mesma variável em todos os saltos do loop provocava a alteração do conteúdo de genotipoBicoCombinado tb nos fenótipos já inseridos
                    // no array, fazendo com que todos os fenótipos fossem iguais ao do primeiro salto.
                    if(contador+1 == 1)
                    {
                        genotipoCorCombinado = genotipoCorCombinado1;
                    }

                    if (contador+1 == 2)
                    {
                        genotipoCorCombinado = genotipoCorCombinado2;
                    }

                    if (contador+1 == 3)
                    {
                        genotipoCorCombinado = genotipoCorCombinado3;
                    }

                    if (contador+1 == 4)
                    {
                        genotipoCorCombinado = genotipoCorCombinado4;
                    }

                    genotipoCorCombinado[0] = gametaAve1.getGenotipo()[i];
                    genotipoCorCombinado[1] = gametaAve2.getGenotipo()[j];

                    //ajuste da ordem dos genes
                    if(genotipoCorCombinado[0] == 3 && genotipoCorCombinado[1] == 4) {
                        genotipoCorCombinado[0] = 4;
                        genotipoCorCombinado[1] = 3;
                    }

                    /* - variacaoFenotipo: identifica as variações dos tipos de fenótipo
                    -> cor: 1 (verde), 2 (mesclado), 3 (amarelo) */
                    int variacaoFenotipoCor;

                    if(genotipoCorCombinado[0] == 4 && genotipoCorCombinado[1] == 4)
                        variacaoFenotipoCor = 1;
                    else {
                        if (genotipoCorCombinado[0] == 4 && genotipoCorCombinado[1] == 3)
                            variacaoFenotipoCor = 2;
                        else
                            variacaoFenotipoCor = 3;
                    }

                    listaGenotiposPossiveisFilhos.add(new Cor(1, variacaoFenotipoCor, genotipoCorCombinado,0.00));

                    contador++;
                }
            }
        }

        if(gametaAve1.getTipo() == 2) //bico
        {
            int[] genotipoBicoCombinado = new int[2];
            int[] genotipoBicoCombinado1 = new int[2];
            int[] genotipoBicoCombinado2 = new int[2];
            int[] genotipoBicoCombinado3 = new int[2];
            int[] genotipoBicoCombinado4 = new int[2];

            int contador = 0;

            for(int i = 0; i < gametaAve1.getGenotipo().length; i++) {

                for(int j = 0; j < gametaAve2.getGenotipo().length; j++) {

                    //POG criada para resolver o problema de atualização de genotipoBicoCombinado após o insert no array listaGenotiposPossiveisFilhos
                    // A reutilização de uma mesma variável em todos os saltos do loop provocava a alteração do conteúdo de genotipoBicoCombinado tb nos fenótipos já inseridos
                    // no array, fazendo com que todos os fenótipos fossem iguais ao do primeiro salto.
                    if(contador+1 == 1)
                    {
                        genotipoBicoCombinado = genotipoBicoCombinado1;
                    }

                    if (contador+1 == 2)
                    {
                        genotipoBicoCombinado = genotipoBicoCombinado2;
                    }

                    if (contador+1 == 3)
                    {
                        genotipoBicoCombinado = genotipoBicoCombinado3;
                    }

                    if (contador+1 == 4)
                    {
                        genotipoBicoCombinado = genotipoBicoCombinado4;
                    }

                    genotipoBicoCombinado[0] = gametaAve1.getGenotipo()[i];
                    genotipoBicoCombinado[1] = gametaAve2.getGenotipo()[j];

                    //ajuste da ordem dos genes
                    if(genotipoBicoCombinado[0] == 1 && genotipoBicoCombinado[1] == 2) {
                        genotipoBicoCombinado = new int[] {2,1};
                    }
                    /* - variacaoFenotipo: identifica as variações dos tipos de fenótipo
                    -> bico: 1 (grande), 2 (médio), 3 (pequeno)*/
                    int variacaoFenotipoBico;

                    if(genotipoBicoCombinado[0] == 2 && genotipoBicoCombinado[1] == 2)
                        variacaoFenotipoBico = 1;
                    else {
                        if (genotipoBicoCombinado[0] == 2 && genotipoBicoCombinado[1] == 1)
                            variacaoFenotipoBico = 2;
                        else
                            variacaoFenotipoBico = 3;
                    }

                    listaGenotiposPossiveisFilhos.add(new Bico(2, variacaoFenotipoBico, genotipoBicoCombinado,0.00));

                    contador++;
                }
            }
        }

        return listaGenotiposPossiveisFilhos;

    }
}
