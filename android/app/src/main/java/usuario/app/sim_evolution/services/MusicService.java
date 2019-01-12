package usuario.app.sim_evolution.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import usuario.app.sim_evolution.R;
import usuario.app.sim_evolution.enums.Musicas;

/**
 * Created by Luana e Diego on 27/12/2016.
 */

public class MusicService extends IntentService {

    private static MediaPlayer musicaPrincipal;
    private static boolean mute = false;
    private static List<MediaPlayer> listaSons = new ArrayList<>();
    private static MediaPlayer musicaAtual;
    private static MediaPlayer musicaSimulacao;
    private static MediaPlayer somPredacao;

    public MusicService(){
        super("MusicService");


    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicaPrincipal = MediaPlayer.create(this, R.raw.menu);
        musicaSimulacao = MediaPlayer.create(this, R.raw.simulacao);
        somPredacao = MediaPlayer.create(this, R.raw.predador);

        musicaPrincipal.setLooping(true);
        musicaSimulacao.setLooping(true);

        listaSons.add(somPredacao);

        setMusicaAtual(Musicas.PRINCIPAL);
        tocarMusicaAtual();
    }

    @Override
    public void onDestroy() {
        musicaPrincipal.stop();
        //musicaPrincipal.release();
        musicaSimulacao.stop();
        //musicaSimulacao.release();
        somPredacao.stop();
        //somPredacao.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static void tocarMusicaAtual(){
        if (musicaAtual != null && !isMute() && !musicaAtual.isPlaying())
            musicaAtual.start();
    }

    public static void pausarMusicaAtual(){
        if (musicaAtual != null && musicaAtual.isPlaying())
            musicaAtual.pause();
    }

    public static boolean isMute() {
        return mute;
    }

    public static void setMute(boolean mute) {
        MusicService.mute = mute;

        if (mute)
            pauseAll();
        else
            tocarMusicaAtual();
    }

    private static void pauseAll() {
        musicaAtual.pause();

        for (MediaPlayer player : listaSons) {
            player.pause();
        }
    }

    public static void setMusicaAtual(Musicas musica){

        if (musicaAtual != null)
            musicaAtual.pause();

        if (musica == Musicas.PRINCIPAL)
            musicaAtual = musicaPrincipal;
        else if (musica == Musicas.SIMULACAO)
            musicaAtual = musicaSimulacao;
    }

    public static void tocarSomPredacao(){
        if (!isMute())
            somPredacao.start();
    }


}
