package dsws.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import br.gpca.survey.Survey;
import br.gpca.survey.SurveyJava;

import java.net.URLEncoder;

public class HelloClientWriteExample 
{
	
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

        String url = (false) ? "http://eic.cefet-rj.br/app/dsws/survey.spring": "http://localhost:8080/dsws/survey.spring";
        url += "?json="+ URLEncoder.encode(json);
        System.out.println(url);
		try 
		{
			CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            String value = "" + response1.getStatusLine();
		    System.out.println("Status da Resposta HTTP:" + value);
		    System.out.println();
		    
		    response1.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}