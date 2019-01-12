package usuario.app.sim_evolution.domain;

import android.content.Context;

import java.util.ArrayList;

import usuario.app.sim_evolution.model.entities.Fenotipo;
import usuario.app.sim_evolution.model.dao.FenotipoDao;

/**
 * Created by Josué on 09/01/2017.
 */

public class FenotipoRepositorio {

    /*** MÉTODOS DE ACESSO A DADOS (DAO) ***/

    public static void inserirFenotipoAptidao(Context context, int tipoFenotipo, int variacaoFenotipo, double valor) {
        FenotipoDao fenotipoDAO = new FenotipoDao(context);
        fenotipoDAO.inserirFenotipoAptidao(tipoFenotipo,variacaoFenotipo,valor);
    }

    public static Fenotipo getFenotipoAptidao(Context context, int tipoFenotipo, int variacaoFenotipo) {
        FenotipoDao fenotipoDAO = new FenotipoDao(context);
        return fenotipoDAO.getFenotipoAptidao(tipoFenotipo,variacaoFenotipo);
    }

    public static ArrayList<Double> listaValorAptidaoFenotipos(Context context, int tipoFenotipo) {
        FenotipoDao fenotipoDAO = new FenotipoDao(context);
        return fenotipoDAO.listaValorAptidaoFenotipos(tipoFenotipo);
    }

    public static void setarAptidoesFenotiposAmbiente(Context context,int tipoAmbiente) {
        FenotipoDao fenotipoDAO = new FenotipoDao(context);
        fenotipoDAO.setarAptidoesFenotiposAmbiente(tipoAmbiente);
    }

}
