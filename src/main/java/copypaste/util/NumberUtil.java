package copypaste.util;

import java.util.Arrays;
import java.util.List;

public class NumberUtil {
    public static String getPhoneNumber(String string) {
        String phoneNumber = "";
        if (string != null) {
            int index = 0;
            while (isACharOfValidPhoneNumber(string.charAt(index))) {
                phoneNumber += String.valueOf(string.charAt(index));
                index ++;
            }
        }
        return phoneNumber.replaceAll("\\s+", "");
    }

    private static boolean isACharOfValidPhoneNumber(char character) {
        String strChar = String.valueOf(character);
        return strChar.matches("[^A-Za-z]");
        /*return " ".equals(strChar) || "+".equals(strChar) || strChar.matches("[-+]?\\d*\\.?\\d+")
                || ",".equals(strChar) || "-";*/
    }


    public static String getEmailAddress(String string) {
        List<String> strings = Arrays.asList(string.split(" "));
        for (String s : strings) {
            if (s.matches("^(.+)@(.+)$")) {
                return s;
            }
        }
        return "";
    }
}
