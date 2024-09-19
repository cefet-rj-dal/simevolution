package br.gpca.survey;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SurveyJava {

    public static Survey createSurvey() {
        Survey survey = new Survey();
        try {
            survey.appName = System.getProperty("sun.java.command");
            survey.androidlanguage = Locale.getDefault().getISO3Language();

            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            survey.day = cal.get(Calendar.DAY_OF_MONTH);
            survey.month = cal.get(Calendar.MONTH) + 1;
            survey.year = cal.get(Calendar.YEAR);

            survey.hour = cal.get(Calendar.HOUR_OF_DAY);
            survey.minute = cal.get(Calendar.MINUTE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return survey;
    }

}