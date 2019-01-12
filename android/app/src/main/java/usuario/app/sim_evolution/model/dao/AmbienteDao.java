package usuario.app.sim_evolution.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import usuario.app.sim_evolution.model.entities.Ambiente;
import usuario.app.sim_evolution.model.db.DataBase;


/**
 * Created by Josué on 23/11/2016.
 */

public class AmbienteDao {

    private DataBase conn;

    public AmbienteDao(Context context) {
        conn = new DataBase(context);
    }

    // Insere o registro do ambiente selecionado pelo usuário
    public void inserirAmbiente(int tipoAmbiente, int corAmbiente) {

        SQLiteDatabase db = conn.getWritableDatabase();

        try{

            ContentValues values = new ContentValues();

            values.put("tipoAmbiente", tipoAmbiente);
            values.put("corAmbiente", corAmbiente);

            long ambienteID = db.insert("tbAmbiente",null,values);

            int id = (int) ambienteID;

        }finally {
            db.close();
        }

        //return (int) ambienteID;

    }

    // Retorna os dados do ambiente ativo na simulação
    public Ambiente getAmbiente() {

        SQLiteDatabase db = conn.getReadableDatabase();

        Ambiente ambiente = null;
        int tipoAmbiente = 0;
        int corAmbiente = 0;

        String selectQuery = "SELECT " +
                "_id, " +
                "tipoAmbiente, " +
                "corAmbiente " +
                "FROM tbAmbiente " +
                "ORDER BY _id DESC " +
                "LIMIT 1";

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);

            try{

                if(cursor != null && cursor.getCount() > 0) {

                    cursor.moveToPosition(-1);

                    while(cursor.moveToNext()) {

                        tipoAmbiente = cursor.getInt(cursor.getColumnIndex("tipoAmbiente"));
                        corAmbiente = cursor.getInt(cursor.getColumnIndex("corAmbiente"));

                    }
                }

                ambiente = new Ambiente(tipoAmbiente,corAmbiente);

            }
            finally {
                cursor.close();
            }

        }
        finally {
            db.close();
        }

        return ambiente;
    }

    public void setarAmbiente(int tipoAmbiente) {

        if(tipoAmbiente == 1) //cenário "Floresta"
        {
            inserirAmbiente(1,1); // 1 (tipoAmbiente - Floresta) | 1 (corAmbiente - verde)
        }

        if(tipoAmbiente == 2) //cenário "Savana"
        {
            inserirAmbiente(2,3); // 2 (tipoAmbiente - Savana) | 3 (corAmbiente - amarelo)
        }

        if(tipoAmbiente == 3) //cenário "Genérico"
        {
            inserirAmbiente(3,2); // 3 (tipoAmbiente - Genérico) | 2 (corAmbiente - indefinida)
        }

    }

}
