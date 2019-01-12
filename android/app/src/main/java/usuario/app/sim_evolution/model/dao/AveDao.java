package usuario.app.sim_evolution.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import usuario.app.sim_evolution.enums.ConstantesValorAptidao;
import usuario.app.sim_evolution.model.entities.Ave;
import usuario.app.sim_evolution.model.entities.Bico;
import usuario.app.sim_evolution.model.entities.Cor;
import usuario.app.sim_evolution.model.db.DataBase;
import usuario.app.sim_evolution.enums.ConstantesGenotipos;

/**
 * Created by Josué on 23/11/2016.
 */

public class AveDao {

    private DataBase conn;

    public AveDao(Context context) {

        conn = new DataBase(context);
    }

    // Insere o registro de nascimento ou morte de uma determinada ave
    public void inserirEventoAve(int tempoEvento, Ave ave) {

        SQLiteDatabase db = conn.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();

            values.put("variacaEspecie", ave.getVariacaoEspecie());
            values.put("tempoEventoNascimento", tempoEvento);
            values.put("tempoEventoMorte", 0);
            values.put("nivelEnergia", ave.getNivelEnergia());
            values.put("valorAptidaoCor", ave.getValorAptidaoCor());
            values.put("valorAptidaoBico", ave.getValorAptidaoBico());

            long idAve = db.insert("tbAveEvento",null,values);

            int id = (int) idAve;

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        // return (int) idAve;
    }

