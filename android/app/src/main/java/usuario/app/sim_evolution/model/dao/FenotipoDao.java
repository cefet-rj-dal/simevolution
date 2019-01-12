package usuario.app.sim_evolution.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import usuario.app.sim_evolution.model.entities.Bico;
import usuario.app.sim_evolution.model.entities.Cor;
import usuario.app.sim_evolution.model.entities.Fenotipo;
import usuario.app.sim_evolution.enums.ConstantesValorAptidao;
import usuario.app.sim_evolution.model.db.DataBase;
import usuario.app.sim_evolution.enums.ConstantesGenotipos;

/**
 * Created by Josué on 09/01/2017.
 */

public class FenotipoDao {

    private DataBase conn;

    public FenotipoDao(Context context) {

        conn = new DataBase(context);
    }

    // Insere o valor de aptidão de um determinado fenótipo
    public void inserirFenotipoAptidao(int tipoFenotipo, int variacaoFenotipo, double valorAptidao) {

        SQLiteDatabase db = conn.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();

            values.put("tipoFenotipo", tipoFenotipo);
            values.put("variacaoFenotipo", variacaoFenotipo);
            values.put("valorAptidao", valorAptidao);

            long idFenotipo = db.insert("tbFenotipo",null,values);

            int id = (int) idFenotipo;

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        // return (int) idAve;
    }

    // Retorna os dados de um determinado Fenotipo
    public Fenotipo getFenotipoAptidao(int tipoFenotipo, int variacaoFenotipo) {

        SQLiteDatabase db = conn.getReadableDatabase();

        Fenotipo fenotipo = null;
        double aptidao = 0.00;

        String selectQuery = "SELECT " +
                "_id, " +
                "tipoFenotipo, " +
                "variacaoFenotipo, " +
                "valorAptidao " +
                "FROM tbFenotipo " +
                "WHERE tipoFenotipo =  " + tipoFenotipo + " AND variacaoFenotipo =  " + variacaoFenotipo + " " +
                "ORDER BY _id DESC " +
                "LIMIT 1";

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);

