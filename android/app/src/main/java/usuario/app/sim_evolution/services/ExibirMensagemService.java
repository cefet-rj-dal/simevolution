package usuario.app.sim_evolution.services;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Josué on 22/11/2016.
 */

public class ExibirMensagemService {

    public static void exibirAlertDialog(Context context, String titulo, String msg) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(msg)
                .setNeutralButton("OK", null)
                .create();

        dialog.show();
    }

}