    // Atualiza o registro de uma determinada ave
    public String atualizarAve(int tipoEvento, int tempoEvento, Ave ave){

        /**
         * tipoEvento == 0: evento de predação (seta nivelEnergia = 0)
         * tipoEvento == 2: evento de alimentação (incrementa nivelEnergia)
         */

        SQLiteDatabase db = conn.getWritableDatabase();

        String idAve = null;

        try {

            ContentValues values = new ContentValues();

            int novoNivelEnergia = 0;

            if (tipoEvento == 2) // evento alimentação (incrementa nivelEnergia)
            {
                /**
                 * As aves ganham até 5 pontos de energia
                 *
                 * | Faixas valor aptidão  | Pontos Energia |
                 * |        0.00           |         0      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_0
                 * |    0.01 - 0.06        |        +1      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_1
                 * |    0.07 - 0.13        |        +2      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_2
                 * |    0.14 - 0.19        |        +3      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_3
                 * |    0.20 - 0.26        |        +4      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_4
                 * |    0.27 - 1.00        |        +5      |   // BICO_LIMITE_FAIXA_ALIMENTACAO_5
                 **/

                int nivelEnergiaAtual = ave.getNivelEnergia();
                double aptidaoBicoAve = ave.getValorAptidaoBico();

                if(aptidaoBicoAve == ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_0) novoNivelEnergia = nivelEnergiaAtual;
                if(aptidaoBicoAve > ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_0 && aptidaoBicoAve <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_1) novoNivelEnergia = (nivelEnergiaAtual + 1);
                if(aptidaoBicoAve > ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_1 && aptidaoBicoAve <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_2) novoNivelEnergia = (nivelEnergiaAtual + 2);
                if(aptidaoBicoAve > ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_2 && aptidaoBicoAve <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_3) novoNivelEnergia = (nivelEnergiaAtual + 3);
                if(aptidaoBicoAve > ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_3 && aptidaoBicoAve <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_4) novoNivelEnergia = (nivelEnergiaAtual + 4);
                if(aptidaoBicoAve > ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_4 && aptidaoBicoAve <= ConstantesValorAptidao.BICO_LIMITE_FAIXA_ALIMENTACAO_5) novoNivelEnergia = (nivelEnergiaAtual + 5);
            }

            // Tratamento de inconsistências
            if(novoNivelEnergia > 100) novoNivelEnergia = 100;
            if(novoNivelEnergia < 0) novoNivelEnergia = 0;

            // Se for evento de predação, setar o tempo da morte da ave
            if (novoNivelEnergia == 0) { //(tipoEvento == 0) // Evento de predação
                values.put("tempoEventoMorte", tempoEvento);
            }

            values.put("nivelEnergia", novoNivelEnergia);
            db.update("tbAveEvento", values, "_id = ?", new String[]{String.valueOf(ave.getId_ave())});

            idAve = String.valueOf(ave.getId_ave());

        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }

            return idAve;
        }

    }

    // Decrementa energia de todas as aves vivas
    public String decrementarEnergiaAves(int tempoEvento){

        SQLiteDatabase db = conn.getWritableDatabase();

        String retorno = null;
        String updateQuery1 = "";
        String updateQuery2 = "";

        try {

            // decrementa nivelEnergia: somente aves vivas (nivelEnergia > 0)
            updateQuery1 = "UPDATE tbAveEvento SET nivelEnergia = (nivelEnergia - 5) WHERE nivelEnergia > 0";

            // Tratar inconsistências:
            // caso alguma tenha ficado alguma ave "viva" (tempoEventoMorte = 0) e com nivelEnergia <= 0: setar nivelEnergia = 0 e atualizar tempoEventoMorte
            updateQuery2 = "UPDATE tbAveEvento SET tempoEventoMorte = " + tempoEvento + ", nivelEnergia = 0 WHERE nivelEnergia <= 0 AND tempoEventoMorte = 0";

            db.execSQL(updateQuery1);
            db.execSQL(updateQuery2);

            retorno = "1";

        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
            return retorno;
        }

    }

    // Retorna os dados de uma determinada ave
    public Ave getAveById(int idAve) {

        SQLiteDatabase db = conn.getReadableDatabase();

        Ave ave = null;
        Cor cor = null;
        Bico bico = null;
        int nivelEnergia = 0;
        //int nivelCamuflagem = 0;
        int tempoEventoNascimento = 0;
        int tempoEventoMorte = 0;
        double valorAptidaoCor = 0.00;
        double valorAptidaoBico = 0.00;

        //int id_ave = 0;
        int variacaEspecie = 0;
        int variacaoFenotipoCor = 0;
        int variacaoFenotipoBico = 0;
        int[] genCor = new int[] {0,0};
        int[] genBico = new int[] {0,0};

        String selectQuery = "";

        selectQuery = "SELECT * FROM tbAveEvento WHERE _id = " + idAve;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);

            try {

                if (cursor != null && cursor.moveToFirst()) {

                    //id_ave = cursor.getInt(cursor.getColumnIndex("_id"));
                    variacaEspecie = cursor.getInt(cursor.getColumnIndex("variacaEspecie"));
                    nivelEnergia = cursor.getInt(cursor.getColumnIndex("nivelEnergia"));
                    //nivelCamuflagem = cursor.getInt(cursor.getColumnIndex("nivelCamuflagem"));
                    tempoEventoNascimento = cursor.getInt(cursor.getColumnIndex("tempoEventoNascimento"));
                    tempoEventoMorte = cursor.getInt(cursor.getColumnIndex("tempoEventoMorte"));
                    valorAptidaoCor = cursor.getDouble(cursor.getColumnIndex("valorAptidaoCor"));
                    valorAptidaoBico = cursor.getDouble(cursor.getColumnIndex("valorAptidaoBico"));

                    switch (variacaEspecie) {
                        case 1: // Verde/Bico Grande (AABB | 4422)

                            variacaoFenotipoCor = 1; // verde
                            variacaoFenotipoBico = 1; //  grande
                            genCor = ConstantesGenotipos.GENOTIPO_COR_VERDE;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_GRANDE;
                            break;

                        case 2: // Verde/Bico Pequeno (AABb | 4421)

                            variacaoFenotipoCor = 1; // verde
                            variacaoFenotipoBico = 2; // pequeno
                            genCor = ConstantesGenotipos.GENOTIPO_COR_VERDE;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_PEQUENO;
                            break;

                        case 3: // Verde/Bico Alicate (AAbb | 4411)

                            variacaoFenotipoCor = 1; // verde
                            variacaoFenotipoBico = 3; // pequeno
                            genCor = ConstantesGenotipos.GENOTIPO_COR_VERDE;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_ALICATE;
                            break;

                        case 4: // Mesclado/Bico Grande (AaBB | 4322)

                            variacaoFenotipoCor = 2; // mesclado
                            variacaoFenotipoBico = 1; // grande
                            genCor = ConstantesGenotipos.GENOTIPO_COR_MESCLADO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_GRANDE;
                            break;

                        case 5: // Mesclado/Bico Pequeno (AaBb | 4321)

                            variacaoFenotipoCor = 2; // mesclado
                            variacaoFenotipoBico = 2; // pequeno
                            genCor = ConstantesGenotipos.GENOTIPO_COR_MESCLADO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_PEQUENO;
                            break;

                        case 6: // Mesclado/Bico Alicate (Aabb | 4311)

                            variacaoFenotipoCor = 2; // mesclado
                            variacaoFenotipoBico = 3; // alicate
                            genCor = ConstantesGenotipos.GENOTIPO_COR_MESCLADO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_ALICATE;
                            break;

                        case 7: // Amarelo/Bico Grande (aaBB | 3322)

                            variacaoFenotipoCor = 3; // amarelo
                            variacaoFenotipoBico = 1; // grande
                            genCor = ConstantesGenotipos.GENOTIPO_COR_AMARELO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_GRANDE;
                            break;

                        case 8: // Amarelo/Bico Pequeno (aaBb | 3321)

                            variacaoFenotipoCor = 3; // amarelo
                            variacaoFenotipoBico = 2; // pequeno
                            genCor = ConstantesGenotipos.GENOTIPO_COR_AMARELO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_PEQUENO;
                            break;

                        case 9: // Amarelo/Bico Alicate (aabb | 3311)

                            variacaoFenotipoCor = 3; // amarelo
                            variacaoFenotipoBico = 3; // alicate
                            genCor = ConstantesGenotipos.GENOTIPO_COR_AMARELO;
                            genBico = ConstantesGenotipos.GENOTIPO_BICO_ALICATE;
                            break;

                        default:
                            ave = null;
                            break;
                    }

                    //if (ave == null) {

                    // Cor(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
                    cor = new Cor(1,variacaoFenotipoCor,genCor, valorAptidaoCor);

                    // Bico(int tipoFenotipo, int variacaoFenotipo, int[] genotipo)
                    bico = new Bico(2,variacaoFenotipoBico,genBico,valorAptidaoBico);

                    //Ave(int id_ave, int variacaoEspecie, Cor cor, Bico bico, int nivelEnergia,int tempoEventoNascimento, int tempoEventoMorte, double valorAptidaoCor, double valorAptidaoBico)
                    ave = new Ave(idAve, variacaEspecie, cor, bico, nivelEnergia, tempoEventoNascimento, tempoEventoMorte, valorAptidaoCor, valorAptidaoBico);

                }

            }finally {
                cursor.close();
            }

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return ave;
    }

    //Listagem de aves ativas
    public ArrayList<Ave> listaEspeciesAves(int tipoLista, double valorAptidao) {

        SQLiteDatabase db = conn.getReadableDatabase();

        ArrayList<Ave> listaEspecies = new ArrayList<>();
        Ave ave = null;
        String selectQuery = "";
        int idAve = 0;

        try {

            if(tipoLista == 0) // Lista todas as aves vivas
            {
                selectQuery = "SELECT * FROM tbAveEvento WHERE nivelEnergia > 0"; // nivelEnergia > 0: somente aves vivas
            }
            else // Lista aves vivas e com um determinado valorAptidaoCor
            {
                selectQuery =   "SELECT * FROM tbAveEvento " +
                                "WHERE nivelEnergia > 0 ";
                selectQuery += "AND valorAptidaoCor = " + valorAptidao;

            }

            Cursor cursor = db.rawQuery(selectQuery,null);

            try {

                if(cursor != null && cursor.getCount() > 0) {

                    cursor.moveToPosition(-1);

                    while(cursor.moveToNext()) {

                        idAve = cursor.getInt(cursor.getColumnIndex("_id"));
                        ave = getAveById(idAve);

                        if (ave != null) listaEspecies.add(ave);

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

        return listaEspecies;
    }

    //Lista o total de aves, agrupadas por variacaEspecie
    public LinkedHashMap<Integer, String> listaHistoricoQtdAvesSobreviventes(String tempo) {

        SQLiteDatabase db = conn.getReadableDatabase();

        LinkedHashMap<Integer, String> lstQtdEspecies = new LinkedHashMap<>();

        String selectQuery = "";

        try{

            selectQuery =   "SELECT DISTINCT A.variacaEspecie, (IFNULL(AN.qtd_aves_total,0) - IFNULL(AM.qtd_aves_mortas,0)) AS qtd FROM tbAve A " +
                            "LEFT JOIN (" +
                            "SELECT variacaEspecie, COUNT(*) AS qtd_aves_total FROM tbAveEvento ";

                            if(tempo != "") // lista a quantidade total de aves nascidas até o valor de tempo
                            {
                                selectQuery += " WHERE tempoEventoNascimento <= " + tempo;
                            }

            selectQuery +=  " GROUP BY variacaEspecie" +
                            ") AN ON A.variacaEspecie = AN.variacaEspecie ";

            selectQuery +=  "LEFT JOIN (" +
                            "SELECT variacaEspecie, COUNT(*) AS qtd_aves_mortas FROM tbAveEvento " +
                            "WHERE nivelEnergia = 0 ";

                            if(tempo != "") // lista a quantidade total de aves mortas até o valor de tempo
                            {
                                selectQuery +=  "AND tempoEventoMorte > 0 AND tempoEventoMorte <= " + tempo;
                            }

            selectQuery +=  " GROUP BY variacaEspecie" +
                            ") AM ON A.variacaEspecie = AM.variacaEspecie";

            selectQuery +=  " ORDER BY A.variacaEspecie ASC";

            Cursor cursor = db.rawQuery(selectQuery,null);

            int key = 0;
            String value = "";

            try{

                if(cursor != null &&  cursor.moveToFirst()) {

                    do {

                        key = Integer.parseInt(cursor.getString(cursor.getColumnIndex("variacaEspecie")));
                        value = cursor.getString(cursor.getColumnIndex("qtd"));

                        lstQtdEspecies.put(key,value);

                    }while(cursor.moveToNext());

                }

            } finally {
                cursor.close();
            }

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return lstQtdEspecies;

    }

    //Listagem qtd total de aves ativas
    public Integer qtdTotalAvesVivas() {

        SQLiteDatabase db = conn.getReadableDatabase();

        String selectQuery = "";
        int qtd = 0;

        try {

           selectQuery = "SELECT COUNT(*) AS qtd FROM tbAveEvento WHERE nivelEnergia > 0"; // nivelEnergia > 0: somente aves vivas

            Cursor cursor = db.rawQuery(selectQuery,null);

            try {

                if(cursor != null && cursor.getCount() > 0) {

                    cursor.moveToPosition(-1);

                    while(cursor.moveToNext()) {

                        qtd = cursor.getInt(cursor.getColumnIndex("qtd"));

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

        return qtd;
    }

    //Limpa a tabela tbAveEvento
    public void deletaAveEvento() {

        SQLiteDatabase db = conn.getWritableDatabase();

        try {

            db.delete("tbAveEvento", null, null);
        }
        finally
        {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}