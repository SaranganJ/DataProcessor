package textprocessor;

import constant.CommonConstants;
import util.CSVUtils;

import java.io.IOException;
import java.util.Arrays;

import java.io.FileWriter;


public class Main {

    public static void main(String[] args) {
        try {
            ImageReader.detectText();

            String csvFile = CommonConstants.CSV_PATH_CHRUST;
            FileWriter writer = new FileWriter(csvFile);

            CSVUtils.writeLine(writer, Arrays.asList("a", "b", "c", "d"));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

//&& readTextArr[index + 1].substring(0).equals("8")