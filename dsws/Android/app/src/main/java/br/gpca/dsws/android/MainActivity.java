package br.gpca.dsws.android;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URLEncoder;

import br.gpca.survey.Survey;
import br.gpca.survey.SurveyAndroid;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonOnClick(View v) {
        if (v != null) {
            Button btn = (Button) v;
            btn.setText("Oi");

            Survey survey = SurveyAndroid.createSurvey(this);
            survey.addItem("1", "attA", "valueA1");
            survey.addItem("1", "attB", "valueB1");
            survey.addItem("2", "attA", "valueA2");
            survey.addItem("2", "attB", "valueB2");
            String json = Survey.convert(survey);
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                CloseableHttpClient httpclient = HttpClients.createDefault();

                String url = "http://eic.cefet-rj.br/app/dsws/survey.spring?json=" + URLEncoder.encode(json);
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response1 = httpclient.execute(httpGet);
                String value = "" + response1.getStatusLine();
                btn.setText(value);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
