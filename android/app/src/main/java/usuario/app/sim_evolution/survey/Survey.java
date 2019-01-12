package usuario.app.sim_evolution.survey;

/**
 * Created by eogasawara on 13/03/15.
 */

import com.google.gson.Gson;

import java.util.ArrayList;

public class Survey {

    public String appName;
    public String appVersion;

    // Josue Cardoso (20/02/2017): atributos comentados e renomeados (por precaução) em razão das políticas de privacidade e segurança do Google
    // (https://play.google.com/intl/pt-BR_ALL/about/privacy-security/personal-sensitive/)
    /*
    public String androidVersion;
    public String androidlanguage;
    public String deviceInfo;
    public String userName;
    public String userEmail;
    */
    public String param3;
    public String param4;
    public String param5;
    public String param6;
    public String param7;

    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;
    public ArrayList<Item> itemList = new ArrayList<Item>();

    public void addItem(String key, String att, String val) {
        Item item = new Item();
        item.key = key;
        item.prop = att;
        item.value = val;
        itemList.add(item);
    }

    public static String convert(Survey survey) {
        Gson gson = new Gson();
        String json = gson.toJson(survey);
        return json;
    }


}
