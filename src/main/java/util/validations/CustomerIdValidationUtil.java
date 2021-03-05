package util.validations;

import java.util.ArrayList;

public class CustomerIdValidationUtil {
    public static int validate(int wordsCount, ArrayList<String> entries, String word) {
        entries.add(word);
        System.out.println("2 : " + word);
        wordsCount++;
        return wordsCount;
    }
}
