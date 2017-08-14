package Utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class YMLCreator {
    private static PrintWriter pw;
    private static final String
        shopName = "shopName",
        companyName = "compName",
        compURL = "http://best.seller.ru";

    public YMLCreator(String fileName){
        try {
            pw = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Create(String json){
        StringBuilder sb = new StringBuilder();
        sb.append(new XMLTag("?xml version=" + new Quote("1.0") +
                " encoding=" + new Quote("UTF-8")+ "?").toString());
        printLine(0, sb.toString());
        sb = new StringBuilder();

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        sb.append(new XMLTag("yml_catalog date=" + new Quote(strDate)));
        printLine(0, sb.toString());

        printLine(1, new XMLTag("shop").toString());

        printLine(1, new DoubleXMLTag("name", shopName).toString());

        printLine(1, new DoubleXMLTag("company", companyName).toString());

        printLine(1, new DoubleXMLTag("url", compURL).toString());

        printLine(1, new XMLTag("currencies").toString());

        sb = new StringBuilder();
        sb.append(XMLTag.left).append("currency id=").append(new Quote("RUR"))
                .append(" rate=").append(new Quote("1"))
                .append("/").append(XMLTag.right);
        printLine(2, sb.toString());

        printLine(1, new XMLTag("/currencies").toString());

        printLine(1, new XMLTag("categories").toString());

        sb = new StringBuilder();
        sb.append(XMLTag.left).append("category id=").append(new Quote("1"))
                .append(XMLTag.right);
        sb.append("Запчасти и детали");
        sb.append(new XMLTag("/category"));
        printLine(2, sb.toString());

        printLine(1, new XMLTag("/categories").toString());

        printLine(1, new XMLTag("offers").toString());

        printOffers(json);

        printLine(1, new XMLTag("/offers").toString());

        printLine(1, new XMLTag("/shop").toString());

        printLine(0, new XMLTag("/yml_catalog").toString());

        pw.flush();
        pw.close();
    }

    private void printOffers(String json) {
        JsonParser jp = new JsonParser();
        JsonElement element = jp.parse(json);
        JsonObject object = element.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();//will return members of your object
        for (Map.Entry<String, JsonElement> entry: entries) {
            JsonObject gGroup = entry.getValue().getAsJsonObject();
            //TODO: zaglushka pic?
            String img_link = null;
            try {
                img_link = gGroup.get("brand_img").toString();
            } catch (NullPointerException e) {
                if (img_link.isEmpty())
                    img_link = "default_link.pnh";
            }
            JsonArray items = gGroup.get("items").getAsJsonArray();
            collectItems(img_link.replaceAll("\"",""), items);
        }
    }


    private void collectItems(String img_link, JsonArray items) {
        for (JsonElement element: items) {
            JsonObject obj = element.getAsJsonObject();
            String price = obj.get("price").toString().replaceAll(" ", "");
            String id = obj.get("id").toString().replaceAll(" ", "");
            String certName = obj.get("name").toString().replaceAll("\"","");
            String days_avg = obj.get("days_avg").toString().replaceAll("\"","");
            String days_max = obj.get("days_max").toString().replaceAll("\"","");
            String code = obj.get("code").toString().replaceAll("\"","");
            String brand = obj.get("chname").toString().replaceAll("\"","");

            StringBuilder sb = new StringBuilder();
            sb.append(XMLTag.left).append("offer id=").append(new Quote(id).toString())
                    .append(" available=").append(new Quote("true").toString());
            sb.append(XMLTag.right);

            printLine(2,sb.toString());

            printLine(3, new DoubleXMLTag("url","tut url na tovar na str saita").toString());

            printLine(3, new DoubleXMLTag("price", price).toString());

            printLine(3, new DoubleXMLTag("categoryId", "1").toString());

            printLine(3, new DoubleXMLTag("currencyId", "RUR").toString());

            printLine(3, new DoubleXMLTag("picture", img_link).toString());

            printLine(3, new DoubleXMLTag("store", "true").toString());

            printLine(3, new DoubleXMLTag("delivery", "true").toString());

            printLine(3, new DoubleXMLTag("vendor", brand).toString());

            printLine(3, new DoubleXMLTag("name", certName).toString());

            //TODO: delivery options
            printLine(2, new XMLTag("/offer").toString());
        }
    }

    //level -- tabs count
    //line -- line to print
    private void printLine(int level, String line) {
        for (int i = 0; i < level; i++) {
            pw.print("\t");
        }
        pw.println(line);
    }
}
