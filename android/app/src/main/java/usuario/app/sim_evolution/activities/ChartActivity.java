package usuario.app.sim_evolution.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.domain.AveRepositorio;
import usuario.app.sim_evolution.services.ExibirMensagemService;
import usuario.app.sim_evolution.services.MusicService;

public class ChartActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "usuario.app.sim_evolution.MESSAGE";

    ImageButton btn_help; //,btn_redo, btn_stop, btn_exit, btn_sound;

    String titulo_msg, corpo_msg;

    LineChart mChart;
    String[] params;
    String tempo;
    String tipoAmbiente;
    String intervaloCruzamento;
    String intervaloPredacao;
    String intervaloAlimentacao;

    RelativeLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart);
        l1 = (RelativeLayout) findViewById(R.id.activity_chart);

        Intent intent = getIntent();
        params = intent.getStringExtra(SimulacaoActivity.EXTRA_MESSAGE).split(",");
        tempo = params[0];
        tipoAmbiente = params[1];
        intervaloCruzamento = params[2];
        intervaloPredacao = params[3];
        intervaloAlimentacao = params[4];

        carregarTelaGrafico();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //callSimulacaoActivity(l1);
    }

    public void carregarTelaGrafico() {

        //Troca o background conforme o cenário escolhido pelo usuário
        if(tipoAmbiente.equals("1")) //cenário "Floresta"
        {
            l1.setBackgroundResource(R.drawable.bg7);
        }
        else
        {
            if(tipoAmbiente.equals("2")) //cenário "Savana"
            {
                l1.setBackgroundResource(R.drawable.bg8);
            }
            else
            {
                l1.setBackgroundResource(R.drawable.bg10);
            }
        }

        mChart = (LineChart) findViewById(R.id.lineChart);

        inicializarVariaveis();
        setListeners();

        // enable / disable grid background
        mChart.setDrawGridBackground(false);

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);

        // add data
        setData();

        mChart.animateXY(3000, 3000);
        //mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setEnabled(false);

        // no description text
        Description description = new Description();
        description.setText("QTD. AVES x TEMPO");
        mChart.setDescription(description);
        mChart.setNoDataText("Não há dados para exibição.");

        //  dont forget to refresh the drawing
        mChart.invalidate();

    }

    private void inicializarVariaveis() {
        //btn_sound = (ImageButton) findViewById(R.id.btn_sound);
        btn_help = (ImageButton) findViewById(R.id.btn_help);
        //btn_redo = (ImageButton) findViewById(R.id.btn_redo);
        //btn_stop = (ImageButton) findViewById(R.id.btn_stop);
        //btn_exit = (ImageButton) findViewById(R.id.btn_exit);
    }

    private void setListeners(){

        btn_help.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        titulo_msg =    "Gráfico de Sobrevivência das Aves";
                        corpo_msg =     "O gráfico exibe a quantidade de AVES SOBREVIVENTES no ambiente x TEMPO. " +
                                        "Os dados são exibidos em intervalos de 5 unidades de tempo. " +
                                        "Cada variedade de ave é representada por uma linha com determinada cor. " +
                                        "Essa relação é exibida na legenda abaixo do gráfico. " +
                                        "Com base nas informações exibidas no gráfico você poderá identificar as aves com maior e menor aptidão de sobrevivência no ambiente escolhido.";

                        ExibirMensagemService.exibirAlertDialog(ChartActivity.this,titulo_msg,corpo_msg);

                    }
                }
        );

        /*btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish();

                    }
                }
        );*/


    }

    private void setData() {

        ArrayList<Entry> ValsAve1 = new ArrayList<>();
        ArrayList<Entry> ValsAve2 = new ArrayList<>();
        ArrayList<Entry> ValsAve3 = new ArrayList<>();
        ArrayList<Entry> ValsAve4 = new ArrayList<>();
        ArrayList<Entry> ValsAve5 = new ArrayList<>();
        ArrayList<Entry> ValsAve6 = new ArrayList<>();
        ArrayList<Entry> ValsAve7 = new ArrayList<>();
        ArrayList<Entry> ValsAve8 = new ArrayList<>();
        ArrayList<Entry> ValsAve9 = new ArrayList<>();

        int qtdIntervaloTempo = (Integer.parseInt(tempo)/5); // quantidade de intervalos de 5 tempos dentro do valor de "tempo" (é truncado como inteiro pq só interessa aqui  o valor à esquerda da vírgula)
        int fracaoTempo = (Integer.parseInt(tempo)%5); // fração de tempo além do(s) intervalo(s) cheio(s) de "qtdIntervaloTempo"

        int x = 0; // receberá os valores de tempo (eixo x)
        int y = 0; // receberá as quantidades de cada espécie (eixo y)
        int t = 5; // receberá os incrementos de intervalos, de 5 em 5. Inicializa com 5 pq este é o primeiro intervalo de tempo.

        ValsAve1.add(new Entry(x, y));
        ValsAve2.add(new Entry(x, y));
        ValsAve3.add(new Entry(x, y));
        ValsAve4.add(new Entry(x, y));
        ValsAve5.add(new Entry(x, y));
        ValsAve6.add(new Entry(x, y));
        ValsAve7.add(new Entry(x, y));
        ValsAve8.add(new Entry(x, y));
        ValsAve9.add(new Entry(x, y));

        if(qtdIntervaloTempo > 0) { // há mais de 1 intervalo de tempo

            for(int i = 0; i < qtdIntervaloTempo; i++) {

                LinkedHashMap<Integer,String> lstHistoricoQtdAvesSobreviventes = AveRepositorio.listaHistoricoQtdAvesSobreviventes(this,Integer.toString(t)); // lista a quantidade de aves no tempo t

                Iterator<Integer> keySetIterator = lstHistoricoQtdAvesSobreviventes.keySet().iterator();

                while(keySetIterator.hasNext()) {
                    Integer key = keySetIterator.next();

                    switch(key) {
                        case 1:
                            ValsAve1.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 2:
                            ValsAve2.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 3:
                            ValsAve3.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 4:
                            ValsAve4.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 5:
                            ValsAve5.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 6:
                            ValsAve6.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 7:
                            ValsAve7.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 8:
                            ValsAve8.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                        case 9:
                            ValsAve9.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                            break;
                    }

                }

                t += 5;
            }

        }

        if(fracaoTempo > 0 || qtdIntervaloTempo == 0) { // Há fração de tempo OU "tempo" é menor do que 5 (1 intervalo)

            t = Integer.parseInt(tempo);

            LinkedHashMap<Integer,String> lstHistoricoQtdAvesSobreviventes = AveRepositorio.listaHistoricoQtdAvesSobreviventes(this,tempo);

            Iterator<Integer> keySetIterator = lstHistoricoQtdAvesSobreviventes.keySet().iterator();

            while(keySetIterator.hasNext()) {
                Integer key = keySetIterator.next();

                switch(key) {
                    case 1:
                        ValsAve1.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 2:
                        ValsAve2.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 3:
                        ValsAve3.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 4:
                        ValsAve4.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 5:
                        ValsAve5.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 6:
                        ValsAve6.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 7:
                        ValsAve7.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 8:
                        ValsAve8.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                    case 9:
                        ValsAve9.add(new Entry(t,Integer.parseInt(lstHistoricoQtdAvesSobreviventes.get(key))));
                        break;
                }
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(ValsAve1, "Ave1");
        LineDataSet set2 = new LineDataSet(ValsAve2, "Ave2");
        LineDataSet set3 = new LineDataSet(ValsAve3, "Ave3");
        LineDataSet set4 = new LineDataSet(ValsAve4, "Ave4");
        LineDataSet set5 = new LineDataSet(ValsAve5, "Ave5");
        LineDataSet set6 = new LineDataSet(ValsAve6, "Ave6");
        LineDataSet set7 = new LineDataSet(ValsAve7, "Ave7");
        LineDataSet set8 = new LineDataSet(ValsAve8, "Ave8");
        LineDataSet set9 = new LineDataSet(ValsAve9, "Ave9");

        set1.setColor(Color.rgb(0,102,0)); // #006600
        set1.setCircleColor(Color.rgb(0,102,0));
        set1.setLineWidth(2f);
        set1.setCircleRadius(5f);
        set1.setFillAlpha(10);
        set1.setFillColor(Color.rgb(0,102,0));
        set1.setDrawCircles(false);
        set1.setCubicIntensity(8f);

        set2.setColor(Color.rgb(0,0,255)); // #0000FF
        set2.setCircleColor(Color.rgb(0,0,255));
        set2.setLineWidth(2f);
        set2.setCircleRadius(5f);
        set2.setFillAlpha(10);
        set2.setFillColor(Color.rgb(0,0,255));
        set2.setDrawCircles(false);

        set3.setColor(Color.rgb(102,0,204)); // #6600CC
        set3.setCircleColor(Color.rgb(102,0,204));
        set3.setLineWidth(2f);
        set3.setCircleRadius(5f);
        set3.setFillAlpha(10);
        set3.setFillColor(Color.rgb(102,0,204));
        set3.setDrawCircles(false);

        set4.setColor(Color.rgb(255,0,0)); // #FF0000
        set4.setCircleColor(Color.rgb(255,0,0));
        set4.setLineWidth(2f);
        set4.setCircleRadius(5f);
        set4.setFillAlpha(10);
        set4.setFillColor(Color.rgb(255,0,0));
        set4.setDrawCircles(false);

        set5.setColor(Color.rgb(255,153,0)); // #FF9900
        set5.setCircleColor(Color.rgb(255,153,0));
        set5.setLineWidth(2f);
        set5.setCircleRadius(5f);
        set5.setFillAlpha(10);
        set5.setFillColor(Color.rgb(255,153,0));
        set5.setDrawCircles(false);

        set6.setColor(Color.rgb(255,0,255)); // #FF00FF
        set6.setCircleColor(Color.rgb(255,0,255));
        set6.setLineWidth(2f);
        set6.setCircleRadius(5f);
        set6.setFillAlpha(10);
        set6.setFillColor(Color.rgb(255,0,255));
        set6.setDrawCircles(false);

        set7.setColor(Color.rgb(0,255,0)); // #00FF00
        set7.setCircleColor(Color.rgb(0,255,0));
        set7.setLineWidth(2f);
        set7.setCircleRadius(5f);
        set7.setFillAlpha(10);
        set7.setFillColor(Color.rgb(0,255,0));
        set7.setDrawCircles(false);

        set8.setColor(Color.rgb(255,255,0)); // #FFFF00
        set8.setCircleColor(Color.rgb(255,255,0));
        set8.setLineWidth(2f);
        set8.setCircleRadius(5f);
        set8.setFillAlpha(10);
        set8.setFillColor(Color.rgb(255,255,0));
        set8.setDrawCircles(false);

        set9.setColor(Color.rgb(0,255,255)); // #00FFFF
        set9.setCircleColor(Color.rgb(0,255,255));
        set9.setLineWidth(2f);
        set9.setCircleRadius(5f);
        set9.setFillAlpha(10);
        set9.setFillColor(Color.rgb(0,255,255));
        set9.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets
        dataSets.add(set3); // add the datasets
        dataSets.add(set4); // add the datasets
        dataSets.add(set5); // add the datasets
        dataSets.add(set6); // add the datasets
        dataSets.add(set7); // add the datasets
        dataSets.add(set8); // add the datasets
        dataSets.add(set9); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);
    }

    /*public void callPrincipalActivity(View view) {

        finish();
        System.exit(0);

        Intent intent = new Intent(this,PrincipalActivity.class);
        //intent.putExtra(EXTRA_MESSAGE,txtContador.getText());
        startActivity(intent);
    }*/

    public void callSimulacaoActivity(View view) {

        Intent intent = new Intent(this,SimulacaoActivity.class);
        intent.putExtra(EXTRA_MESSAGE,tempo + "," + tipoAmbiente + "," + intervaloCruzamento + "," + intervaloPredacao + "," + intervaloAlimentacao); //Retorna variáveis com valores correntes
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        MusicService.tocarMusicaAtual();
    }
}
