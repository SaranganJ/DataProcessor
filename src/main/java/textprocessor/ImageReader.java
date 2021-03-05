package textprocessor;

import autohandler.AutoController;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import constant.CommonConstants;
import dto.RecordDTO;
import util.CommonUtil;
import util.validations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ImageReader {
    public static void detectText() throws IOException {
        File folder = new File(CommonConstants.IMAGE_PATH_CHRUST);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("Reading entries from file " + file.getName()+ "\n");
                detectText(file.getAbsolutePath());
                System.out.println("---------------------------------------------------\n");
            }
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isString(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }


    // This methods accepts integer value as string and return it as dollars.
    //1500 ---->$1,500.00
    public static String convertToCurreny (String value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        try {
            int dollarValue = Integer.parseInt(value);
            return format.format(dollarValue);
        } catch (NumberFormatException e) {
            long dollarValue = Long.parseLong(value);
            return format.format(dollarValue);
        }
    }

    public static boolean checkSubstring(String value, List<String>  myArrayList ) {
        for(String listItem : myArrayList){
            if(value.contains(listItem)){
                return true;
            }
        }
        return false;
    }


    // Detects text in the specified image.
    public static void detectText(String filePath) throws IOException {
        String readText = "";
        ArrayList<RecordDTO> records = new ArrayList<>();
        try {
            List<AnnotateImageRequest> requests = new ArrayList<>();

            ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        System.out.format("Error: %s%n", res.getError().getMessage());
                        return;
                    }
                    String[] l = new String[0];
                    boolean isFinished = false;
                    for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                        if (isFinished) {
                            break;
                        }
                        String s = annotation.getDescription();
//                        System.out.println(s);
                        l = s.split(" |\n");
//                    for (int i = 0 ; i < l.length ; i++) {
//                        System.out.println(l[i]);
//                    }

                        // This list is used to keep track of all attributes in an entry
                        ArrayList<String> entry = new ArrayList<String>();
                        // This count is used to keep track of the number of attributes in an entry.
                        long firstId = Long.parseLong(l[0]);

                        List<String> images = Arrays.asList(l);
                        int startIndex = 0;
                        long startElement = firstId;
                        List<String> idsInImages = generateIdsInImage(startElement);
                        int endIndex = 0;
                        for (int h = 1; h < 10; h++) {
                            if (h == 9) {
                                isFinished = true;
                                break;
                            }
                            endIndex = findEndIndex(idsInImages, images, endIndex);

                            List<String> temp = images.subList(startIndex, endIndex);
                            String[] f = temp.toArray(new String[0]);
                            handleZero(f);

                            System.out.println("\n");
                            System.out.println("........Record : " + (h) + ".............");

                            int whileLoopBreaker = 1;
                            int i = 0;
                            int numberOfFieldsInAnEntry = -1;

                            while (i < f.length) {
                                try {
                                    whileLoopBreaker = whileLoopBreaker + 1;
                                    if (whileLoopBreaker > f.length) {
                                        break;
                                    }

                                    numberOfFieldsInAnEntry++;

                                    if ("".equals(f[i])) {
                                        i++;
                                        numberOfFieldsInAnEntry--;
                                        continue;
                                    }

                                    //Logic for the ID (1st attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 0) {
                                        addIntoRecords(records, entry);
                                        // Clear the previous entries from the array list and add the entries for the new row
                                        entry.clear();
                                        entry.add(f[i]);
                                        System.out.println("1 : " + f[i]);
                                        i = i + 1;
                                        continue;
                                    }

                                    //Logic for the CUSTOMER ID (2nd attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 1) {
//                                        i = CustomerIdValidationUtil.validate(i, entry, f[i]);
                                        if (isInteger(f[i+1])) {
                                            entry.add(f[i] + f[i+1]);
                                            System.out.println("2 : " + f[i] + f[i+1]);
                                            i = i + 2;
                                            continue;
                                        } else {
                                            entry.add(f[i]);
                                            System.out.println("2 : " + f[i]);
                                            i = i + 1;
                                            continue;
                                        }
                                    }

                                    //Logic for the CARD ISSUER TYPE (3rd attribute)
                                    if (!isInteger(f[i]) && numberOfFieldsInAnEntry == 2) {
                                        i = CardIssuerTypeValidationUtil.validate(i, entry, f);
                                        continue;
                                    }

                                    //Logic for the CARD LIMIT (4th attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 3) {
                                        entry.add(convertToCurreny(f[i]));
                                        System.out.println("4 : " + convertToCurreny(f[i]));
                                        i = i + 1;
                                        continue;
                                    }

                                    //Logic for the AVERAGE MONTHLY USAGE (5th attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 4) {
                                        entry.add(convertToCurreny(f[i]));
                                        System.out.println("5 : " + convertToCurreny(f[i]));
                                        i = i + 1;
                                        continue;
                                    }

                                    //Logic for the DOES CREDIT CARD PROVIDENCE LIFE INSURANCE (6th attribute)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 5) {
                                        i = ProvidenceLifeInsuranceValidationUtil.validate(i, entry, f);
                                        continue;
                                    }

                                    //Logic for the AVERAGE MONTHLY PAYMENT (7th attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 6) {
                                        if (isInteger(f[i+1])) {
                                            entry.add(convertToCurreny(f[i] + f[i+1]));
                                            System.out.println("7 : " + convertToCurreny(f[i] + f[i+1]));
                                            i = i + 2;
                                            continue;
                                        } else {
                                            entry.add(convertToCurreny(f[i]));
                                            System.out.println("7 : " + convertToCurreny(f[i]));
                                            i = i + 1;
                                            continue;
                                        }
                                    }

                                    //Logic for the CARD ISSUER DATE (8th attribute)
                                    if (numberOfFieldsInAnEntry == 7) {
                                        //i = CardIssuerDateValidationUtil.validate(i, entry, f);
                                        i = DateValidationUtil.validate(i, entry, f, "8");
                                        continue;
                                    }

                                    //Logic for the CARD HOLDER NAME and CARD HOLDER DOB (9th and 10th attributes)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 8) {
                                        i = CardHolderNameDOBValidationUtil.validate(i, entry, f);
                                        numberOfFieldsInAnEntry++;
                                        continue;
                                    }


                                    //Logic for the DOCKET NO (11th attribute)
                                    if (numberOfFieldsInAnEntry == 10) {
                                        if (f[i].contains("PRO")) {
                                            entry.add(f[i]);
                                            System.out.println("11 : " + f[i]);
                                            i = i + 1;
                                            continue;
                                        }
                                        if (f[i].contains("PR") && f[i + 1].contains("O")) {
                                            entry.add(f[i] + f[i + 1]);
                                            System.out.println("11 : " + f[i] + f[i + 1]);
                                            i = i + 2;
                                            continue;
                                        }

                                        if (isInteger(f[i])) {
                                            if (f[i + 1].contains("PRO")) {
                                                entry.add(f[i] + f[i + 1]);
                                                System.out.println("11 : " + f[i] + f[i + 1]);
                                                i = i + 2;
                                                continue;
                                            }
                                            if (isInteger(f[i]) && f[i + 2].contains("PRO")) {
                                                entry.add(f[i] + f[i + 1] + f[i+2]);
                                                System.out.println("11 : " + f[i] + f[i + 1] + f[i+2]);
                                                i = i + 3;
                                                continue;
                                            }

                                        }
                                    }


                                    //Logic for the REMARKS (12th attribute)
                                    if (numberOfFieldsInAnEntry == 11) {
                                        //missing field
                                        i = RemarksValidationUtil.validate(i, entry, f);
                                        continue;
                                    }

                                    //Logic for the SEX 1 (13th attribute)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 12) {
                                        if (f[i].equals("MALE") || f[i].equals("FEMALE") || f[i].equals("FEMALĒ")) {
                                            entry.add(f[i]);
                                            System.out.println("13 : " + f[i]);
                                            i = i + 1;
                                            continue;
                                        }
                                        if ((f[i]+f[i+1]).equals("MALE") || (f[i]+f[i+1]).equals("FEMALE") || (f[i]+f[i+1]).equals("FEMALĒ")) {
                                            entry.add((f[i]+f[i+1]));
                                            System.out.println("13 : " + (f[i]+f[i+1]));
                                            i = i + 2;
                                            continue;
                                        }
                                    }

                                    //Logic for the REGISTER ADDRESS (14th attribute)
                                    if (numberOfFieldsInAnEntry == 13) {
                                        int prevI = i;
                                        i = RegisterAddressValidationUtil.validate(i, entry, f);
                                        if (i - prevI == 5) {
                                            //numberOfFieldsInAnEntry += 4;
                                        }
                                        continue;
                                    }

                                    // Logic for the CARD TYPE (15th attribute)
                                    if (numberOfFieldsInAnEntry == 14) {
                                        int k = i;
                                        while (!isDouble(f[i])) {
                                            i = i + 1;
                                        }
                                        String card = "";
                                        for (int n = k; n < i; n++) {
                                            card = card + " " + f[n];
                                        }
                                        entry.add(card.trim());
                                        System.out.println("15 : " + card.trim());

                                        continue;
                                    }


                                    //Logic for the RATE OF INTEREST TYPE (16th attribute)
                                    if (isDouble(f[i]) && numberOfFieldsInAnEntry == 15) {
                                        if (f[i].equals("0")){
                                            entry.add(f[i] + f[i+1]+ "%");
                                            System.out.println("16 : " + f[i] + f[i+1]+ "%");
                                            i = i + 2;
                                            continue;
                                        }
                                        if (f[i].equals("0.")){
                                            entry.add(f[i] + f[i+1]+ "%");
                                            System.out.println("16 : " + f[i] + f[i+1]+ "%");
                                            i = i + 2;
                                            continue;
                                        }
                                        if (Double.parseDouble(f[i]) < 1) {
                                            if (isInteger(f[i+1]) && !isInteger(f[i+2])) {
                                                entry.add(f[i] + "%");
                                                System.out.println("16 : " + f[i] + "%");
                                                i = i + 1;
                                                continue;
                                            }
                                            if (isInteger(f[i+1]) && isInteger(f[i+2])) {
                                                entry.add(f[i] + f[i+1]+ "%");
                                                System.out.println("16 : " + f[i] + f[i+1]+ "%");
                                                i = i + 2;
                                                continue;
                                            }
                                        }
                                    }

                                    //Logic for the   FICO CREDIT SCORE (17th attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 16) {
                                        entry.add(convertToCurreny(f[i]));
                                        System.out.println("17 : " + convertToCurreny(f[i]));

                                        i = i + 1;
                                        continue;
                                    }

                                    //Logic for the CITY 1 & PROVINCE 1 (18th & 19th attribute)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 17) {
                                        i = City1Province1ValidationUtil.validate(i, entry, f);
                                        numberOfFieldsInAnEntry++;
                                        continue;
                                    }

                                    //Logic for the ZIP Code (20th attribute)

                                    if (numberOfFieldsInAnEntry == 19) {
                                        if (isInteger(f[i]) && isInteger(f[i + 1])) {
                                            entry.add(f[i] + f[i + 1]);
                                            System.out.println("20 : " + f[i] + f[i + 1]);
                                            i = i + 2;
                                            continue;
                                        }
                                        if (isInteger(f[i])) {
                                            entry.add(f[i]);
                                            System.out.println("20 : " + f[i]);

                                            i = i + 1;
                                            continue;
                                        }

                                    }

                                    //Logic for the REMARKS (21th attribute)
                                    if (numberOfFieldsInAnEntry == 20) {
                                        //missing field

                                        if (f[i].contains("NO") && f[i + 1].contains("Avail")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("21 : " + "NOT AVAILABLE");

                                            i = i + 2;
                                            continue;
                                        }

                                        if (f[i].contains("No") && f[i + 1].contains("Avail")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("21 : " + "NOT AVAILABLE");

                                            i = i + 2;
                                            continue;
                                        }

                                        if (f[i].contains("NO") && f[i + 1].contains("t") && f[i + 2].contains("Avail")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("21 : " + "NOT AVAILABLE");

                                            i = i + 3;
                                            continue;
                                        }

                                        if (f[i].contains("No") && f[i + 1].contains("t") && f[i + 2].contains("Avail")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("21 : " + "NOT AVAILABLE");

                                            i = i + 3;
                                            continue;
                                        }

                                        if (f[i].contains("N") && f[i + 2].contains("ble")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("21 : " + "NOT AVAILABLE");

                                            i = i + 3;
                                            continue;
                                        }
                                    }

                                    //Logic for the BENEFICIARY NAME (22nd attribute)
                                    if (isString(f[i].replaceAll("[^\\\\dA-Za-z ]", ""))
                                            && numberOfFieldsInAnEntry == 21) {
                                        i = BeneficiaryNameValidationUtil.validate(i, entry, f);
                                        continue;
                                    }


                                    //Logic for the ALTERNATE ADDRESS (23rd attribute)
                                    if (isInteger(f[i]) && numberOfFieldsInAnEntry == 22) {
                                        i = AlternateAddressValidationUtil.validate(i, entry, f);
                                        continue;
                                    }

                                    //Logic for BENEFICIARY DOB(24th ATTRIBUTe)
                                    if (numberOfFieldsInAnEntry == 23) {
                                        i = DateValidationUtil.validate(i, entry, f, "24");
                                    }

                                    //Logic for the CITY 2 & PROVINCE 2 (25th & 26th attribute)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 24) {
                                        if (isString(f[i + 1]) && isInteger(f[i + 2])) {
                                            entry.add(f[i]);
                                            System.out.println("25 : " + f[i]);
                                            entry.add(f[i + 1]);
                                            System.out.println("26 : " + f[i + 1]);
                                            numberOfFieldsInAnEntry = numberOfFieldsInAnEntry + 1;
                                            i = i + 2;
                                            continue;
                                        }
                                        if (isString(f[i + 1]) && isString(f[i + 2]) && isInteger(f[i + 3])) {
                                            entry.add(f[i] + " " + f[i + 1]);
                                            System.out.println("25 : " + f[i] + " " + f[i + 1]);
                                            entry.add(f[i + 2]);
                                            System.out.println("26 : " + f[i + 2]);
                                            numberOfFieldsInAnEntry = numberOfFieldsInAnEntry + 1;
                                            i = i + 3;
                                            continue;
                                        }
                                        if (isString(f[i + 1]) &&  isString(f[i + 2]) && isString(f[i + 3]) && isInteger(f[i + 4])) {
                                            entry.add(f[i] + " " + f[i + 1] + " " + f[i + 2]);
                                            System.out.println("25 : " + f[i] + " " + f[i + 1] + " " + f[i + 2]);
                                            entry.add(f[i + 3]);
                                            System.out.println("26 : " + f[i + 3]);
                                            numberOfFieldsInAnEntry = numberOfFieldsInAnEntry + 1;
                                            i = i + 4;
                                            continue;
                                        }
                                        if (isString(f[i + 1]) && isString(f[i + 2]) && isString(f[i + 3])
                                                && isString(f[i + 4]) && isInteger(f[i + 5])) {
                                            //Special case New York
                                            if (f[i + 3].contains("Ne") && f[i + 4].contains("Yo")){
                                                entry.add(f[i] + " " + f[i + 1] + " " + f[i + 2]);
                                                System.out.println("25 : " + f[i] + " " + f[i + 1] + " " + f[i + 2]);
                                                entry.add(f[i + 4]);
                                                System.out.println("26 : " + f[i + 3] + " " + f[i + 4]);
                                                numberOfFieldsInAnEntry = numberOfFieldsInAnEntry + 1;
                                                i = i + 5;
                                                continue;
                                            }
                                            entry.add(f[i] + " " + f[i + 1] + " " + f[i + 2]+" " + f[i + 3] );
                                            System.out.println("25 : " + f[i] + " " + f[i + 1] + " " + f[i + 2]+" " + f[i + 3]);
                                            entry.add(f[i + 4]);
                                            System.out.println("26 : " + f[i + 4]);
                                            numberOfFieldsInAnEntry = numberOfFieldsInAnEntry + 1;
                                            i = i + 5;
                                            continue;
                                        }
                                    }

