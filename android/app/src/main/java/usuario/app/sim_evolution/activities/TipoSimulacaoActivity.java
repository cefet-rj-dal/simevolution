package usuario.app.sim_evolution.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.util.List;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.model.entities.Ave;
import usuario.app.sim_evolution.services.ExibirMensagemService;
import usuario.app.sim_evolution.services.MusicService;

public class TipoSimulacaoActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ToggleButton btn_sound;
    ImageButton btn_help,btn_exit,btn_custom, btn_random;

    String titulo_msg, corpo_msg, tipoAmbiente;

    private List<Ave> avesTotais;
    private List<Ave> avesSelecionadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregarTelaSelecaoTipoSimulacao();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    public void carregarTelaSelecaoTipoSimulacao() {

        setContentView(R.layout.activity_tipo_simulacao);

        inicializarVariaveis();
        setListeners();

        // Destrói o arquivo do BD para eliminar dados residuais de simulação anterior.
        // Desse modo, a simulação inicia-se em uma base limpa.
        destroyDB(TipoSimulacaoActivity.this);

    }

    private void inicializarVariaveis() {
        btn_custom = (ImageButton) findViewById(R.id.btn_custom);
        btn_random = (ImageButton) findViewById(R.id.btn_random);
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

                        titulo_msg = "Tipos de Simulação";
                        corpo_msg = "Ao selecionar o tipo de simulação PADRÃO você poderá escolher o ambiente e as aves que comporão a simulação. ";
                        corpo_msg += "Já no tipo de simulação CUSTOM, o sistema permitirá que você configure os níveis de aptidão de cada fenótipo (características de cor e formato de bico), independentemente do ambiente.";

                        ExibirMensagemService.exibirAlertDialog(TipoSimulacaoActivity.this,titulo_msg,corpo_msg);
                    }
                }
        );


        //Comentado pois para aplicações Android, não faz sentido o botão de fechar. Por esta razão, ocorre uma cadeia de erros.
        /*btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish();
                        //System.exit(0);

                        moveTaskToBack(true);
                        stopService(new Intent(TipoSimulacaoActivity.this, MusicService.class));
                    }
                }
        );*/

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               MusicService.setMute(!isChecked);
            }
        });

    }

    public void callCenariosActivity(View view) {
        Intent intent = new Intent(this,CenariosActivity.class);
        startActivity(intent);
    }

    public void callAptidaoActivity(View view) {
        Intent intent = new Intent(this,AptidaoActivity.class);
        startActivity(intent);
    }

    // Invoca a simulação com base na seleção aleatório de 4 aves dentres as possíveis do ambiente
    /*public void callSimulacaoActivity(View view) {

        // Seleciona aleatoriamente os elementos para a simulação
        selecionaElementosAleatorios();

        // Insere as aves selecionadas no BD antes de iniciar a simulação
        persistirAvesSelecionadas();

        Intent intent = new Intent(this,SimulacaoActivity.class);
        intent.putExtra(EXTRA_MESSAGE,"0" + "," + tipoAmbiente); //Envia o tipoAmbiente
        startActivity(intent);

    }*/

    /*public void selecionaElementosAleatorios() {

        Random geradorNumeroAleatorio = new Random();

        int[] arrayTiposAmbientes = new int[] {1,2}; // 1: Floresta | 2: Savana
        int[] arrayIndexAves = new int[4] ; // índices das aves selecionadas

        int indexTipoAmbiente = 0;

        indexTipoAmbiente = geradorNumeroAleatorio.nextInt(arrayTiposAmbientes.length);
        tipoAmbiente = String.valueOf(arrayTiposAmbientes[indexTipoAmbiente]);

        if(tipoAmbiente.equals("1")) {
            AmbienteRepositorio.inserirAmbiente(TipoSimulacaoActivity.this,Integer.parseInt(tipoAmbiente),1);
        }
        else {
            AmbienteRepositorio.inserirAmbiente(TipoSimulacaoActivity.this,Integer.parseInt(tipoAmbiente),3);
        }

        // Monta lista com todas as aves possíveis do ambiente
        avesTotais = AveRepositorio.criarAve(Integer.parseInt(tipoAmbiente));
        avesSelecionadas = new ArrayList<>();

        // Seleciona aleatoriamente 4 posições do array total de aves
        // Note-se que aqui poderá ser escolhida uma mesma espécia mais de uma vez
        for(int i = 0; i < arrayIndexAves.length; i++) {
            arrayIndexAves[i] = geradorNumeroAleatorio.nextInt(avesTotais.size());
        }

        // Popula o array de aves selecionadas
        for(int i = 0; i < arrayIndexAves.length; i++) {
            avesSelecionadas.add(avesTotais.get(arrayIndexAves[i]));
        }

    }*/

    // Insere as aves selecionadas no BD
    /*public void persistirAvesSelecionadas() {

        // Limpa preventivamente as tabelas de aves antes de inserir os novos registros
        //AveRepositorio.removerTodosRegistrosAve(TipoSimulacaoActivity.this);

        for (Ave a : avesSelecionadas){

            AveRepositorio.inserirEventoAve(this, 1, a);
        }

    }*/

    // Destrói o arquivo do BD
    private void destroyDB(Context context) {
        context.deleteDatabase("DBSimEvolution.db");
    }

}
