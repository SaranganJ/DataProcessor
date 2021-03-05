package util.validations;

import util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class CardHolderNameDOBValidationUtil {
    public static int validate(int wordsCount, List<String> entries, String[] words) {
        int next = 0;
        List<String> cardHolderNameAndDOB = new ArrayList<>();
        while (!CommonUtil.isStringContainNumber(words[wordsCount + next])) {
            cardHolderNameAndDOB.add(words[wordsCount + next]);
            next++;
        }
        if (words[wordsCount + next+1].contains("P")) {
            cardHolderNameAndDOB.add(words[wordsCount + next]);
        } else {
            if (CommonUtil.removeSpecialChar(words[wordsCount + next]).length() ==8){
                cardHolderNameAndDOB.add(words[wordsCount + next]);
            } else {
                cardHolderNameAndDOB.add(words[wordsCount + next]+ words[wordsCount + next+1]);
            }

        }


        correctCardHolderDOB(cardHolderNameAndDOB);
        StringBuilder cardHolderNameBuilder = new StringBuilder();
        for (String finalWord : cardHolderNameAndDOB.subList(0, cardHolderNameAndDOB.size() - 1)) {
            cardHolderNameBuilder.append(finalWord).append(" ");
        }
        System.out.println("9 : " + cardHolderNameBuilder.substring(0, cardHolderNameBuilder.length() - 1));
        System.out.println("10 : " + cardHolderNameAndDOB.get(cardHolderNameAndDOB.size() - 1));
        entries.add(cardHolderNameBuilder.substring(0, cardHolderNameBuilder.length() - 1));
        entries.add(cardHolderNameAndDOB.get(cardHolderNameAndDOB.size() - 1));
        return wordsCount + next + 1;
    }

    private static void correctCardHolderDOB(List<String> cardHolderNameAndDOB) {
        int lastIndex = cardHolderNameAndDOB.size() -1;
        //monday,11/12/2020 (same word)
        if (cardHolderNameAndDOB.get(lastIndex).contains("day")
                || cardHolderNameAndDOB.get(lastIndex).contains("Day") ||  cardHolderNameAndDOB.get(lastIndex).contains("dsy")
        || cardHolderNameAndDOB.get(lastIndex).contains("dry")) {
           String lastWord = cardHolderNameAndDOB.remove(lastIndex);

           if (lastWord.contains(",")) {
               String[] lastWordArray = lastWord.split(",");
               separateDOB(lastWordArray, cardHolderNameAndDOB);
           } else {
               String[] lastWordArray = lastWord.split("(?<=\\D)(?=\\d)");
               separateDOB(lastWordArray, cardHolderNameAndDOB);
           }
        } else {
            //monday , 11/12/2020 (sep word)
            String lastWord = cardHolderNameAndDOB.remove(lastIndex);
            String day = cardHolderNameAndDOB.remove(lastIndex - 1);
            String finalDob = "";
            if (day.contains("Da") || day.contains("da")) {
                finalDob += CommonUtil.removeSpecialChar(day) + ",";
            }
            finalDob += CommonUtil.formatDate(lastWord);
            cardHolderNameAndDOB.add(finalDob);
        }
    }

    private static int getFirstNumberIndex(String lastWord) {
        if (lastWord != null && lastWord.length() > 0) {
            for (String letter : lastWord.split("")) {
                if (CommonUtil.isInteger(letter)) {
                    return lastWord.indexOf(letter);
                }
            }
        }
        return lastWord.length() - 1;
    }

    private static void separateDOB(String[] lastWordArray, List<String> cardHolderNameAndDOB) {
        if (lastWordArray.length == 2) {
            if (lastWordArray[1].length() > 0) {
                String dob = lastWordArray[1];
                dob = CommonUtil.formatDate(dob);
                cardHolderNameAndDOB.add(lastWordArray[0] + "," + dob);
            }
        }
        if (lastWordArray.length == 4) {
            String dob = lastWordArray[1]+ lastWordArray[2]+ lastWordArray[3];
            dob = CommonUtil.formatDate(dob);
            cardHolderNameAndDOB.add(lastWordArray[0] + "," + dob);
        }

    }

}
