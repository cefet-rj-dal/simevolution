package usuario.app.sim_evolution.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.services.MusicService;

public class PrincipalActivity extends AppCompatActivity {

    ToggleButton btn_sound;
    ImageButton btn_exit, btn_entrar, btn_sobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregarTelaPrincipal();

        Intent svc = new Intent(this, MusicService.class);
        startService(svc);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (btn_sound != null)
            btn_sound.setChecked(!MusicService.isMute());
    }

    public void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }

    public void carregarTelaPrincipal() {

        setContentView(R.layout.activity_principal);

        inicializarVariaveis();
        setListeners();

    }

    private void inicializarVariaveis() {
        btn_entrar = (ImageButton) findViewById(R.id.btn_entrar);
        btn_sobre = (ImageButton) findViewById(R.id.btn_sobre);
        btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
//        btn_exit = (ImageButton) findViewById(R.id.btn_exit);
        btn_sound.setChecked(!MusicService.isMute());
    }

    private void setListeners(){

        btn_sobre.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialogSobre();

                    }
                }
        );

        //Comentado pois para aplicações Android, não faz sentido o botão de fechar. Por esta razão, ocorre uma cadeia de erros
       /* btn_exit.setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        finish();
                        //System.exit(0);

                        moveTaskToBack(true);
                        stopService(new Intent(PrincipalActivity.this, MusicService.class));

                    }
                }
        );*/

        btn_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MusicService.setMute(!isChecked);
            }
        });

    }

    public void callTipoSimulacaoActivity(View view) {
        Intent intent = new Intent(this,TipoSimulacaoActivity.class);
        startActivity(intent);
    }

    private void alertDialogSobre() {

        // LayoutInflater é utilizado para inflar o layout em uma view
        LayoutInflater li = getLayoutInflater();

        // Infla o layout alert_aves.xml na view
        View view = li.inflate(R.layout.alert_sobre,null);

        // Monta o AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sobre o Aplicativo")
                .setView(view)
                .setNeutralButton("OK", null)
                .create();

        dialog.show();

    }
}