//                            if ( numberOfFieldsInAnEntry == 26) {
//                                while (CommonUtil.isInteger(f[i])) {
//                                    i++;
//                                }// need to ask
//                            }

                                    //Logic for the ZIP CODE 2 (27th attribute)
                                    if (numberOfFieldsInAnEntry == 26) {
                                        if (isInteger(f[i + 1]) && isInteger(f[i])) {
                                            entry.add(f[i] + f[i + 1]);
                                            System.out.println("27 : " + f[i] + f[i + 1]);
                                            i = i + 2;
                                            continue;
                                        }
                                        if (isInteger(f[i])) {
                                            entry.add(f[i]);
                                            System.out.println("27 : " + f[i]);

                                            i = i + 1;
                                            continue;
                                        }

                                    }

                                    //Logic for the Country (27th attribute)
                                    if (numberOfFieldsInAnEntry == 27) {
                                        if (f[i].equalsIgnoreCase("united") && f[i + 1].equalsIgnoreCase("states")) {
                                            entry.add(f[i] + " " + f[i + 1]);
                                            System.out.println("28 : " + f[i] + " " + f[i + 1]);

                                            i = i + 2;
                                            continue;
                                        }
                                        String ss = f[i] + f[i + 1] + f[i + 2].replaceAll("[^a-zA-Z0-9]", "");
                                        if (ss.equalsIgnoreCase("unitedstates")
                                                || ss.equalsIgnoreCase("UÑITEDSTATES")) {

                                            entry.add(f[i] + " " + f[i + 1] + " " + f[i + 2]);
                                            System.out.println("28 : " + f[i] + " " + f[i + 1] + " " + f[i + 2]);

                                            i = i + 3;
                                            continue;
                                        }

                                        if (isString(f[i]) && isString(f[i + 1]) && isString(f[i + 2])) {
                                            entry.add(f[i] + " " + f[i + 1]+ " " + f[i + 2]);
                                            System.out.println("28 : " + f[i] + " " + f[i + 1] + " " + f[i + 2]);

                                            i = i + 3;
                                            continue;
                                        }
                                        if (isString(f[i]) && isString(f[i + 1])) {
                                            entry.add(f[i] + " " + f[i + 1]);
                                            System.out.println("28 : " + f[i] + " " + f[i + 1]);

                                            i = i + 2;
                                            continue;
                                        }

                                    }

                                    List<String> bloodGroups = Arrays.asList("A-", "A+", "B-", "B+", "O-", "O+", "AB-", "AB+", "0+", "0-");

                                    //Logic for the BLOOD GROUP (28th attribute)
                                    if (numberOfFieldsInAnEntry == 28) {
                                        if (bloodGroups.contains(f[i])) {
                                            entry.add(f[i]);
                                            System.out.println("29 : " + f[i]);
                                            i = i + 1;
                                            continue;
                                        }
                                        if (f[i + 1].charAt(0) == 'M' || f[i + 1].charAt(0) == 'm' || f[i + 1].charAt(0) == 'F' || f[i + 1].charAt(0) == 'f') {
                                            entry.add(f[i]);
                                            System.out.println("29 : " + f[i] + " This Entry is wrong");
                                            i = i + 1;
                                            continue;
                                        }
                                    }


                                    //Logic for the SEX 2 (29th attribute)
                                    if (isString(f[i]) && numberOfFieldsInAnEntry == 29) {
                                        if (f[i].equals("MALE") || f[i].equals("FEMALE")) {
                                            entry.add(f[i]);
                                            System.out.println("30 : " + f[i]);

                                            i = i + 1;
                                            continue;
                                        }
                                        String gender = "";
                                        if (f[i].substring(0, 1) == "M" || f[i].substring(0, 1) == "m") {
                                            while (!gender.equals("MALE")) {
                                                gender = gender + f[i];
                                                i = i + 1;
                                            }
                                        } else {
                                            while (!gender.equals("FEMALE")) {
                                                gender = gender + f[i];
                                                i = i + 1;
                                            }
                                        }
                                        entry.add(gender);
                                        System.out.println("30 : " + gender);

                                        continue;
                                    }

                                    //Logic for the Card Expire date (30th attribute)
                                    if (numberOfFieldsInAnEntry == 30) {
                                        i = DateValidationUtil.validate(i, entry, f, "31");
                                    }

                                    //Logic for the CREDIT CARD ACCOUNT NUMBER and IDON CUSTOMER NUMBER (31st and 32nd attribute)
                                    if ((isInteger(f[i]) || isLong(f[i])) && numberOfFieldsInAnEntry == 31) {

                                        if (isInteger(f[i+1]) && isInteger(f[i+2])) {
                                            long one = Long.valueOf(f[i]);
                                            long two = Long.valueOf(f[i+1]);
                                            long three = Long.valueOf(f[i+2]);
                                            if (three > one && three > two){
                                                entry.add(f[i] + f[i+1]);
                                                entry.add(f[i+2]);
                                                System.out.println("32 : " + f[i] + f[i+1]);
                                                System.out.println("33 : " + f[i+2]);
                                                i = i + 3;
                                                continue;
                                            }
                                            if (one > two && one > three){
                                                entry.add(f[i] );
                                                entry.add(f[i+1] + f[i+2]);
                                                System.out.println("32 : " + f[i]);
                                                System.out.println("33 : " + f[i+1] + f[+2]);
                                                i = i + 3;
                                                continue;
                                            }
                                        } else if (isInteger(f[i+1])) {
                                            entry.add(f[i]);
                                            entry.add(f[i+1]);
                                            System.out.println("32 : " + f[i]);
                                            System.out.println("33 : " + f[i+1]);
                                            i = i + 2;
                                            continue;
                                        }
                                    }

                                    if (numberOfFieldsInAnEntry == 33) {
                                        //missing field
                                        if (f[i].contains("NO") && f[i + 1].contains("Available")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("34 : " + "NOT AVAILABLE");

                                            i = i + 2;
                                            continue;
                                        }

                                        if (f[i].contains("No") && f[i + 1].contains("Available")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("34 : " + "NOT AVAILABLE");

                                            i = i + 2;
                                            continue;
                                        }

                                        if (f[i].contains("NO") && f[i + 1].contains("t") && f[i + 2].contains("Available")) {
                                            entry.add("NOT AVAILABLE");
                                            System.out.println("34 : " + "NOT AVAILABLE");

                                            i = i + 3;
                                            continue;
                                        }
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    startIndex = endIndex;
                                    //startElement = startElement + 1;
                                    break;
                                }
                            }
                            startIndex = endIndex;
                            //startElement = startElement + 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
     //   AutoController.saveRecords(records);
