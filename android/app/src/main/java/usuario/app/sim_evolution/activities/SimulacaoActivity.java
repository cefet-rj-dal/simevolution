package usuario.app.sim_evolution.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.domain.AveRepositorio;
import usuario.app.sim_evolution.domain.FenotipoRepositorio;
import usuario.app.sim_evolution.enums.Musicas;
import usuario.app.sim_evolution.services.ExibirMensagemService;
import usuario.app.sim_evolution.services.MusicService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;

import java.net.URLEncoder;

import usuario.app.sim_evolution.survey.Survey;
import usuario.app.sim_evolution.survey.SurveyAndroid;

public class SimulacaoActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ToggleButton btn_sound, btn_pause;
    ImageButton btn_help; // btn_exit, btn_stop;

    TextView qtd_4422, qtd_4421, qtd_4411, qtd_4322, qtd_4321, qtd_4311, qtd_3322, qtd_3321, qtd_3311;
    ImageView img_4422, img_4421, img_4411,img_4322, img_4321, img_4311,img_3322, img_3321, img_3311;

    String titulo_msg, corpo_msg;

    TextView txtContador, lblContador;

    int contadorTempo = 0;
    int intervaloCruzamento = 0;
    int intervaloPredacao = 10;
    int intervaloAlimentacao = 0;

    static final int LIMITE_SIMULACAO = 600; // 1 unidade de tempo = 1 segundo (1000 milissegundos)

    String tipoAmbiente;

    Handler handlerContador = new Handler();
    Handler handlerCruzamento = new Handler();
    Handler handlerPredacao = new Handler();
    Handler handlerAlimentacao = new Handler();

    LinearLayout l1;

    boolean emPausa = true;

    ArrayList<Double> listaValoresAptidaoFenotiposCor = new ArrayList<>();
    ArrayList<Double> listaValoresAptidaoFenotiposBico = new ArrayList<>();
    double valorMaisAltoAptidaoBico = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregarTelaSimulacao();
    }

    @Override
    public void onResume(){
        super.onResume();
        iniciarSimulacao();

        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    //onPause is called when another activity gains focus
    @Override
    public void onPause(){
        super.onPause();

        pausarSimulacao();
    }

    //onStop() is called when the activity is no longer visible
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MusicService.setMusicaAtual(Musicas.PRINCIPAL);
        MusicService.tocarMusicaAtual();
        transmitirResultado(this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void carregarTelaSimulacao() {

        setContentView(R.layout.activity_simulacao);
        inicializarVariaveis();
        atualizarVariaveis();
        setListeners();
        iniciarSimulacao();
    }

    private void inicializarVariaveis() {

        btn_pause = (ToggleButton) findViewById(R.id.btn_pause);

        btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
//        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_help = (ImageButton) findViewById(R.id.btn_help);

        // Campos para informação das qts das aves
        qtd_4422 = (TextView) findViewById(R.id.qtd_4422);
        qtd_4421 = (TextView) findViewById(R.id.qtd_4421);
        qtd_4411 = (TextView) findViewById(R.id.qtd_4411);
        qtd_4322 = (TextView) findViewById(R.id.qtd_4322);
        qtd_4321 = (TextView) findViewById(R.id.qtd_4321);
        qtd_4311 = (TextView) findViewById(R.id.qtd_4311);
        qtd_3322 = (TextView) findViewById(R.id.qtd_3322);
        qtd_3321 = (TextView) findViewById(R.id.qtd_3321);
        qtd_3311 = (TextView) findViewById(R.id.qtd_3311);

        // Botões de seleção das aves
        img_4422 = (ImageView) findViewById(R.id.img_4422);
        img_4421 = (ImageView) findViewById(R.id.img_4421);
        img_4411 = (ImageView) findViewById(R.id.img_4411);
        img_4322 = (ImageView) findViewById(R.id.img_4322);
        img_4321 = (ImageView) findViewById(R.id.img_4321);
        img_4311 = (ImageView) findViewById(R.id.img_4311);
        img_3322 = (ImageView) findViewById(R.id.img_3322);
        img_3321 = (ImageView) findViewById(R.id.img_3321);
        img_3311 = (ImageView) findViewById(R.id.img_3311);

        txtContador = (TextView) findViewById(R.id.txtContador);
        //timeProgressBar = (ProgressBar)findViewById(R.id.timeProgressBar);
        lblContador = (TextView) findViewById(R.id.lblContador);
        txtContador.setBackgroundColor(Color.WHITE);

        l1 = (LinearLayout) findViewById(R.id.activity_simulacao);

        // Lista os valores de aptidão dos fenótipos sobre o ambiente atual
        listaValoresAptidaoFenotiposCor = FenotipoRepositorio.listaValorAptidaoFenotipos(this,1); // 1 (cor)
        listaValoresAptidaoFenotiposBico = FenotipoRepositorio.listaValorAptidaoFenotipos(this,2); // 2 (bico)

        for (double valor : listaValoresAptidaoFenotiposBico){

            valorMaisAltoAptidaoBico = valor;
        }

        btn_sound.setChecked(!MusicService.isMute());
    }

    private void atualizarVariaveis() {

        String[] params = null;

        Intent intent = getIntent();

        // Trata os parâmetros recebidos das outras activities
        if(!intent.getStringExtra(AvesActivity.EXTRA_MESSAGE).isEmpty()) {
            params = intent.getStringExtra(AvesActivity.EXTRA_MESSAGE).split(",");
        }
        if(!intent.getStringExtra(ChartActivity.EXTRA_MESSAGE).isEmpty()) {
            params = intent.getStringExtra(ChartActivity.EXTRA_MESSAGE).split(",");
        }

        // Parâmetros de AvesActivity: ("0" ,tipoAmbiente)
        // Parâmetros de ChartActivity: (tempo,tipoAmbiente,intervaloCruzamento,intervaloPredacao,intervaloAlimentacao)

        if(params[0] != null) {
            contadorTempo = Integer.parseInt(params[0]);
            txtContador.setText(params[0]);
        }

        if(params[1] != null)
            tipoAmbiente = params[1];

        if(params.length > 2) { // parâmetros de ChartActivity
            if(params[2] != null) intervaloCruzamento = Integer.parseInt(params[2]);
            if(params[3] != null) intervaloPredacao = Integer.parseInt(params[3]);
            if(params[4] != null) intervaloAlimentacao = Integer.parseInt(params[4]);
        }

        //Troca o background conforme o ambiente escolhido pelo usuário
        if(tipoAmbiente.equals("1")) //ambiente "Floresta"
        {
            l1.setBackgroundResource(R.drawable.bg4);
        }
        if(tipoAmbiente.equals("2")) //ambiente "Savana"
        {
            l1.setBackgroundResource(R.drawable.bg5);
            lblContador.setBackgroundColor(Color.WHITE);
        }
        if(tipoAmbiente.equals("3")) //ambiente "Genérico"
        {
            l1.setBackgroundResource(R.drawable.bg2);
        }

    }

    private void setListeners(){

        btn_help.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        titulo_msg =    "Simulação de Seleção de Espécies";
                        corpo_msg =     "Aqui você visualiza as aves existentes no ambiente com suas respectivas quantidades, exibidas nas caixinhas com números à direita de cada variedade da espécie. " +
                                        "O sistema exibe um contador de tempo, em que cada unidade de tempo equivale a cerca de 1 segundo. " +
                                        "O limite de tempo da simulação é de 600 unidades de tempo (cerca de 10 minutos), quando o sistema exibirá mensagem informativa e pausará. " +
                                        "Você poderá pausar a simulação em qualquer momento, tocando no botão correspondente na parte superior da tela. " +
                                        "Em intervalos de tempo predeterminados ocorrem eventos de alimentação, reprodução e predação das aves, sendo informados pelo sistema através de mensagens. " +
                                        "Os eventos de reprodução e predação alteram as quantidades das aves e o sistema atualiza os valores nas caixinhas numéricas das aves envolvidas nos eventos. " +
                                        "Todos os eventos são relacionados com os valores de aptidão dos fenótipos das aves: a cor influencia na sobrevivência das aves, pois está relacionada com as probabilidades de serem predadas; " +
                                        "o formato do bico impacta na alimentação das aves, logo na sua capacidade de reprodução. " +
                                        "Em qualquer momento da simulação você poderá consultar o gráfico de sobrevivência das aves, tocando no botão correspondente na parte superior da tela. " +
                                        "Para retornar para a simulação a partir da tela do gráfico, toque no botão com a função VOLTAR em seu aparelho.";

                        ExibirMensagemService.exibirAlertDialog(SimulacaoActivity.this,titulo_msg,corpo_msg);
                    }
                }
        );

        //Comentado pois para aplicações Android, não faz sentido o botão de fechar. Por esta razão, ocorre uma cadeia de erros
        /*btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish(); // encerra a Activity

                        moveTaskToBack(true);
                        stopService(new Intent(SimulacaoActivity.this, MusicService.class));

                    }
                }
        );*/

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicService.setMute(!isChecked);
            }
        });

    }

    public void iniciarPausarSimulacao(View v ){
        if (emPausa) {
            iniciarSimulacao();
        } else {
            pausarSimulacao();
        }
    }

    private void iniciarSimulacao() {

        if (!emPausa)
            return;

        MusicService.setMusicaAtual(Musicas.SIMULACAO);
        MusicService.tocarMusicaAtual();

        atualizarLista();

        if(contadorTempo < LIMITE_SIMULACAO) {
            //Invoca as threads de eventos
            handlerContador.post(atualizarContador);
            handlerCruzamento.post(executarCruzamento);
            handlerPredacao.post(executarPredacao);
            handlerAlimentacao.post(executarAlimentacao);
            emPausa = false;
        } else {
            emPausa = true;
        }
    }

    private void pausarSimulacao(){

        if (emPausa)
            return;

        handlerContador.removeCallbacks(atualizarContador);
        handlerPredacao.removeCallbacks(executarPredacao);
        handlerCruzamento.removeCallbacks(executarCruzamento);
        handlerAlimentacao.removeCallbacks(executarAlimentacao);

        MusicService.pausarMusicaAtual();
        emPausa = true;
    }

    // Atualiza na tela a exibição de aves e suas quantidades
    public void atualizarLista() {

        //É neste ponto que a simulação está obtendo do banco as últimas aves restantes, tomando como ponto de partida a última simulação
        //É necessário tratar este método para que uma simulação anterior não influencie na simulação seguinte
        //Não é necessário forçar o carregamento da tela anterior ou ou o fechamento desta. Ao pressionar o botão voltar, a activity já é finalizada automaticamente.
        HashMap<Integer,String> lstHistoricoQtdAvesSobreviventes = AveRepositorio.listaHistoricoQtdAvesSobreviventes(this,"");

        Iterator<Integer> keySetIterator = lstHistoricoQtdAvesSobreviventes.keySet().iterator();

        while(keySetIterator.hasNext()) {
            Integer key = keySetIterator.next();

            switch (key) {
                case 1: //A
                    qtd_4422.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4422.setVisibility(View.INVISIBLE);
                        img_4422.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4422.setVisibility(View.VISIBLE);
                        img_4422.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4422.setTextColor(Color.RED);
                        qtd_4422.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4422.setTextColor(Color.BLACK);
                        qtd_4422.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 2: //B
                    qtd_4421.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4421.setVisibility(View.INVISIBLE);
                        img_4421.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4421.setVisibility(View.VISIBLE);
                        img_4421.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4421.setTextColor(Color.RED);
                        qtd_4421.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4421.setTextColor(Color.BLACK);
                        qtd_4421.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 3: //C
                    qtd_4411.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4411.setVisibility(View.INVISIBLE);
                        img_4411.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4411.setVisibility(View.VISIBLE);
                        img_4411.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4411.setTextColor(Color.RED);
                        qtd_4411.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4411.setTextColor(Color.BLACK);
                        qtd_4411.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 4: //D
                    qtd_4322.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4322.setVisibility(View.INVISIBLE);
                        img_4322.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4322.setVisibility(View.VISIBLE);
                        img_4322.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4322.setTextColor(Color.RED);
                        qtd_4322.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4322.setTextColor(Color.BLACK);
                        qtd_4322.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 5: //E
                    qtd_4321.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4321.setVisibility(View.INVISIBLE);
                        img_4321.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4321.setVisibility(View.VISIBLE);
                        img_4321.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4321.setTextColor(Color.RED);
                        qtd_4321.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4321.setTextColor(Color.BLACK);
                        qtd_4321.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 6: //F
                    qtd_4311.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_4311.setVisibility(View.INVISIBLE);
                        img_4311.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_4311.setVisibility(View.VISIBLE);
                        img_4311.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_4311.setTextColor(Color.RED);
                        qtd_4311.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_4311.setTextColor(Color.BLACK);
                        qtd_4311.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 7: //G
                    qtd_3322.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_3322.setVisibility(View.INVISIBLE);
                        img_3322.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_3322.setVisibility(View.VISIBLE);
                        img_3322.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_3322.setTextColor(Color.RED);
                        qtd_3322.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_3322.setTextColor(Color.BLACK);
                        qtd_3322.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 8: //H
                    qtd_3321.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_3321.setVisibility(View.INVISIBLE);
                        img_3321.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_3321.setVisibility(View.VISIBLE);
                        img_3321.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_3321.setTextColor(Color.RED);
                        qtd_3321.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_3321.setTextColor(Color.BLACK);
                        qtd_3321.setBackgroundColor(Color.WHITE);
                    }
                    break;

                case 9: //I
                    qtd_3311.setText("  " + lstHistoricoQtdAvesSobreviventes.get(key));

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("0")) {
                        qtd_3311.setVisibility(View.INVISIBLE);
                        img_3311.setVisibility(View.INVISIBLE);
                    } else {
                        qtd_3311.setVisibility(View.VISIBLE);
                        img_3311.setVisibility(View.VISIBLE);
                    }

                    if (lstHistoricoQtdAvesSobreviventes.get(key).equals("1")) {
                        qtd_3311.setTextColor(Color.RED);
                        qtd_3311.setBackgroundColor(Color.YELLOW);
                    } else {
                        qtd_3311.setTextColor(Color.BLACK);
                        qtd_3311.setBackgroundColor(Color.WHITE);
                    }
                    break;

            }
        }

    }

    public void callChartActivity(View view) {

        pausarSimulacao();

        Intent intent = new Intent(this,ChartActivity.class);

        //Passa por parâmetro os valores atuais do ambientede e de tempo do contador e dos eventos
        intent.putExtra(EXTRA_MESSAGE,String.valueOf(contadorTempo) + "," + tipoAmbiente + "," + String.valueOf(intervaloCruzamento) + "," + String.valueOf(intervaloPredacao) + "," + String.valueOf(intervaloAlimentacao)); //Envia variáveis com valores correntes
        startActivity(intent);
    }

    private void alertDialogSimulacao(int flagAlert) {

        // LayoutInflater é utilizado para inflar o layout em uma view
        LayoutInflater li = getLayoutInflater();

        // Infla o layout alert_timeout.xml na view
        View view = li.inflate(R.layout.alert_timeout,null);

        switch (flagAlert) {
            case 0: // flagAlert 0: thread atualizarContador
                titulo_msg = "Fim da Simulação";
                break;
            case 1: // flagAlert 1: threads Alimentação e Cruzamento
                titulo_msg = "Aves Insuficientes";
                // Infla o layout alert_stop.xml na view
                view = li.inflate(R.layout.alert_stop,null);
                TextView txt1 = (TextView) view.findViewById(R.id.txt_stop);
                txt1.setText("Não há aves suficientes para continuidade da espécie. A simulação foi encerrada.");
                break;
            case 2: // flagAlert 2: thread Predação
                titulo_msg = "Aves Insuficientes";
                // Infla o layout alert_stop.xml na view
                view = li.inflate(R.layout.alert_stop,null);
                TextView txt2 = (TextView) view.findViewById(R.id.txt_stop);
                txt2.setText("Não há aves suficientes para predação. A simulação foi encerrada.");
                break;
        }

        // Monta o AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(titulo_msg)
                .setView(view)
                .setNeutralButton("OK", null)
                .create();

        dialog.show();

    }

    private void transmitirResultado(final AppCompatActivity activity) {

        // Toda chamada externa necessita rodar em background, então utilizamos thread

        new Thread(new Runnable() {

            @Override

            public void run() {

                // Criamos nosso objeto de retorno que poderia ser uma entidade (Exemplo: Aluno, Usuário, etc.), nesse caso utilizamos algo genérico.
                Object retorno = null;

                Survey survey = SurveyAndroid.createSurvey(activity);

                //addItem(String key, String att, String val)
                survey.addItem("1", "tipo_ambiente", tipoAmbiente);

                // Recupera as aves que iniciaram a simulação, com suas respectivas quantidades
                HashMap<Integer,String> lstHistoricoQtdAvesSobreviventes = AveRepositorio.listaHistoricoQtdAvesSobreviventes(activity,"1");

                Iterator<Integer> keySetIterator = lstHistoricoQtdAvesSobreviventes.keySet().iterator();

                while(keySetIterator.hasNext()) {
                    Integer key = keySetIterator.next();

                    switch (key) {
                        case 1: //A
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_A_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 2: //B
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_B_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 3: //C
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_C_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 4: //D
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_D_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 5: //E
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_E_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 6: //F
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_F_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 7: //G
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_G_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 8: //H
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_H_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 9: //I
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("2", "ave_I_inicial", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;
                    }
                }

                // Recupera as aves que finalizaram a simulação, com suas respectivas quantidades
                lstHistoricoQtdAvesSobreviventes = AveRepositorio.listaHistoricoQtdAvesSobreviventes(activity,"");

                keySetIterator = lstHistoricoQtdAvesSobreviventes.keySet().iterator();

                while(keySetIterator.hasNext()) {
                    Integer key = keySetIterator.next();

                    switch (key) {
                        case 1: //A
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_A_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 2: //B
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_B_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 3: //C
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_C_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 4: //D
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_D_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 5: //E
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_E_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 6: //F
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_F_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 7: //G
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_G_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 8: //H
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_H_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;

                        case 9: //I
                            if (!lstHistoricoQtdAvesSobreviventes.get(key).equals("0"))
                                survey.addItem("3", "ave_I_final", lstHistoricoQtdAvesSobreviventes.get(key));

                            break;
                    }
                }

                survey.addItem("4", "duracao_simulacao", String.valueOf(contadorTempo));
                String json = Survey.convert(survey);

                // Há a necessidade de tratarmos exceção tendo em vista que estamos realizando requisições em nossa aplicação
                try {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String url = "http://eic.cefet-rj.br/app/dsws/survey.spring?json=" + URLEncoder.encode(json);

                    // Setamos o cliente http e o nosso request, que será do tipo GET (O POST veremos em outros artigos)
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet request = new HttpGet();

                    // Setamos nossa URI
                    request.setURI(new URI(url));

                    // Executamos nossa transação HTTP
                    HttpResponse response = httpclient.execute(request);

                    // Pegamos o conteúdo advindo como resposta e inserimos em um InputStream
                    InputStream content = response.getEntity().getContent();

                    // Instanciamos o nosso Reader com o InputStream
                    Reader reader = new InputStreamReader(content);

                    // Aqui vamos utilizar a Biblioteca Gson para transformar o Json recebido em Objeto JAVA

                    /* Instanciamos o objeto Gson e em seguida utilizamos o método fromJson() passando como parâmetro o Reader instanciado e o tipo do Objeto que será retornado. */

                    Gson gson = new Gson();

                    retorno = gson.fromJson(reader, HashMap.class);

                    content.close();


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }).start();

    }

    /** THREADS - Início ***************************************************/

    private final Runnable atualizarContador = new Runnable() {

        volatile boolean isRunning = true;

        @Override
        public void run() {

            if(contadorTempo++ <= LIMITE_SIMULACAO && isRunning )
            {

                txtContador.setText(String.valueOf(contadorTempo));
                //updateProgress(contadorTempo);
                handlerContador.postDelayed(this, 1000); // re-executa em 1 intervalos de 1 segundo

            } else{
                atualizarLista();
                isRunning = false;
                alertDialogSimulacao(0); // flagAlert 0: thread atualizarContador
                pausarSimulacao(); //Única Thread responsável por pausar a simulação após esgotado o tempo limite
            }
        }
    };

    private final Runnable executarAlimentacao = new Runnable() {

        volatile boolean isRunning = true;

        @Override
        public void run() {

            String retornoAlimentacao = null;

            if(contadorTempo < LIMITE_SIMULACAO && isRunning){

                // intervaloAlimentacao inicia com 0. Assim, o intervalo de alimentação é redefinido em 3, 6 ,9, 12, 15,...
                if((intervaloAlimentacao + 3) == contadorTempo){

                    intervaloAlimentacao = contadorTempo;
                    retornoAlimentacao = AveRepositorio.alimentarAve(SimulacaoActivity.this,String.valueOf(contadorTempo));

                    if(retornoAlimentacao != null && !retornoAlimentacao.equals("N")){ // "N" indica que não há mais aves suficientes no ambiente

                        if(valorMaisAltoAptidaoBico > 0) {
                            Toast t = Toast.makeText(getBaseContext(), "Aves se alimentaram", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP | Gravity.LEFT, 10,73);
                            t.show();
                        }
                        else
                        {
                            Toast t = Toast.makeText(getBaseContext(), "Aves não se alimentaram", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP | Gravity.LEFT, 10,73);
                            t.show();
                        }

                    }else {
                        atualizarLista();
                        isRunning = false;
                        alertDialogSimulacao(1); // flagAlert 1: threads Alimentação e Cruzamento
                        pausarSimulacao();
                    }

                }

                handlerAlimentacao.postDelayed(this,1000);

            }
        }
    };

    private final Runnable executarCruzamento = new Runnable() {

        volatile boolean isRunning = true;

        @Override
        public void run() {

            String[] retornoCruzamento; // armazena os dados de retorno do método gerarFilhotes: {letra_parental_1, letra_parental_2, qtd_filhotes}
            String filhotes = "filhotes";

            if(contadorTempo <= LIMITE_SIMULACAO  && isRunning)
            {
                // intervaloCruzamento inicia com 0.  Assim, o intervalo de cruzamento é redefinido em 11 22 ,33, 44, 55,...
                if((intervaloCruzamento + 11) == contadorTempo) {

                    intervaloCruzamento = contadorTempo;
                    retornoCruzamento = AveRepositorio.gerarFilhotes(SimulacaoActivity.this,contadorTempo, Integer.parseInt(tipoAmbiente));

                    if(retornoCruzamento[2] != null && !retornoCruzamento[2].equals("N")) // "N" indica que não há mais aves suficientes no ambiente
                    {
                        if (!retornoCruzamento[2].equals("0")) {
                            if (retornoCruzamento[2].equals("1")) {
                                filhotes = "filhote"; // 1 filhote: corrigir a concordância
                            }
                            atualizarLista();
                            Toast t = Toast.makeText(SimulacaoActivity.this, "Aves " + String.valueOf(retornoCruzamento[0]) + " e " + String.valueOf(retornoCruzamento[1]) + " geraram " + String.valueOf(retornoCruzamento[2]) + " " + filhotes, Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 73);
                            t.show();
                        }
                        else // retornoCruzamento[2].equals("0"): 0 filhotes
                        {
                            atualizarLista();
                            Toast t = Toast.makeText(SimulacaoActivity.this, "Aves " + String.valueOf(retornoCruzamento[0]) + " e " + String.valueOf(retornoCruzamento[1]) + " não geraram filhotes", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 73);
                            t.show();
                        }
                    }
                    else
                    {
                        atualizarLista();
                        isRunning = false;
                        alertDialogSimulacao(1); // flagAlert 1: threads Alimentação e Cruzamento
                        pausarSimulacao();
                    }
                }

                handlerCruzamento.postDelayed(this,1000);

            }
        }
    };

    private final Runnable executarPredacao = new Runnable() {

        volatile boolean isRunning = true;

        @Override
        public void run() {

            String retornoPredacao = null;

            if(contadorTempo <= LIMITE_SIMULACAO  && isRunning){
                if(contadorTempo >= 10){

                    // intervaloPredacao inicia com 10. Assim, o primeiro evento ocorre em 15 e daí o intervalo é redefinido em 15, 20, 25, 30,...
                    if((intervaloPredacao + 5) == contadorTempo) {

                        intervaloPredacao = contadorTempo;
                        retornoPredacao = AveRepositorio.predarAve(SimulacaoActivity.this, String.valueOf(contadorTempo), listaValoresAptidaoFenotiposCor);

                        if(retornoPredacao != null)
                        {

                            if(!retornoPredacao.equals("N")) // N" indica que não há mais aves vivas
                            {
                                if(!retornoPredacao.equals("0")) // "0" indica que não há aves com os critérios
                                {
                                    MusicService.tocarSomPredacao();
                                    atualizarLista();
                                    Toast t = Toast.makeText(SimulacaoActivity.this,"Ave " + retornoPredacao + " predada",Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0,73);
                                    t.show();
                                }
                            }
                            else
                            {
                                MusicService.tocarSomPredacao();
                                atualizarLista();
                                isRunning = false;
                                alertDialogSimulacao(2); // flagAlert 2: thread Predação
                                pausarSimulacao();
                            }

                        }
                        else
                        {
                            atualizarLista();
                            isRunning = false;
                            pausarSimulacao();
                            alertDialogSimulacao(2); // flagAlert 2: thread Predação
                        }

                    }

                }

                handlerPredacao.postDelayed(this,1000);

            }
        }
    };

    /** THREADS - Fim ******************************************************/

}
