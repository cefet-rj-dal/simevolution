package usuario.app.sim_evolution.model.entities;

/**
 * Created by Josué on 08/09/2016.
 */
public class Cor extends Fenotipo {

    public Cor(int tipoFenotipo,int variacaoFenotipo, int[] genotipo, double valorAptidao){
        super.setTipo(tipoFenotipo);
        super.setVariacao(variacaoFenotipo);
        super.setGenotipo(genotipo);
        super.setValorAptidao(valorAptidao);
    }

    public Cor(int tipoFenotipo,int variacaoFenotipo, int[] genotipo){
        super.setTipo(tipoFenotipo);
        super.setVariacao(variacaoFenotipo);
        super.setGenotipo(genotipo);
    }


}
