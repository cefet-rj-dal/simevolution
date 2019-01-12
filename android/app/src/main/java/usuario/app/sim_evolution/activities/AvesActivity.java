package usuario.app.sim_evolution.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.domain.AmbienteRepositorio;
import usuario.app.sim_evolution.domain.AveRepositorio;
import usuario.app.sim_evolution.domain.FenotipoRepositorio;
import usuario.app.sim_evolution.model.entities.Ave;
import usuario.app.sim_evolution.model.entities.Bico;
import usuario.app.sim_evolution.model.entities.Cor;
import usuario.app.sim_evolution.services.MusicService;


public class AvesActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ToggleButton btn_sound;
    ImageButton btn_help,btn_exit,btn_iniciar;

    ImageView img_4422, img_4421, img_4411,
            img_4322, img_4321, img_4311,
            img_3322, img_3321, img_3311;

    String titulo_msg, corpo_msg, tipoAmbiente;

    private final int LIMITE_AVES = 4;

    private List<Ave> avesTotais;
    private List<Ave> avesSelecionadas;

    LinearLayout l1;
    private ImageView img_ave_selecionada_1;
    private ImageView img_ave_selecionada_2;
    private ImageView img_ave_selecionada_3;
    private ImageView img_ave_selecionada_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        tipoAmbiente = intent.getStringExtra(CenariosActivity.EXTRA_MESSAGE);
        carregarTelaSelecaoAves();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void carregarTelaSelecaoAves() {

        setContentView(R.layout.activity_aves);
        l1 = (LinearLayout) findViewById(R.id.activity_aves);

        inicializarVariaveis();
        setListeners();

        //Josué: bloco comentado pq o BD já é montado em activity anterior (AptidaoActivity), logo não pode ser destruído aqui
        // Destrói o arquivo do BD para eliminar dados residuais de simulação anterior.
        // Desse modo, a simulação inicia-se em uma base limpa.
        //destroyDB(AvesActivity.this);

        //Troca o background conforme o cenário escolhido pelo usuário
        if(tipoAmbiente.equals("1")) //cenário "Floresta"
            l1.setBackgroundResource(R.drawable.bg4);

        if(tipoAmbiente.equals("2")) //cenário "Savana"
            l1.setBackgroundResource(R.drawable.bg5);

        if(tipoAmbiente.equals("3")) //cenário "Genérico"
            l1.setBackgroundResource(R.drawable.bg2);
    }

    private void inicializarVariaveis() {

        // Botões do topo
        btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
//        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_help = (ImageButton) findViewById(R.id.btn_help);

        // Botões execução da simulação
        btn_iniciar = (ImageButton) findViewById(R.id.btn_iniciar);

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

        // Caixinhas para exibição das aves selecionadas (limite de 4 aves)

        /* Caixinhas da 1a. fileira */
        img_ave_selecionada_1 = (ImageView) findViewById(R.id.img_ave_selecionada_1);
        img_ave_selecionada_2 = (ImageView) findViewById(R.id.img_ave_selecionada_2);

        /* Caixinhas da 2a. fileira */
        img_ave_selecionada_3 = (ImageView) findViewById(R.id.img_ave_selecionada_3);
        img_ave_selecionada_4 = (ImageView) findViewById(R.id.img_ave_selecionada_4);

        // Monta lista com todas as aves possíveis do ambiente
        avesTotais = AveRepositorio.criarAves();

        avesSelecionadas = new ArrayList<>();
        btn_sound.setChecked(!MusicService.isMute());
    }

    // Eventos de seleção das aves realizados pelo usuário
    private void setListeners(){

        btn_help.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialogAves();
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
                        stopService(new Intent(AvesActivity.this, MusicService.class));

                    }
                }
        );*/

        /* Eventos de seleção das opções de aves: ADIÇÃO de aves nas caixinhas */

        img_4422.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(0));
            }
        });

        img_4421.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(1));
            }
        });

        img_4411.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(2));
            }
        });

        img_4322.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(3));
            }
        });

        img_4321.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(4));
            }
        });

        img_4311.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(5));
            }
        });

        img_3322.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(6));
            }
        });

        img_3321.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(7));
            }
        });

        img_3311.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
                addAveSelecionada(avesTotais.get(8));
            }
        });

        /* Eventos relacionados às caixinhas de aves selecionadas: REMOÇÃO de aves selecionadas */

        /* Caixinhas da 1a. fileira */
        img_ave_selecionada_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerAveSelecionada(0);
            }
        });

        img_ave_selecionada_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerAveSelecionada(1);
            }
        });

        /* Caixinhas da 2a. fileira */
        img_ave_selecionada_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerAveSelecionada(2);
            }
        });

        img_ave_selecionada_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerAveSelecionada(3);
            }
        });

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicService.setMute(!isChecked);
            }
        });

    }

    // Adiciona a ave selecionada na lista "avesSelecionadas", que deverá conter no máximo 4 itens
    private void addAveSelecionada(Ave ave){

        if (avesSelecionadas.size() >= LIMITE_AVES) {
            Toast.makeText(this, "Limite é de 4 aves", Toast.LENGTH_SHORT).show();
            return;
        }

        avesSelecionadas.add(ave);
        atualizarListaAves();
    }

    // Retira a ave da caixinha selecionada
    private void removerAveSelecionada(int index){

        try {
            avesSelecionadas.remove(index);
            atualizarListaAves();
        } catch (IndexOutOfBoundsException e) {

        }
    }

    // Atualiza a a exibição das aves selecionadas nas caixinhas relacionadas
    private void atualizarListaAves() {

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.aves_selecionadas1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.aves_selecionadas2);

        //"Esvazia" as caixinhas (ação preventiva), para preenchê-las no próximo "for" com as aves selecionadas
        for (int i = 0; i < LIMITE_AVES; i++) {

            if(i < 2) {
                ImageView imageView = (ImageView) linearLayout1.getChildAt(i);
                imageView.setImageBitmap(null);
            } else {
                ImageView imageView = (ImageView) linearLayout2.getChildAt(i-2);
                imageView.setImageBitmap(null);
            }
        }

        //Preenche as caixinhas com as imagens das aves selecionadas
        for (int i = 0; i < avesSelecionadas.size(); i++) {

            if(i < 2) {
                ImageView imageView = (ImageView) linearLayout1.getChildAt(i);
                Ave ave = avesSelecionadas.get(i);
                imageView.setImageResource(ave.getImageID());
            } else {
                ImageView imageView = (ImageView) linearLayout2.getChildAt(i-2);
                Ave ave = avesSelecionadas.get(i);
                imageView.setImageResource(ave.getImageID());
            }

        }
    }

    // Invoca a simulação com base nas aves selecionadas pelo usuário
    public void callSimulacaoActivity(View view) {

        if (avesSelecionadas.size() > 1) {

            // Persiste no BD os dados de ambiente atual, fenótipos relacionados e aves selecionadas antes de iniciar a simulação
            persistirDados();

            Intent intent = new Intent(this,SimulacaoActivity.class);
            intent.putExtra(EXTRA_MESSAGE,"0" + "," + tipoAmbiente); //Envia o tipoAmbiente
            startActivity(intent);
        }
        else {
            Toast.makeText(AvesActivity.this,"Ops! Você precisa selecionar pelo menos duas (2) aves para a simulação.",Toast.LENGTH_SHORT).show();
        }

    }

    // Insere as aves selecionadas no BD
    public void persistirDados() {

        //Registra no BD as definições do ambiente selecionado
        AmbienteRepositorio.setarAmbiente(this,Integer.parseInt(tipoAmbiente));

        //Registra no BD as aptidões dos fenótipos com base no ambiente selecionado
        FenotipoRepositorio.setarAptidoesFenotiposAmbiente(this,Integer.parseInt(tipoAmbiente));

        //Limpa a tabela tbAveEvento antes de repopulá-la
        AveRepositorio.deletaAveEvento(this);

        Cor cor = null;
        Bico bico = null;

        //Atualiza as aves selecionadas com os fenótipos corretos e registra os dados no BD
        for (Ave a : avesSelecionadas){

            //Recupera os valores de aptidão dos fenótipos e atualiza o objeto ave antes de sua inserção no BD
            cor = (Cor)FenotipoRepositorio.getFenotipoAptidao(this,a.getCor().getTipo(), a.getCor().getVariacao());
            bico = (Bico)FenotipoRepositorio.getFenotipoAptidao(this,a.getBico().getTipo(),a.getBico().getVariacao());

            a.setValorAptidaoCor(cor.getValorAptidao());
            a.setValorAptidaoBico(bico.getValorAptidao());

            AveRepositorio.inserirEventoAve(this,1,a); // 1 (tempoEvento)
        }

    }

    private void alertDialogAves() {

        // LayoutInflater é utilizado para inflar o layout em uma view
        LayoutInflater li = getLayoutInflater();

        // Infla o layout alert_aves.xml na view
        View view = li.inflate(R.layout.alert_aves,null);

        // Monta o AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Seleção de Aves")
                .setView(view)
                .setNeutralButton("OK", null)
                .create();

        dialog.show();

    }

}
