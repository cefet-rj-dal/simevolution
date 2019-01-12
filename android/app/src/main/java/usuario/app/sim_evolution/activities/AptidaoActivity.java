package usuario.app.sim_evolution.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.domain.FenotipoRepositorio;
import usuario.app.sim_evolution.enums.ConstantesValorAptidao;
import usuario.app.sim_evolution.services.ExibirMensagemService;
import usuario.app.sim_evolution.services.MusicService;

/**
 * Created by Josué on 27/01/2017.
 * Updated by Josué on 14/02/2017
 */

public class AptidaoActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ToggleButton btn_sound;
    ImageButton btn_help,btn_exit,btn_proximo;
    EditText chanceCor1, chanceCor2, chanceCor3, chanceBico1, chanceBico2, chanceBico3;

    String titulo_msg, corpo_msg;

    boolean valorVazio = false;
    boolean valorForaFaixa = false;
    boolean somaInconsistente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregarTelaConfiguracaoAptidao();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    public void applyViewGroupTypeface(Typeface normalTypeface, Typeface boldTypeface, ViewGroup viewGroup){

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);

            if (v instanceof ViewGroup)
                applyViewGroupTypeface(normalTypeface, boldTypeface, (ViewGroup) v);
            else if (v instanceof TextView){
                TextView textView = ((TextView) v);
                boolean isBold = textView.getTypeface() != null && textView.getTypeface().isBold();

                textView.setTypeface(isBold ? boldTypeface : normalTypeface);
            }
        }
    }

    public void carregarTelaConfiguracaoAptidao() {

        setContentView(R.layout.activity_aptidao);

        Typeface typefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/angrybirds_regular.ttf");
        Typeface typefaceNegrito = Typeface.createFromAsset(getAssets(), "fonts/angrybirds_regular_0.ttf");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_aptidao);

        applyViewGroupTypeface(typefaceNormal, typefaceNegrito, layout);

        inicializarVariaveis();
        setListeners();

        // Destrói o arquivo do BD para eliminar dados residuais de simulação anterior.
        // Desse modo, a simulação inicia-se em uma base limpa.
        destroyDB(AptidaoActivity.this);

    }

    private void inicializarVariaveis() {
        btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
//        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_help = (ImageButton) findViewById(R.id.btn_help);
        btn_proximo = (ImageButton) findViewById(R.id.btn_proximo);

        chanceCor1 = (EditText) findViewById(R.id.chanceCor1);
        chanceCor2 = (EditText) findViewById(R.id.chanceCor2);
        chanceCor3 = (EditText) findViewById(R.id.chanceCor3);
        chanceBico1 = (EditText) findViewById(R.id.chanceBico1);
        chanceBico2 = (EditText) findViewById(R.id.chanceBico2);
        chanceBico3 = (EditText) findViewById(R.id.chanceBico3);

        // Preenche campos com valores padrão
        chanceCor1.setText(String.valueOf(ConstantesValorAptidao.COR_CHANCE_BAIXA));
        chanceCor2.setText(String.valueOf(ConstantesValorAptidao.COR_CHANCE_MEDIA));
        chanceCor3.setText(String.valueOf(ConstantesValorAptidao.COR_CHANCE_ALTA));

        chanceBico1.setText(String.valueOf(ConstantesValorAptidao.BICO_CHANCE_BAIXA));
        chanceBico2.setText(String.valueOf(ConstantesValorAptidao.BICO_CHANCE_MEDIA));
        chanceBico3.setText(String.valueOf(ConstantesValorAptidao.BICO_CHANCE_ALTA));

        btn_sound.setChecked(!MusicService.isMute());
    }

    private void setListeners(){

        btn_help.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialogAptidao();

                        /*titulo_msg = "Aptidão dos Fenótipos";
                        corpo_msg = "Aqui você definirá os valores de aptidão de cada fenótipo (cor e formato de bico). ";
                        corpo_msg += "CHANCES DE PREDAÇÃO: define as chances de SOBREVIVÊNCIA de aves com cada cor no ambiente. " +
                                "Assim, quanto MENOR o valor, MENOR será a probabilidade de a ave ser predada. ";
                        corpo_msg += "CHANCES DE ALIMENTAÇÃO: define as chances de REPRODUÇÃO de aves com um determinado formato de bico no ambiente. " +
                                "Aves que se alimentam melhor têm mais energia e se reproduzem mais. ";
                        corpo_msg += "Dessa forma, quanto MENOR o valor, MENOR será a chance de a ave se alimentar no ambiente. ";
                        corpo_msg += "Informe somente valores maiores que 0.00 e até 1.00 (inclusive). ";
                        corpo_msg += "A SOMA dos 3 valores de cada fenótipo deve ser IGUAL a 1.00, ou seja, os valores de cor somados devem totalizar 1.00 e da mesma forma os valores de formato de bico. ";
                        corpo_msg += "Os campos já vêm preenchidos com valores padrão.";

                        ExibirMensagemService.exibirAlertDialog(AptidaoActivity.this,titulo_msg,corpo_msg);*/
                    }
                }
        );

        /*btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish();
                        //System.exit(0);

                        moveTaskToBack(true);
                        stopService(new Intent(AptidaoActivity.this, MusicService.class));

                    }
                }
        );*/

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicService.setMute(!isChecked);
            }
        });

    }

    public void callAvesActivity3(View view) {

        // Insere os valores de fitness no BD antes de prosseguir
        persistirValoresInformados();

        if(valorVazio == true){
            Toast.makeText(this, "Todos os campos devem ser preenchidos com valores maiores que 0.00.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(valorForaFaixa == true) {
                Toast.makeText(this, "Informe somente valores maiores que 0.00 e até 1.00 (inclusive).", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(somaInconsistente == true) {
                    Toast.makeText(this, "A soma dos três valores de cada fenótipo deve ser igual a 1.00.", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(this, AvesActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, "3"); //Envia o tipoAmbiente "Genérico" (3)
                    startActivity(intent);
                }
            }
        }
    }

    // Insere os valores de aptidao no BD
    public void persistirValoresInformados() {

        boolean resultadoValidacao = validarCampos();

        if(resultadoValidacao == false)
        {

            // 1 (cor) | 1 (verde)     | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 1, 1, Double.parseDouble(chanceCor1.getText().toString()));

            // 1 (cor) | 2 (mesclado)  | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 1, 2, Double.parseDouble(chanceCor2.getText().toString()));

            // 1 (cor) | 3 (amarelo)   | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 1, 3, Double.parseDouble(chanceCor3.getText().toString()));

            // 2 (bico) | 1 (grande)   | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 2, 1, Double.parseDouble(chanceBico1.getText().toString()));

            // 2 (bico) | 2 (pequeno)  | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 2, 2, Double.parseDouble(chanceBico2.getText().toString()));

            // 2 (bico) | 3 (alicate)   | chanceAdaptabilidade
            FenotipoRepositorio.inserirFenotipoAptidao(this, 2, 3, Double.parseDouble(chanceBico3.getText().toString()));

        }

    }

    public boolean validarCampos() {

        valorVazio = false;
        valorForaFaixa = false;
        somaInconsistente = false;

        ArrayList<EditText> listaTodosCampos = new ArrayList<>();
        ArrayList<EditText> listaCamposCor = new ArrayList<>();
        ArrayList<EditText> listaCamposBico = new ArrayList<>();

        listaTodosCampos.add(chanceCor1);
        listaTodosCampos.add(chanceCor2);
        listaTodosCampos.add(chanceCor3);
        listaTodosCampos.add(chanceBico1);
        listaTodosCampos.add(chanceBico2);
        listaTodosCampos.add(chanceBico3);

        listaCamposCor.add(chanceCor1);
        listaCamposCor.add(chanceCor2);
        listaCamposCor.add(chanceCor3);

        listaCamposBico.add(chanceBico1);
        listaCamposBico.add(chanceBico2);
        listaCamposBico.add(chanceBico3);

        // Verifica se há algum campo nulo ou com valor 0 (zero)
        for(int i = 0; i < listaTodosCampos.size(); i++) {
            if(listaTodosCampos.get(i).getText() == null || listaTodosCampos.get(i).getText().equals("0")) {
                valorVazio = true;
                break;
            }
        }

        if (valorVazio == true) return valorVazio;

        // Verifica se há algum campo > 1.00
        for(int i = 0; i < listaTodosCampos.size(); i++) {
            if(Double.parseDouble(listaTodosCampos.get(i).getText().toString()) > 1.00) {
                valorForaFaixa = true;
                break;
            }
        }

        if (valorForaFaixa == true) return valorForaFaixa;

        // Verifica se há inconsistência no somatório dos campos
        somaInconsistente = verificarSomatorioValoresCampos(listaCamposCor);

        if(somaInconsistente == true) return somaInconsistente;

        somaInconsistente = verificarSomatorioValoresCampos(listaCamposBico);

        if(somaInconsistente == true) return somaInconsistente;

        return false;

    }

    public boolean verificarSomatorioValoresCampos(ArrayList<EditText> listaCampos) {

        boolean retorno = false;

        double soma = 0;
        int qtdOcorrenciasValorMedio = 0;

        for(int i = 0; i < listaCampos.size(); i++)
        {
            // Incrementa o somatório
            soma += Double.parseDouble(listaCampos.get(i).getText().toString());

            // Verifica a ocorrência do valor médio (0.33)
            if(Double.parseDouble(listaCampos.get(i).getText().toString()) == 0.33) {
                qtdOcorrenciasValorMedio += 1;
            }

        }

        if(soma > 1.00) // soma inconsistente
            retorno = true;

        if(soma < 1.00) // soma inconsistente, mas verificar ocorrências do valor médio 0.33
        {
            retorno = true;

            if(qtdOcorrenciasValorMedio >= 2)
                retorno = false;

        }

        return retorno;

    }

    private void alertDialogAptidao() {

        // LayoutInflater é utilizado para inflar o layout em uma view
        LayoutInflater li = getLayoutInflater();

        // Infla o layout alert_aves.xml na view
        View view = li.inflate(R.layout.alert_aptidao,null);

        // Monta o AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Aptidão dos Fenótipos")
                .setView(view)
                .setNeutralButton("OK", null)
                .create();

        dialog.show();

    }

    // Destrói o arquivo do BD
    private void destroyDB(Context context) {
        context.deleteDatabase("DBSimEvolution.db");
    }
}


