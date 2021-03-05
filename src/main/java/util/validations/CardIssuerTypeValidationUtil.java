package util.validations;

import util.CommonUtil;

import java.util.ArrayList;

public final class CardIssuerTypeValidationUtil {
    public static int validate(int wordsCount, ArrayList<String> entries, String[] words) {
        //TODO:: this validation should be changed, planning to get all card issuer and to pick suitable one
        int next = 0;
        StringBuilder finalWord = new StringBuilder();
        while (!CommonUtil.isInteger(words[wordsCount + next])) {
           finalWord.append(words[wordsCount + next]).append(" ");
           next++;
        }
        entries.add(finalWord.substring(0, finalWord.length() - 1));
        System.out.println("3 : " + finalWord.substring(0, finalWord.length() - 1));
        return wordsCount + next;
    }
}
