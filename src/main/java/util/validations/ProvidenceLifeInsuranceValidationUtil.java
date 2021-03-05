package util.validations;

import util.CommonUtil;

import java.util.ArrayList;

public class ProvidenceLifeInsuranceValidationUtil {

    private static final String YES = "Yes";
    private static final String NO = "No";

    public static int validate(int wordsCount, ArrayList<String> entries, String[] words) {
        int next = 0;
        String finalWord = null;
        ArrayList<String> yesOrNoArray = new ArrayList();
        while (!CommonUtil.isInteger(words[wordsCount + next])) {
            yesOrNoArray.add(words[wordsCount + next]);
            next++;
        }
        for (int j = 0; j < yesOrNoArray.size(); j++) {
            if (yesOrNoArray.get(j).contains("Y") || yesOrNoArray.get(j).contains("y")) {
                finalWord = YES;
                break;
            }
            if (yesOrNoArray.get(j).contains("N") || yesOrNoArray.get(j).contains("n")) {
                finalWord = NO;
                break;
            }
        }
        entries.add(finalWord);
        System.out.println("6 : " + finalWord);
        return wordsCount + next;
    }
}
