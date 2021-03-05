package copypaste.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.List;

public class ConverterUtil {

    public static String convertHTMLtoJson(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.select("body").first();
        //String arrayName = table.select("th").first().text();
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
       // Elements elements = body.getElementsByAttribute("body");
        System.out.println(body.getAllElements().get(0));
        JSONObject jo = new JSONObject();
        /*for (int i = 0, l = body.size(); i < l; i++) {
            String key = elements.get(i).text();
            //jo.put(key, value);
        }*/
        jsonArr.put(jo);
      //  jsonObj.put(arrayName, jsonArr);
        return (jsonObj.toString());
    }

    public static String convertToNonHTMLElement(List<String> textsWithHTMLElements) {
        StringBuilder processedStr = new StringBuilder();
        if (textsWithHTMLElements != null && !textsWithHTMLElements.isEmpty()) {
            boolean isAddable = false;
            for (String letter : textsWithHTMLElements) {
                if ("<".equals(letter)) {
                    isAddable = false;
                }
                if (isAddable) {
                    processedStr.append(letter);
                }
                if (">".equals(letter)) {
                    isAddable = true;
                }
            }
        }
        return processedStr.toString();
    }



}
