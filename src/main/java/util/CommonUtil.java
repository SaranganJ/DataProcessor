package util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtil {
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isString(String name) {
        String new_name = name.replaceAll("[^a-zA-Z0-9]", " ");
        char[] chars = new_name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }


    public static boolean checkSubstring(String value, List<String> myArrayList ) {
        for(String listItem : myArrayList){
            if(value.contains(listItem)){
                return true;
            }
        }
        return false;
    }

    public static boolean isStringContainNumber(String value) {
        String numbers = "0123456789";
        for (String s : numbers.split("")) {
            if (value.contains(s)) {
                return true;
            }
        }
        return false;
    }


    public static String removeSpecialChar(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String formatDate(String date) {
        StringBuilder dobBuilder = new StringBuilder();
        if (date != null && date.length() > 0) {
            date = removeSpecialChar(date);
            if (date.length() == 8) {
                dobBuilder.append(date, 0,2).append("/")
                        .append(date, 2,4).append("/").append(date, 4, 8);
                return dobBuilder.toString();
            } else {
                dobBuilder.append(date, 0,1).append("/")
                        .append(date, 1,3).append("/").append(date, 3, 7);
                return dobBuilder.toString();
            }
        }
        return "";
    }
    public static boolean hasDay(String word) {
        boolean foundMatch = false;
        Pattern regex = Pattern.compile("/([a-zA-Z0-9]|\\s*)([Dd][a-zA-Z][Yy])([0-9]|\\s*)/");
        Matcher regexMatcher = regex.matcher(word);
        foundMatch = regexMatcher.find();
        return  word.contains("ds") || word.contains("da") || word.contains("DA") || word.contains("dA") || word.contains("daY") || word.contains("dey");
    }

    public static String getValidDay(String word) {
//        if (!hasDay(word)) {
//            return "";
//        }
        if (word.contains("M") || word.contains("m") || word.contains("o") || word.contains("O")) {
            return "Monday";
        } else if ((word.contains("T") || word.contains("t")) && ((word.contains("e") || word.contains("E")))) {
            return "Tuesday";
        } else if (word.contains("w") || word.contains("W")) {
            return "Wednesday";
        } else if ((word.contains("T") || word.contains("t")) && (word.contains("h") || word.contains("H"))) {
            return "Thursday";
        } else if (word.contains("f") || word.contains("F")) {
            return "Friday";
        } else if ("s".equalsIgnoreCase(word.substring(0,1)) && (word.contains("t") || word.contains("T"))) {
            return "Saturday";
        } else if ((word.contains("n") || word.contains("N")) && (word.contains("u") || word.contains("U"))) {
            return "Sunday";
        }
        return "";
    }
}
