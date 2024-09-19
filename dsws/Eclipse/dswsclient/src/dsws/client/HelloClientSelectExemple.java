package dsws.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import br.gpca.survey.Survey;
import br.gpca.survey.SurveyJava;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Scanner;

public class HelloClientSelectExemple {
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		Survey survey = SurveyJava.createSurvey();
        survey.addItem("2", "Player_Points", "300");
        survey.addItem("2", "Computer_Points", "270");
        survey.addItem("2", "Computer_Algorithm", "RandomPlayer");
        survey.appName = "br.gpca.hanafuda.android";
        survey.appVersion = "2.0.0";
        survey.androidVersion = "24(7.0)";
        survey.androidlanguage = "por";
        survey.deviceInfo = "teste";
        survey.userEmail = "teste@gmail.com";
        survey.userName = "teste@gmail.com";
        survey.day = 23;
        survey.month = 8;
        survey.year = 2017;
        survey.hour = 21;
        survey.minute = 30;
        
        String json = Survey.convert(survey); 

        String url = (false) ? "http://eic.cefet-rj.br/app/dsws/ranking.spring": "http://localhost:8080/dsws/ranking.spring";
        url += "?json="+ URLEncoder.encode(json);
       System.out.println(url);
        
        
        String retorno = null;
        
        try 
		{
			CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            
            InputStream content = response1.getEntity().getContent();
            retorno = new Scanner(content,"UTF-8").useDelimiter("\\A").next();
            
            String value = "" + response1.getStatusLine();
		    String position = retorno.substring(47);
		    System.out.println("A posicao do usuario "+ survey.userName + " e: " + position);
		    response1.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        
	}
}
