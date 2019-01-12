package usuario.app.sim_evolution.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Josué on 20/10/2016.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "DBSimEvolution.db";
    private static final int DB_VERSION = 1;

    public DataBase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_AVE = "CREATE TABLE tbAve (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "variacaEspecie INTEGER)";

        String CREATE_TABLE_AVE_EVENTO = "CREATE TABLE tbAveEvento (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "variacaEspecie INTEGER, " +
                "tempoEventoNascimento INTEGER, " +
                "tempoEventoMorte INTEGER, " +
                "nivelEnergia INTEGER, " +
                "valorAptidaoCor DECIMAL, " +
                "valorAptidaoBico DECIMAL)";

        String CREATE_TABLE_AMBIENTE = "CREATE TABLE tbAmbiente (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipoAmbiente INTEGER, " +
                "corAmbiente INTEGER)";

        String CREATE_TABLE_FENOTIPO = "CREATE TABLE tbFenotipo (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipoFenotipo INTEGER, " +
                "variacaoFenotipo INTEGER, " +
                "valorAptidao DECIMAL)";

        db.execSQL(CREATE_TABLE_AVE);
        db.execSQL(CREATE_TABLE_AVE_EVENTO);
        db.execSQL(CREATE_TABLE_AMBIENTE);
        db.execSQL(CREATE_TABLE_FENOTIPO);

        preencherTableAve(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tbAveEvento");
        db.execSQL("DROP TABLE IF EXISTS tbAve");
        db.execSQL("DROP TABLE IF EXISTS tbAmbiente");
        db.execSQL("DROP TABLE IF EXISTS tbFenotipo");

        onCreate(db);
    }

    // Insere o registro das espécies de aves (1 a 9)
    public void preencherTableAve(SQLiteDatabase db) {

        String INSERT_VALORES_TABLE_AVES = "";

        for(int ave = 1; ave < 10; ave++) {

            INSERT_VALORES_TABLE_AVES = "INSERT INTO tbAve(variacaEspecie) VALUES(" + ave + ")";

            db.execSQL(INSERT_VALORES_TABLE_AVES);

        }

    }

}
