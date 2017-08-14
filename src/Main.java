import Models.Item;
import Utils.XLSXUtil;
import Utils.YMLCreator;
import org.boon.Pair;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    private static final String XLSXFoldName = "xlsx input files";

    public static void main(String[] args) throws FileNotFoundException {
        YMLCreator yml = new YMLCreator("test.yml");
        Scanner sc = new Scanner(new File("out.json"));
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.next());
        }
        yml.Create(sb.toString());

        List<String> inputFiles = new LinkedList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(XLSXFoldName))) {
            Object[] temp = paths.toArray();
            for (Object object : temp) {
                String s = object.toString().replaceAll(XLSXFoldName, "");
                if (!s.endsWith("xlsx"))
                    continue;
                if (!s.isEmpty()) {
                    inputFiles.add(s.substring(1));
                }
            }

            for (String n : inputFiles) {
                List<Item> items = XLSXUtil.workWithFile(XLSXFoldName + "/" + n);

                PrintWriter pw = new PrintWriter(new File("debug.txt"));
                for (Item item : items) {
                    pw.println(item.toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            } else {
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
        for (Pair<String, String> entry : params) {
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
