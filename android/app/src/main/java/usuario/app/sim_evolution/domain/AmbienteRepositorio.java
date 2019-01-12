package usuario.app.sim_evolution.domain;

import android.content.Context;

import usuario.app.sim_evolution.model.dao.AmbienteDao;

/**
 * Created by Josué on 23/11/2016.
 */

public class AmbienteRepositorio {

    /*** MÉTODOS DE ACESSO A DADOS (DAO) ***/

    public static void setarAmbiente(Context context, int tipoAmbiente)  {
        AmbienteDao ambienteDAO = new AmbienteDao(context);
        ambienteDAO.setarAmbiente(tipoAmbiente);
    }

}
