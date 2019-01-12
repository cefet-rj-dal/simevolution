package usuario.app.sim_evolution.survey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eogasawara on 14/03/15.
 */
public class SurveyAndroid {

    public static Survey createSurvey(Activity act) {

        Survey survey = new Survey();

        try {
            survey.appName = act.getApplicationContext().getPackageName();
            PackageInfo pInfo = act.getPackageManager().getPackageInfo(survey.appName, 0);
            survey.appVersion = pInfo.versionName;

            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            survey.day = cal.get(Calendar.DAY_OF_MONTH);
            survey.month = cal.get(Calendar.MONTH) + 1;
            survey.year = cal.get(Calendar.YEAR);

            survey.hour = cal.get(Calendar.HOUR_OF_DAY);
            survey.minute = cal.get(Calendar.MINUTE);

            // Josue Cardoso (20/02/2017): atributos comentados e com valor nulo     em razão das políticas de privacidade e segurança do Google
            // (https://play.google.com/intl/pt-BR_ALL/about/privacy-security/personal-sensitive/)

            //String release = Build.VERSION.RELEASE;
            //int sdkVersion = Build.VERSION.SDK_INT;
            //survey.androidVersion = sdkVersion + " (" + release + ")";
            survey.param3 = null;

            //survey.androidlanguage = Locale.getDefault().getISO3Language();
            survey.param4 = null;

            //survey.deviceInfo = Build.MODEL + " (" + Build.PRODUCT + ")";
            survey.param5 = null;

            /*try {
                AccountManager manager = (AccountManager) act.getSystemService(act.ACCOUNT_SERVICE);
                Account[] list = manager.getAccounts();
                if (list.length > 0) {
                    survey.userEmail = list[0].name;
                    survey.userName = list[0].name;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }*/

            survey.param6 = null;
            survey.param7 = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return survey;
    }

}

