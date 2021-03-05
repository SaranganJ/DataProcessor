package util.validations;

import util.CommonUtil;

import java.util.List;

//FIXME :: UNUSED
public class CardIssuerDateValidationUtil {
    public static int validate(int wordsCount, List<String> entries, String[] words) {
        if (CommonUtil.isStringContainNumber(words[wordsCount])) {
            if (countOfNumber(words[wordsCount]) == 8) {
                String dayAndDate = words[wordsCount];
                String finalWord = processDayDate(dayAndDate);
                System.out.println("8 : " + finalWord);
                entries.add(finalWord);
                return wordsCount + 1;
            } else {
                String dayAndDate = words[wordsCount] + words[wordsCount + 1];
                String finalWord = processDayDate(dayAndDate);
                System.out.println("8 : " + finalWord);
                entries.add(finalWord);
                return wordsCount + 2;
            }
        } else {
            String day = CommonUtil.getValidDay(words[wordsCount]);
            String date = CommonUtil.formatDate(words[wordsCount + 1]);
            String finalWord = day + "," + date;
            System.out.println("8 : " + finalWord);
            entries.add(finalWord);
            return wordsCount + 2;
        }
    }

    private static int countOfNumber(String word) {
        int count = 0;
        for (String letter : word.split("")) {
            if (CommonUtil.isInteger(letter)) {
                count++;
            }
        }
        return count;
    }

    private static String processDayDate(String word) {
        String day = "";
        String date = "";
        if (CommonUtil.hasDay(word)) {
            if (word.contains(",")) {
                String[] wordArray = word.split(",");
                day = CommonUtil.getValidDay(wordArray[0]);
                date = CommonUtil.formatDate(wordArray[1]);
            } else {
                int firstNumberIndex = getFirstNumberIndex(word);
                day = CommonUtil.getValidDay(word.substring(0, firstNumberIndex));
                date = CommonUtil.formatDate(word.substring(firstNumberIndex));
            }
        }
        return day + "," + date;
    }

    private static int getFirstNumberIndex(String word) {
        if (word != null && word.length() > 0) {
            for (String letter : word.split("")) {
                if (CommonUtil.isInteger(letter)) {
                    return word.indexOf(letter);
                }
            }
        }
        return word.length() - 1;
    }
}
