package util.validations;

import constant.CommonConstants;
import util.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterAddressValidationUtil {


    
    public static int validate(int wordsCount, ArrayList<String> entries, String[] words) {
        int next = 0;
        List<String> addressWords = new ArrayList<>();
        while (!(words[wordsCount + next]).contains(".")) {
            addressWords.add(words[wordsCount + next]);
            next++;
        }
        if (addressWords.get(addressWords.size()-1).equals("0")) {
            next --;
            addressWords = addressWords.subList(0, addressWords.size()-1);
        }

        int toBeRemovedWordsCount = 0;
        if (CommonUtil.checkSubstring(addressWords.get(addressWords.size() - 2), CommonConstants.cardTypesprefix)) {
            toBeRemovedWordsCount = 2; //case America Express
        } else if (CommonUtil.checkSubstring(addressWords.get(addressWords.size() - 3), CommonConstants.cardTypesprefix)){
            toBeRemovedWordsCount = 3; //case Ame rica Express
        } else {
            toBeRemovedWordsCount = 1;
        }
        addressWords = addressWords.subList(0, addressWords.size() - toBeRemovedWordsCount);
        String finalRegAddress = String.join(" ", addressWords);
        entries.add(finalRegAddress);
        System.out.println("14 : " + finalRegAddress);
        return wordsCount + next - toBeRemovedWordsCount;
    }
}