            try{

                if(cursor != null && cursor.getCount() > 0) {

                    cursor.moveToPosition(-1);

                    while(cursor.moveToNext()) {

                        aptidao = cursor.getDouble(cursor.getColumnIndex("valorAptidao"));

                    }
                }

                //Tratamento para definição do genotipo
                if(tipoFenotipo == 1) // Cor
                {
                    if(variacaoFenotipo == 1) // 1: verde
                        fenotipo = new Cor(tipoFenotipo,variacaoFenotipo, ConstantesGenotipos.GENOTIPO_COR_VERDE,aptidao);
                    if(variacaoFenotipo == 2) // 2: mesclado
                        fenotipo = new Cor(tipoFenotipo,variacaoFenotipo,ConstantesGenotipos.GENOTIPO_COR_MESCLADO,aptidao);
                    if(variacaoFenotipo == 3) // 3: amarelo
                        fenotipo = new Cor(tipoFenotipo,variacaoFenotipo,ConstantesGenotipos.GENOTIPO_COR_AMARELO,aptidao);
                }

                if(tipoFenotipo == 2) // Bico
                {
                    if(variacaoFenotipo == 1) // 1: grande
                        fenotipo = new Bico(tipoFenotipo,variacaoFenotipo,ConstantesGenotipos.GENOTIPO_BICO_GRANDE,aptidao);
                    if(variacaoFenotipo == 2) // 2: pequeno
                        fenotipo = new Bico(tipoFenotipo,variacaoFenotipo,ConstantesGenotipos.GENOTIPO_BICO_PEQUENO,aptidao);
                    if(variacaoFenotipo == 3) // 3: alicate
                        fenotipo = new Bico(tipoFenotipo,variacaoFenotipo,ConstantesGenotipos.GENOTIPO_BICO_ALICATE,aptidao);
                }

            }
            finally {
                cursor.close();
            }

        }
        finally {
            db.close();
        }

        return fenotipo;
    }

    // Listagem de valores de aptidao dos fenotipos (1 valor para cada variação de fenótipo, logo a lista deve conter só 3 itens)
    public ArrayList<Double> listaValorAptidaoFenotipos(int tipoFenotipo) {

        SQLiteDatabase db = conn.getReadableDatabase();

        ArrayList<Double> listaChances = new ArrayList<>();
        double aptidao = 0.00;
        String selectQuery = "";

        try {

            // Tratamento de inconsistências: força a listagem dos últimos três registros inseridos na tabela
            selectQuery =   "SELECT valorAptidao FROM tbFenotipo WHERE _id IN ( " +
                                    "SELECT _id FROM tbFenotipo WHERE tipoFenotipo = " + tipoFenotipo + " ORDER BY _id DESC LIMIT 3 " +
                                ") ORDER BY valorAptidao ASC";

            Cursor cursor = db.rawQuery(selectQuery,null);

            try {

                if(cursor != null && cursor.getCount() > 0) {

                    cursor.moveToPosition(-1);

                    while(cursor.moveToNext()) {

                        aptidao = cursor.getDouble(cursor.getColumnIndex("valorAptidao"));

                        listaChances.add(aptidao);
                    }
                }

            }finally {
                cursor.close();
            }

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        //Tratamento de inconsistências: apesar de os elementos terem sido inseridos em ordem crescente, ordena a lista para garantir
        Collections.sort(listaChances);

        return listaChances;
    }

    // Registra no BD os valores de aptidão dos fenótipos para cada tipo de ambiente
    public void setarAptidoesFenotiposAmbiente(int tipoAmbiente) {

        if(tipoAmbiente == 1) //cenário "Floresta"
        {
            // 1 (cor) | 1 (verde)     | 0.11 (BAIXA chance de ser predado)
            inserirFenotipoAptidao(1,1, ConstantesValorAptidao.COR_CHANCE_BAIXA);

            // 1 (cor) | 2 (mesclado)  | 0.26 (MÉDIA chance de ser predado)
            inserirFenotipoAptidao(1,2, ConstantesValorAptidao.COR_CHANCE_MEDIA);

            // 1 (cor) | 3 (amarelo)   | 0.63 (ALTA chance de ser predado)
            inserirFenotipoAptidao(1,3, ConstantesValorAptidao.COR_CHANCE_ALTA);

            // 2 (bico) | 1 (grande)   | 0.11 (BAIXA chance de alimentação/BAIXA reprodução)
            inserirFenotipoAptidao(2,1, ConstantesValorAptidao.BICO_CHANCE_BAIXA);

            // 2 (bico) | 2 (pequeno)  | 0.26 (MÉDIA chance de alimentação/MÉDIA reprodução)
            inserirFenotipoAptidao(2,2, ConstantesValorAptidao.BICO_CHANCE_MEDIA);

            // 2 (bico) | 3 (alicate)  | 0.63 (ALTA chance de alimentação/ALTA reprodução)
            inserirFenotipoAptidao(2,3, ConstantesValorAptidao.BICO_CHANCE_ALTA);
        }
        if(tipoAmbiente == 2) //cenário "Savana"
        {
            // 1 (cor) | 1 (verde)     | 0.63 (ALTA chance de ser predado)
            inserirFenotipoAptidao(1,1, ConstantesValorAptidao.COR_CHANCE_ALTA);

            // 1 (cor) | 2 (mesclado)  | 0.26 (MÉDIA chance de ser predado)
            inserirFenotipoAptidao(1,2, ConstantesValorAptidao.COR_CHANCE_MEDIA);

            // 1 (cor) | 3 (amarelo)   | 0.11 (BAIXA chance de ser predado)
            inserirFenotipoAptidao(1,3, ConstantesValorAptidao.COR_CHANCE_BAIXA);

            // 2 (bico) | 1 (grande)   | 0.63 (ALTA chance de alimentação/ALTA reprodução)
            inserirFenotipoAptidao(2,1, ConstantesValorAptidao.BICO_CHANCE_ALTA);

            // 2 (bico) | 2 (pequeno)  | 0.26 (MÉDIA chance de alimentação/MÉDIA reprodução)
            inserirFenotipoAptidao(2,2, ConstantesValorAptidao.BICO_CHANCE_MEDIA);

            // 2 (bico) | 3 (alicate)  | 0.11 (BAIXA chance de alimentação/BAIXA reprodução)
            inserirFenotipoAptidao(2,3, ConstantesValorAptidao.BICO_CHANCE_BAIXA);
        }
        if(tipoAmbiente == 3) //cenário "Genérico"
        {
            //Inserts já realizados em AptidaoActivity
        }

    }

}
