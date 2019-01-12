package usuario.app.sim_evolution.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.services.ExibirMensagemService;
import usuario.app.sim_evolution.services.MusicService;

public class CenariosActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ToggleButton btn_sound;
    ImageButton btn_help,btn_exit, btn_savana, btn_floresta;

    String titulo_msg, corpo_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregarTelaSelecaoCenarios();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    public void carregarTelaSelecaoCenarios() {

        setContentView(R.layout.activity_cenarios);

        inicializarVariaveis();
        setListeners();

    }

    private void inicializarVariaveis() {
        btn_savana = (ImageButton) findViewById(R.id.btn_savana);
        btn_floresta = (ImageButton) findViewById(R.id.btn_floresta);
        btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
//        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_help = (ImageButton) findViewById(R.id.btn_help);
        btn_sound.setChecked(!MusicService.isMute());
    }

    private void setListeners(){

        btn_help.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        titulo_msg = "Seleção de Ambiente";
                        corpo_msg = "Aqui você poderá escolher o ambiente em que ocorrerá a simulação. ";
                        corpo_msg += "A FLORESTA é caracterizada por alta densidade de árvores e cor predominantemente VERDE, " +
                                "já a SAVANA possui coloração AMARELADA em decorrência de clima seco e nela predominam as plantas gramíneas. ";
                        corpo_msg += "De acordo com a teoria da Seleção Natural de Darwin, em um determinado ambiente apenas os seres vivos que possuem " +
                                "as condições ideais de sobrevivência conseguirão perpetuar a sua espécie. Assim, as aves com coloração mais próxima da " +
                                "cor do ambiente ou com formato de bico mais adequado aos alimentos fornecidos pelo meio " +
                                "têm mais chances de perpetuar sua espécie, transmitindo suas características genéticas e fenotípicas aos seus descendentes.";

                        ExibirMensagemService.exibirAlertDialog(CenariosActivity.this,titulo_msg,corpo_msg);
                    }
                }
        );

        //Comentado pois para aplicações Android, não faz sentido o botão de fechar. Por esta razão, ocorre uma cadeia de erros
        /*btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish();
                        //System.exit(0);

                        moveTaskToBack(true);
                        stopService(new Intent(CenariosActivity.this, MusicService.class));

                    }
                }
        );*/

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               MusicService.setMute(!isChecked);
            }
        });

    }

    public void callAvesActivity1(View view) {
        Intent intent = new Intent(this,AvesActivity.class);
        intent.putExtra(EXTRA_MESSAGE,"1"); //Envia o tipoAmbiente "Floresta" (1)
        startActivity(intent);
    }

    public void callAvesActivity2(View view) {
        Intent intent = new Intent(this,AvesActivity.class);
        intent.putExtra(EXTRA_MESSAGE,"2"); //Envia o tipoAmbiente "Savana" (2)
        startActivity(intent);
    }

}