//        printRecords(records);
    }

    private static List<String> generateIdsInImage(long startElement) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            ids.add(startElement + i + "");
        }
        return ids;
    }

    private static int findEndIndex(List<String> ids, List<String> images, int endIndex) {
        for (int i = endIndex; i < images.size(); i++) {
           if (ids.contains(images.get(i))) {
                ids.remove(images.get(i));
                return i;
            }
        }
        return images.size();
    }

    private static void printRecords(List<RecordDTO> records) {
        if (records != null  && records.size() > 0) {
            Collections.sort(records);
            for (RecordDTO record : records) {
                System.out.println("-----------Printing record for id : " + record.getId() + " -------------");
                if (record.getEntries() != null && record.getEntries().size() > 0) {
                    int index = 0;
                    for (String entry : record.getEntries()) {
                        System.out.println(index + ". " + entry);
                        index++;
                    }
                }
                System.out.println();
            }
        }
    }

    private static void addIntoRecords(List<RecordDTO> records, ArrayList<String> entries) {
        if (entries != null && entries.size() > 0) {
            RecordDTO record = new RecordDTO();
            record.setId(entries.get(0));
            record.setEntries(new ArrayList<>(entries));
            
            records.add(record);
        }
    }

    private static void handleZero(String[] f) {
        if (f != null && f.length > 0) {
            int index = 0;
            for (String s : f) {
                if ("o".equalsIgnoreCase(s) || "0".equals(s)) {
                    if (index > 0 && CommonUtil.isInteger(f[index - 1])) {
                        f[index - 1] = f[index - 1] + "0";
                        f[index] = "";
                    }

                    else if (index < f.length && CommonUtil.isInteger(f[index + 1])) {
                        f[index + 1] = "0" + f[index + 1];
                        f[index] = "";
                    }
                }
                index++;
            }
        }
    }
}
