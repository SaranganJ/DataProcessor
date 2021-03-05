package util.validations;

import util.CommonUtil;

import java.util.List;

public class DateValidationUtil {
    public static int validate(int wordsCount, List<String> entries, String[] words, String index) {
        if (CommonUtil.isStringContainNumber(words[wordsCount])) {
            if (countOfNumber(words[wordsCount]) == 8) {
                String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "");
                String finalWord = processDayDate(dayAndDate);
                System.out.println(index + " : " + finalWord);
                entries.add(finalWord);
                return wordsCount + 1;
            } else {
                String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "") + words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", "");
                String finalWord = processDayDate(dayAndDate);
                System.out.println(index + " : " + finalWord);
                entries.add(finalWord);
                return wordsCount + 2;
            }
        } else {
            if (CommonUtil.isString(words[wordsCount].replaceAll("[^a-zA-Z0-9]", "") + words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", ""))) {

                if (CommonUtil.isInteger(words[wordsCount+3].replaceAll("[^a-zA-Z0-9]", ""))) {
                    String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "")+
                            words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", "") +
                            words[wordsCount+2].replaceAll("[^a-zA-Z0-9]", "")
                            + words[wordsCount+3].replaceAll("[^a-zA-Z0-9]", "");
                    String[] dayAndDateArray = dayAndDate.split("(?<=\\D)(?=\\d)");
                    if (dayAndDateArray[1].length() ==8) {
                        String finalWord = processDayDate(dayAndDate);
                        System.out.println(index + " : " + finalWord);
                        entries.add(finalWord);
                        return wordsCount + 4;
                    } else {
                        dayAndDate = dayAndDate + words[wordsCount+4].replaceAll("[^a-zA-Z0-9]", "");
                        String finalWord = processDayDate(dayAndDate);
                        System.out.println(index + " : " + finalWord);
                        entries.add(finalWord);
                        return wordsCount + 5;
                    }
                }

                if (CommonUtil.isInteger(words[wordsCount+2].replaceAll("[^a-zA-Z0-9]", ""))) {
                    String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "")+
                            words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", "") +
                            words[wordsCount+2].replaceAll("[^a-zA-Z0-9]", "");
                    String finalWord = processDayDate(dayAndDate);
                    System.out.println(index + " : " + finalWord);
                    entries.add(finalWord);
                    return wordsCount + 3;
                }
            }
            else if (CommonUtil.isStringContainNumber(words[wordsCount]+ words[wordsCount + 1])) {
                if (countOfNumber(words[wordsCount]+ words[wordsCount + 1]) == 8) {
                    String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "")+ words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", "");
                    String finalWord = processDayDate(dayAndDate);
                    System.out.println(index + " : " + finalWord);
                    entries.add(finalWord);
                    return wordsCount + 2;
                } else if (countOfNumber((words[wordsCount] + words[wordsCount + 1])) == 8) {
                    String dayAndDate = words[wordsCount].replaceAll("[^a-zA-Z0-9]", "") + words[wordsCount + 1].replaceAll("[^a-zA-Z0-9]", "");
                    String finalWord = processDayDate(dayAndDate);
                    System.out.println(index + " : " + finalWord);
                    entries.add(finalWord);
                    return wordsCount + 2;
                } else {
                    String day = CommonUtil.getValidDay(words[wordsCount]);
                    String date = words[wordsCount + 1];
                    if (countOfNumber(date) == 8) {
                        String finalWord = day + "," + CommonUtil.formatDate(date);
                        System.out.println(index + " : " + finalWord);
                        entries.add(finalWord);
                        return wordsCount + 2;
                    } else {
                        date += words[wordsCount + 2];
                        String finalWord = day + "," + CommonUtil.formatDate(date);
                        System.out.println(index + " : " + finalWord);
                        entries.add(finalWord);
                        return wordsCount + 3;
                    }
                }
            }
        }
        return 0;
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
                String[] dayDateArray = word.split("(?<=\\D)(?=\\d)");
                day = dayDateArray[0];
                String dateMonthyear = dayDateArray[1];
                date = dateMonthyear.substring(0,2) + "/" + dateMonthyear.substring(2,4) + "/" + dateMonthyear.substring(4,8);
            }
            return day + "," + date;
        } else {
            date = CommonUtil.formatDate(word);
            return date;
        }
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

    private static int countOfNumber(String word) {
        int count = 0;
        for (String letter : word.split("")) {
            if (CommonUtil.isInteger(letter)) {
                count++;
            }
        }
        return count;
    }
}
