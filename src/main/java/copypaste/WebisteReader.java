package copypaste;

import copypaste.logic.Logic;
import copypaste.util.ConverterUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebisteReader {

    public static void main(String[] args) throws IOException {
        List<String> urls = Arrays.asList("http://www.selbyhardware.com");
        /*List<String> urls = Arrays.asList("https://everesthouse.lk/", "https://woodlands-ice-cream.co.uk/contact/","https://pierres.com/contact/contact-us/", "https://ssbponetwork.com/", "https://bnshardware.lk/en/",
                "https://www.visioncare.lk/");*/

        for (String newUrl : urls) {
            System.out.println(newUrl);
            URL url;
            try {
                url = new URL(newUrl);
                URLConnection con = url.openConnection();
                InputStream in = con.getInputStream();
                String encoding = con.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                String body = IOUtils.toString(in, encoding);

                String processedStr = ConverterUtil.convertToNonHTMLElement(new ArrayList<>
                        (Arrays.asList(body.split(""))));

                processedStr = processedStr.replaceAll("\"", " ");
                processedStr = processedStr.replaceAll(":", " ");
                processedStr = processedStr.replaceAll("}", " ");
                processedStr = processedStr.replaceAll("\\s+", " ");

                System.out.println("Phone Number : " + Logic.getPhoneNumbers(processedStr));
                System.out.println("Fax : " + Logic.getFaxNumber(processedStr));
                System.out.println("Email : " + Logic.getEmail(processedStr));
                //List<String> strings = new ArrayList<>(Arrays.asList(processedStr.split(" ")));
                //System.out.println(strings);
            /*System.out.println(ConverterUtil.convertToNonHTMLElement(new ArrayList<>
                    (Arrays.asList(body.split("<")))));*/
                //System.out.println(ConverterUtil.convertHTMLtoJson(body));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("\n");
        }

    }


}
