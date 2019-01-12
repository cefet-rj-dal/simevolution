package usuario.app.sim_evolution.services;

/**
 * Created by Josué on 05/01/2017.
 */

public class ExibirVariacaoAveService {

    public static String exibir(int variacaoEspecie) {

        String variacao = null;

        switch (variacaoEspecie) {
            case 1:
                variacao = "A";
                break;
            case 2:
                variacao = "B";
                break;
            case 3:
                variacao = "C";
                break;
            case 4:
                variacao = "D";
                break;
            case 5:
                variacao = "E";
                break;
            case 6:
                variacao = "F";
                break;
            case 7:
                variacao = "G";
                break;
            case 8:
                variacao = "H";
                break;
            case 9:
                variacao = "I";
                break;
        }

        return variacao;
    }
}
