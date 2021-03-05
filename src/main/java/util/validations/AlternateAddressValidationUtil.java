package util.validations;

import util.CommonUtil;

import java.util.List;

public class AlternateAddressValidationUtil {
    public static int validate(int wordsCount, List<String> entries, String[] words) {
        int next = 0;
        StringBuilder alternateAddressSb = new StringBuilder();
        while (words[wordsCount + next] != null && !containsDay(words[wordsCount + next]) ) {
            alternateAddressSb.append(words[wordsCount + next]).append(" ");
            next++;
        }
        String registerAddress = alternateAddressSb.substring(0, alternateAddressSb.length() - 1);
        entries.add(registerAddress);
        registerAddress = processRegisterAddress(registerAddress);
        System.out.println("23 : " + registerAddress);
        return wordsCount + next;
    }

    private static String processRegisterAddress(String registerAddress) {
        StringBuilder numberPartSb = new StringBuilder();
        StringBuilder stringPartSb = new StringBuilder();
        int index = 0;
        for (String letter : registerAddress.split("")) {
            if (CommonUtil.isInteger(letter)) {
                numberPartSb.append(letter);
            } else {
                if (oButZero(letter, registerAddress, index)) {
                    numberPartSb.append(0);
                }
                else {
                    stringPartSb.append(letter);
                }
            }
            index++;
        }
        return numberPartSb.toString().trim() + " " + stringPartSb.toString().trim();
    }

    private static boolean oButZero(String letter, String registerAddress, int index) {
        String[] regAddArray = registerAddress.split("");
        //if previous letter number or space and next letter number or space
        return  ("o".equalsIgnoreCase(letter) &&
                isSpaceOrNumber(regAddArray[index - 1]) && isSpaceOrNumber(regAddArray[index + 1]));
    }

    private static boolean containsDay(String word) {
        return word.contains("da") || word.contains("Da")  || word.contains("DA") || word.contains("/") ||
                word.contains("//") || word.contains("ds") || word.contains("dr") || word.contains("dA")
                || word.contains("Sat") || word.contains("Wed") || word.contains("Mon") ;
    }

    private static boolean isSpaceOrNumber(String letter) {
        return " ".equals(letter) || CommonUtil.isInteger(letter);
    }
}
