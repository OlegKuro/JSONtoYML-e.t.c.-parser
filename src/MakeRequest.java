import com.google.gson.Gson;
import org.boon.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MakeRequest {
    private static final String
            USER = "00119867",
            PASSWORD = "d2mJF108",
            SEARCH = "searchDo2",
            LINK = "http://the-parts.ru/cabinet/ws/ws.json.php";
    private String ans = null;
    public MakeRequest(String art, String TOKEN) throws UnsupportedEncodingException{

        ArrayList<Pair<String, String>> map = new ArrayList<>();
        map.add(new Pair<>("user", USER));
        map.add(new Pair<>("password", PASSWORD));
        StringBuilder s = new StringBuilder();
        s.append("{\"action\": \"searchDo2\", \"params\": [\"").append(URLEncoder.encode(art, "UTF-8")).append("\"");
        s.append(", { \"noreplace\" : true,")
                .append("\"present\" : true,")
                .append("\"original\" : true")
                .append("}").append("]}");
        System.out.println("Making request: "+ s);
        map.add(new Pair<>("request", s.toString()));
        String resp = HTTPSend.performPostCall(LINK, map);
        resp = Utils.PrettyJson.convert(resp);
        try {
            PrintWriter pw = new PrintWriter(new File("out.json"));
            pw.print(resp);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ans = resp;
    }

    public String getAns(){
        return this.ans;
    }
}
