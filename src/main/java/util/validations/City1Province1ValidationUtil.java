package util.validations;

import util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class City1Province1ValidationUtil {

    public static int validate(int wordsCount, ArrayList<String> entries, String[] words) {
        int next = 0;
        List<String> finalWordArray = new ArrayList<>();
        while (!CommonUtil.isInteger(words[wordsCount + next])) {
            finalWordArray.add(words[wordsCount + next]);
            next++;
        }
        int totalSizeofArray = finalWordArray.size();
        StringBuilder city1Builder = new StringBuilder();
        for (int index = 0; index < totalSizeofArray; index++) {
            if (index <= totalSizeofArray - 2) {
                city1Builder.append(finalWordArray.get(index)).append(" ");
            }
        }
        String province1;
        String city1;
        if (finalWordArray.get(totalSizeofArray - 1).equals("A") && finalWordArray.get(totalSizeofArray - 2).equals("c") ) {
            city1 = city1Builder.substring(0, city1Builder.length() -2);
            province1 = finalWordArray.get(totalSizeofArray - 2) + finalWordArray.get(totalSizeofArray - 1);
        } else {
            city1 = city1Builder.substring(0, city1Builder.length() -1);
            province1 = finalWordArray.get(totalSizeofArray - 1);
        }
        entries.add(city1);
        entries.add(province1);
        System.out.println("18 : " + city1);
        System.out.println("19 : " + province1);
        return wordsCount + next;
    }
}
