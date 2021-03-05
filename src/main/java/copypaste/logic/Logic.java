package copypaste.logic;

import constant.CommonConstants;
import copypaste.util.NumberUtil;

import java.util.Arrays;
import java.util.List;

public class Logic {
    public static String getPhoneNumbers(String str) {
        if (str != null) {
            String strLowerCase = str.toLowerCase();

            for (String phoneNumberString : CommonConstants.PHONE_NUMBER_STRINGS) {
                if (strLowerCase.contains(phoneNumberString)) {
                    List<String> strings = Arrays.asList(strLowerCase.split(phoneNumberString));
                    String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                    if (pN.length() > 8) {
                        return pN;
                    }
                }
            }

           /* if (strLowerCase.contains("phone number")) {
                List<String> strings = Arrays.asList(strLowerCase.split("phone number"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }
            if (strLowerCase.contains("mobile")) {
                List<String> strings = Arrays.asList(strLowerCase.split("mobile"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }
            if (strLowerCase.contains("phone")) {
                List<String> strings = Arrays.asList(strLowerCase.split("phone"));
                for (String string : strings) {
                    String pN = NumberUtil.getPhoneNumber(string.trim());
                    if (pN.length() > 8) {
                        return pN;
                    }
                }
            }
            if (strLowerCase.contains("tel")) {
                List<String> strings = Arrays.asList(strLowerCase.split("tel"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }
            if (strLowerCase.contains("telephone")) {
                List<String> strings = Arrays.asList(strLowerCase.split("telephone"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }

            if (strLowerCase.contains("call")) {
                List<String> strings = Arrays.asList(strLowerCase.split("call"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }

            if (strLowerCase.contains("sweet lines")) {
                List<String> strings = Arrays.asList(strLowerCase.split("sweet lines"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }*/
        }
        return "";
    }


    public static String getFaxNumber(String str) {
        if (str != null) {
            String strLowerCase = str.toLowerCase();
            if (strLowerCase.contains("fax number")) {
                List<String> strings = Arrays.asList(strLowerCase.split("fax number"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }
            if (strLowerCase.contains("fax")) {
                List<String> strings = Arrays.asList(strLowerCase.split("fax"));
                String pN = NumberUtil.getPhoneNumber(strings.get(1).trim());
                if (pN.length() > 8) {
                    return pN;
                }
            }
        }
        return "";
    }


    public static String getEmail(String str) {
        if (str != null) {
            String strLowerCase = str.toLowerCase();
            if (strLowerCase.contains("email")) {
                List<String> strings = Arrays.asList(strLowerCase.split("email"));
                for (String string : strings) {
                    String email = NumberUtil.getEmailAddress(string.trim());
                    if (email.length() > 1) {
                        return email;
                    }
                }
            }

            if (strLowerCase.contains("mail")) {
                List<String> strings = Arrays.asList(strLowerCase.split("email"));
                for (String string : strings) {
                    String email = NumberUtil.getEmailAddress(string.trim());
                    if (email.length() > 1) {
                        return email;
                    }
                }
            }
        }
        return "";
    }


}
