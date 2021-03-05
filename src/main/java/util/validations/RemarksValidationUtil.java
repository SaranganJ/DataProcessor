package util.validations;

import java.util.ArrayList;

public class RemarksValidationUtil {
    public static int validate(int wordsCount, ArrayList<String> entries, String[] words) {
        int next = 0;
        String remark = "";
        while (!words[wordsCount + next].contains("M")) {
            remark += words[wordsCount + next];
            next++;
        }
        if (remark.contains("N")) {
            entries.add("Not Available");
            System.out.println("12 : " + "Not Available");
        } else {
            entries.add("Available");
            System.out.println("12 : " +  "Availble");
        }
        return wordsCount + next;
    }
}
