import Utils.YMLCreator;
import org.boon.Pair;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
public class HTTPSend {
    private static String TOKEN = null;

    public static void main(String[] args) throws UnsupportedEncodingException {
        Authorization authorization = new Authorization();
        TOKEN = authorization.TOKEN;
        System.out.println("TOKEN:" + TOKEN);
        /*that's how we make requests*/
        MakeRequest mr = new MakeRequest("01080", TOKEN);
        System.out.println(mr.getAns());
        if (!mr.getAns().equals("[]")) {
            YMLCreator yml = new YMLCreator("test.yml");
            yml.Create(mr.getAns());
        }
        mr = null;
    }

    public static String performPostCall(String requestURL,
                                         ArrayList<Pair<String, String>> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(ArrayList<Pair<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Pair<String,String> entry : params){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
