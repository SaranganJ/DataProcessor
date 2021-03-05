package util.validations;

import util.CommonUtil;

import java.util.List;

public class BeneficiaryNameValidationUtil {
    public static int validate(int wordsCount, List<String> entries, String[] words) {
        int next = 0;
        StringBuilder beneficiarBuilder = new StringBuilder();
        while (!CommonUtil.isInteger(words[wordsCount + next])) {
            beneficiarBuilder.append(words[wordsCount + next]).append(" ");
            next++;
        }
        String beneFinalName = beneficiarBuilder.substring(0, beneficiarBuilder.length() - 1);
        entries.add(beneFinalName);
        System.out.println("22 : " + beneFinalName);
        return wordsCount + next;
    }
}
