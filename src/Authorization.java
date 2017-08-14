import org.boon.Pair;

import java.util.ArrayList;

public class Authorization {
    private static final String
            USER = "00119867",
            PASSWORD = "d2mJF108",
            SEARCH = "searchDo2",
            LINK = "http://the-parts.ru/cabinet/ws/ws.json.php";
    public String TOKEN = null;

    ArrayList<Pair<String, String>> map = new ArrayList<Pair<String, String>>();
    public Authorization() {
        map.add(new Pair<>("user", USER));
        map.add(new Pair<>("password", PASSWORD));
        String s = "{\"action\": \"authorize\"}";
        map.add(new Pair<>("request", s));
        TOKEN = HTTPSend.performPostCall(LINK, map);
        TOKEN = TOKEN.substring(1,TOKEN.length() - 1);
        System.out.println(TOKEN);
    }
}
