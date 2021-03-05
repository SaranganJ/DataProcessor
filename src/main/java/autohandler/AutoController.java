package autohandler;

import constant.CommonConstants;
import dto.RecordDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoController {
    static int buttonPointer = 0;
    static HashMap<String, MouseLocation> mouseLocationHashMap = new HashMap<>();
    public static void main(String[] args) throws IOException,
            AWTException, InterruptedException
    {
        String command = "C:\\Users\\Chrustkiran\\OneDrive\\Documents\\OCR\\CibilSetupLive6.1.1\\setup.exe";
        //String command = "notepad.exe";
        Runtime run = Runtime.getRuntime();
        run.exec(command);
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Create an instance of Robot class
        Robot robot = new Robot();
        //login(robot);
        robot.mouseMove(200, 300);
        getMousePosition();
    }

    public static void saveRecords(ArrayList<RecordDTO> records) {
        //String command = "C:\\Users\\Chrustkiran\\OneDrive\\Documents\\OCR\\CibilSetupLive6.1.1\\setup.exe";
        String command = "notepad.exe";
        Runtime run = Runtime.getRuntime();
        try {
            run.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        try {
            final Robot robot = new Robot();
            //login(robot);
            robot.mouseMove(200, 300);
            getMousePosition();
            records.forEach(record -> {
                if (record.getEntries() != null && record.getEntries().size() > 0) {
                    int index = 0;
                    for (String entry : record.getEntries()) {
                        record.getEntries().get(index);
                        MouseLocation mouseLocationOfEntry = mouseLocationHashMap
                                .get(CommonConstants.idFieldMap.get(index));
                        mouseMoveAndClick(robot, mouseLocationOfEntry);
                        copyPasteContent(entry, robot);
                        index++;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void login(Robot robot) throws InterruptedException {
        String loginId = "Typtics@cibil6";
        StringSelection stringSelection = new StringSelection(loginId);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        copyPaste(robot);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_ENTER);
    }

    private static void copyPasteContent(String content, Robot robot) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        copyPaste(robot);
    }

    private static void copyPaste(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }

    private static void getMousePosition() throws InterruptedException {
        System.out.println("Click mouse to save " + CommonConstants.BUTTON_NAME.values()[buttonPointer].toString() + "s location");

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                    if (event.toString().contains("MOUSE_CLICKED")) {
                        System.out.println(MouseInfo.getPointerInfo().getLocation().x + ", "
                                + MouseInfo.getPointerInfo().getLocation().y);
                        if (CommonConstants.BUTTON_NAME.values().length > buttonPointer) {
                            mouseLocationHashMap.put(CommonConstants.BUTTON_NAME.values()[buttonPointer].toString(),
                                    new MouseLocation(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y));
                            //System.out.println("Mouse pointer of " + CommonConstants.BUTTON_NAME.values()[buttonPointer].toString() + " got saved");
                            System.out.println(mouseLocationHashMap);
                            buttonPointer++;
                            System.out.println("Click mouse to save " + CommonConstants.BUTTON_NAME.values()[buttonPointer].toString() + "s location");

                        }
                    }

            }
        }, AWTEvent.MOUSE_EVENT_MASK);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setOpacity(0.6f);
        jFrame.setSize(500,500);
        //jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void mouseClick(Robot robot) {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }

    private static void mouseMoveAndClick(Robot robot, MouseLocation mouseLocation) {
        robot.mouseMove(mouseLocation.getX(), mouseLocation.getY());
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
    }

}
