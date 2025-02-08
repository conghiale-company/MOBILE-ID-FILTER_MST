/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package filtermst;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Admin
 */
public class FilterMST {

    /**
     * @param args the command line arguments
     */
    
    private static String PATH_FOLDER;
    private static String PATH_FILE;
    private static String START_MST;
    private static String END_MST;
    private static String message;
    private static String mst;
    private static String index;
    private static String dtisId;
    private static String previousMST;
    private static String contentInformation;
    private static String foundMessage;
    
    private static int numberNotFound = 0;
    private static int numberParameterIsInvalid = 0;
    private static int numberUnknownException = 0;
    private static int numberDataResponseIsNull = 0;
    private static int numberCaptchaInvalid = 0;
    private static int numberErrors = 0;
    private static int numberSuccessfully = 0;
    private static int numberTaxCode = 0;

    private static int sumNumberNotFound = 0;
    private static int sumNumberParameterIsInvalid = 0;
    private static int sumNumberUnknownException = 0;
    private static int sumNumberDataResponseIsNull = 0;
    private static int sumNumberCaptchaInvalid = 0;
    private static int sumNumberErrors = 0;
    private static int sumNumberSuccessfully = 0;
    private static int sumNumberTaxCode = 0;

    private static int totalNumberNotFound = 0;
    private static int totalNumberParameterIsInvalid = 0;
    private static int totalNumberUnknownException = 0;
    private static int totalNumberDataResponseIsNull = 0;
    private static int totalNumberCaptchaInvalid = 0;
    private static int totalNumberErrors = 0;
    private static int totalNumberSuccessfully = 0;
    private static int totalNumberTaxCode = 0;

    private static int lineNumber;
    private static int itemFile = 0;
    private static int numberRemoveTaxCode = 0;

    private static int folderCount = 0;

    private static boolean isSuccess = false;
    
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println();
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            if (args[0].contains(".txt") || args[0].contains(".log")) {
                PATH_FOLDER = args[0];

                if (args.length > 1 && args[1] != null && !args[1].isEmpty()) {
                    START_MST = args[1];

                    if (args.length > 2 && args[2] != null && !args[2].isEmpty())
                        END_MST = args[2];
                    else
                        END_MST = null;
                } else {
                    START_MST = null;
                    END_MST = null;
                }

                loadDataInFIle(1);
            } else {
                PATH_FOLDER = args[0];
                File directory = new File(PATH_FOLDER);

                if (!directory.exists()) {
                    System.out.println("Directory or file does not exist");
                    System.exit(0);
                } else if (!directory.isDirectory()) {
                    System.out.println("Invalid parameter");
                    System.exit(0);
                }

                processDirectory(directory, 1);
            }
        } else
            System.out.println("The first parameter is invalid. Filter file path");

    }

    private static void processDirectory(File directory, int level) {
        int fileCount = 0;
        boolean hasFiles = false;

        sumNumberSuccessfully = 0;
        sumNumberErrors = 0;
        sumNumberNotFound = 0;
        sumNumberParameterIsInvalid = 0;
        sumNumberUnknownException = 0;
        sumNumberCaptchaInvalid = 0;
        sumNumberDataResponseIsNull = 0;
        sumNumberTaxCode = 0;

        if (directory.exists() && directory.isDirectory()) {
            File[] filesList = directory.listFiles();

            contentInformation = "";
            if (filesList != null && filesList.length > 0) {
//                String tab = ((level - 1) != 0) ? "\t".repeat(level) : "";
                String tab = createTab(level);
                System.out.println("\n" + tab + "Retrieving all files from directory: " + directory.getAbsolutePath() + ". [" + Arrays.stream(filesList).count() + "]");
                contentInformation += ("\n" + tab + "Retrieving all files from directory: " + directory.getAbsolutePath() + ". [" + Arrays.stream(filesList).count() + "]");
                itemFile = 0;

//                Xu ly cac file trong mot folder
                for (File file : filesList) {
                    if (file.isDirectory()) {
                        folderCount++;
                        processDirectory(file, level + 1);
                    } else if (file.isFile()) {
                        itemFile++;
                        fileCount++;
                        hasFiles = true;

                        PATH_FILE = file.getAbsolutePath();
                        System.out.println();
//                        tab = (level != 0) ? "\t".repeat(level) : "";
                        tab = createTab(level);
                        System.out.println(tab + "PATH: " + PATH_FILE);
                        loadDataInFIle(level);
                    }
                }

//                Hien thi thong tin tong cua cac file sau khi xu ly cac file trong mot folder
                if (hasFiles) {
//                    tab = (level != 0) ? "\t".repeat(level) : "";
                    tab = createTab(level);
                    System.out.println("\n" + tab + "SUM: Successfully got all business tax code with folder path: " + PATH_FOLDER + ". [" + folderCount + "|" + fileCount + "]");
                    System.out.println(tab + "SUM: sumNumberTaxCode: " + sumNumberTaxCode);
                    System.out.println(tab + "SUM: sumNumberSuccessfully: " + sumNumberSuccessfully);
                    System.out.println(tab + "SUM: sumNumberErrors: " + sumNumberErrors);
                    System.out.println(tab + "SUM: sumNumberNotFound: " + sumNumberNotFound);
                    System.out.println(tab + "SUM: sumNumberParameterIsInvalid: " + sumNumberParameterIsInvalid);
                    System.out.println(tab + "SUM: sumNumberUnknownException: " + sumNumberUnknownException);
                    System.out.println(tab + "SUM: sumNumberCaptchaInvalid: " + sumNumberCaptchaInvalid);
                    System.out.println(tab + "SUM: sumNumberDataResponseIsNull: " + sumNumberDataResponseIsNull);

                    contentInformation += ("\n\n" + tab + "SUM: Successfully got all business tax code with folder path " + PATH_FOLDER + ". [" + folderCount + "|" + fileCount + "]");
                    contentInformation += ("\n" + tab + "SUM: sumNumberTaxCode: " + sumNumberTaxCode);
                    contentInformation += ("\n" + tab + "SUM: sumNumberSuccessfully: " + sumNumberSuccessfully);
                    contentInformation += ("\n" + tab + "SUM: sumNumberErrors: " + sumNumberErrors);
                    contentInformation += ("\n" + tab + "SUM: sumNumberNotFound: " + sumNumberNotFound);
                    contentInformation += ("\n" + tab + "SUM: sumNumberParameterIsInvalid: " + sumNumberParameterIsInvalid);
                    contentInformation += ("\n" + tab + "SUM: sumNumberUnknownException: " + sumNumberUnknownException);
                    contentInformation += ("\n" + tab + "SUM: sumNumberCaptchaInvalid: " + sumNumberCaptchaInvalid);
                    contentInformation += ("\n" + tab + "SUM: sumNumberDataResponseIsNull: " + sumNumberDataResponseIsNull);

                    totalNumberSuccessfully += sumNumberSuccessfully;
                    totalNumberErrors += sumNumberErrors;
                    totalNumberNotFound += sumNumberNotFound;
                    totalNumberParameterIsInvalid += sumNumberParameterIsInvalid;
                    totalNumberUnknownException += sumNumberUnknownException;
                    totalNumberCaptchaInvalid += sumNumberCaptchaInvalid;
                    totalNumberDataResponseIsNull += sumNumberDataResponseIsNull;
                    totalNumberTaxCode += sumNumberTaxCode;

                    tab = createTab(level);
                    System.out.println("\n" + tab + "TOTAL: Successfully got all business tax code with folder path: " + PATH_FOLDER);
                    System.out.println(tab + "TOTAL: totalNumberTaxCode: " + totalNumberTaxCode);
                    System.out.println(tab + "TOTAL: totalNumberSuccessfully: " + totalNumberSuccessfully);
                    System.out.println(tab + "TOTAL: totalNumberErrors: " + totalNumberErrors);
                    System.out.println(tab + "TOTAL: totalNumberNotFound: " + totalNumberNotFound);
                    System.out.println(tab + "TOTAL: totalNumberParameterIsInvalid: " + totalNumberParameterIsInvalid);
                    System.out.println(tab + "TOTAL: totalNumberUnknownException: " + totalNumberUnknownException);
                    System.out.println(tab + "TOTAL: totalNumberCaptchaInvalid: " + totalNumberCaptchaInvalid);
                    System.out.println(tab + "TOTAL: totalNumberDataResponseIsNull: " + totalNumberDataResponseIsNull);

                    contentInformation += ("\n\n" + tab + "TOTAL: Successfully got all business tax code with folder path " + PATH_FOLDER + ". [" + folderCount + "]");
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberTaxCode: " + totalNumberTaxCode);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberSuccessfully: " + totalNumberSuccessfully);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberErrors: " + totalNumberErrors);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberNotFound: " + totalNumberNotFound);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberParameterIsInvalid: " + totalNumberParameterIsInvalid);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberUnknownException: " + totalNumberUnknownException);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberCaptchaInvalid: " + totalNumberCaptchaInvalid);
                    contentInformation += ("\n" + tab + "TOTAL: totalNumberDataResponseIsNull: " + totalNumberDataResponseIsNull);

                    writeToFile("INFORMATION_FILTER_TAX_CODE.txt", contentInformation);
                }
            } else {
                System.out.println("No files found in directory: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Directory does not exist or is not a valid directory");
        }
    }

    private static void loadDataInFIle(int level) {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
            numberSuccessfully = 0;
            numberErrors = 0;
            numberNotFound = 0;
            numberParameterIsInvalid = 0;
            numberUnknownException = 0;
            numberCaptchaInvalid = 0;
            numberDataResponseIsNull = 0;
            numberRemoveTaxCode = 0;
            numberTaxCode = 0;

            String line;
            boolean isHandle = false;
            lineNumber = 0;

            while ((line = br.readLine()) != null) {
                handleLine(line);

                if (mst != null && !mst.isEmpty() && message != null && !message.isEmpty()) {
                    if (START_MST != null && !START_MST.isEmpty()) {
                        if (mst.equals(START_MST) && !isHandle) {
                            System.out.println("\tStart processing from TAX CODE: " + line + "...");
                            isHandle = true;
                        }
                    } else if (!isHandle) {
                        System.out.println("\tStart processing from the first TAX CODE...");
                        isHandle = true;
                    }

                    if (END_MST != null && !END_MST.isEmpty() && mst.equals(END_MST)) {
                        System.out.println("DONE: Retrieved information to TAX CODE: " + mst);
                        System.exit(0);
                    }

                    if (isHandle) {
                        if ((previousMST == null || previousMST.isEmpty()) || !previousMST.equals(mst)) {
                            numberTaxCode++;
//                            LocalDateTime now = LocalDateTime.now();
//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                            String formattedDateTime = now.format(formatter);

                            if (isSuccess) {
                                writeToFile("./SUCCESSFULLY.txt", mst);

                                String mess = "MESSAGE: " + message + " -- MST = " + mst + " -- INDEX = " + index;
                                String content = (dtisId != null && !dtisId.isEmpty()) ? mess + " -- DTIS_ID = " + dtisId : mess;
                                writeToFile("./DETAIL_SUCCESSFULLY.txt", content);

                                numberSuccessfully++;
                            } else {
                                String mess = "MESSAGE: " + message + " -- MST = " + mst + " -- INDEX = " + index;
//                                String content = formattedDateTime + " - " + mess;

                                writeToFile("./ALL_ERROR.txt", mess);
                                numberErrors++;

                                if (message.contains("PARAMETER IS INVALID")) {
                                    writeToFile("./PARAMETER_IS_INVALID.txt", mst);
                                    numberParameterIsInvalid++;
                                } else if (message.contains("UNKNOWN EXCEPTION")) {
                                    writeToFile("./UNKNOWN_EXCEPTION.txt", mst);
                                    numberUnknownException++;
                                } else if (message.contains("ERROR CONNECTING TO ENTITY, PLEASE TRY AGAIN LATER")) {
                                    writeToFile("./CAPTCHA_INVALID.txt", mst);
                                    numberCaptchaInvalid++;
                                } else if (message.contains("BUSINESS INFORMATION NOT FOUND") || message.contains("NOT FOUND")) {
                                    writeToFile("./NOT_FOUND.txt", mst);
                                    numberNotFound++;
                                } else if (message.contains("DATA RESPONSE IS NULL")) {
                                    writeToFile("./DATA_RESPONSE_IS_NULL.txt", mst);
                                    numberDataResponseIsNull++;
                                }
//                                else {
//                                    System.out.println("ERROR MESSAGE: " + message);
//                                }
                            }

                            previousMST = mst;
                        } else
                            numberRemoveTaxCode++;
                    }

                    mst = "";
                    index = "";
                    message = "";
                    dtisId = "";
                }

                lineNumber++;
            }

            if (!isHandle) {
                System.out.println("ERROR: Not found tax code: " + START_MST + " with path " + PATH_FILE);
                System.exit(0);
            }

//            numberTaxCode = lineNumber - numberRemoveTaxCode;
//            String tab = (level != 0) ? "\t".repeat(level) : "";
            String tab = createTab(level);
            System.out.println(tab + "DONE: Successfully got all business tax code with file path: " + PATH_FILE + ". [" + itemFile + "]");
            System.out.println(tab + "DONE: numberTaxCode: " + numberTaxCode);
            System.out.println(tab + "DONE: numberSuccessfully: " + numberSuccessfully);
            System.out.println(tab + "DONE: numberErrors: " + numberErrors);
            System.out.println(tab + "DONE: numberNotFound: " + numberNotFound);
            System.out.println(tab + "DONE: numberParameterIsInvalid: " + numberParameterIsInvalid);
            System.out.println(tab + "DONE: numberUnknownException: " + numberUnknownException);
            System.out.println(tab + "DONE: numberCaptchaInvalid: " + numberCaptchaInvalid);
            System.out.println(tab + "DONE: numberDataResponseIsNull: " + numberDataResponseIsNull);

            contentInformation += ("\n\n" + tab + "DONE: Successfully got all business tax code with path " + PATH_FILE + ". [" + itemFile + "]");
            contentInformation += ("\n" + tab + "DONE: numberTaxCode: " + numberTaxCode);
            contentInformation += ("\n" + tab + "DONE: numberSuccessfully: " + numberSuccessfully);
            contentInformation += ("\n" + tab + "DONE: numberErrors: " + numberErrors);
            contentInformation += ("\n" + tab + "DONE: numberNotFound: " + numberNotFound);
            contentInformation += ("\n" + tab + "DONE: numberParameterIsInvalid: " + numberParameterIsInvalid);
            contentInformation += ("\n" + tab + "DONE: numberUnknownException: " + numberUnknownException);
            contentInformation += ("\n" + tab + "DONE: numberCaptchaInvalid: " + numberCaptchaInvalid);
            contentInformation += ("\n" + tab + "DONE: numberDataResponseIsNull: " + numberDataResponseIsNull);

            sumNumberSuccessfully += numberSuccessfully;
            sumNumberErrors += numberErrors;
            sumNumberNotFound += numberNotFound;
            sumNumberParameterIsInvalid += numberParameterIsInvalid;
            sumNumberUnknownException += numberUnknownException;
            sumNumberCaptchaInvalid += numberCaptchaInvalid;
            sumNumberDataResponseIsNull += numberDataResponseIsNull;
            sumNumberTaxCode += numberTaxCode;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleLine(String line) {
        message = null;
        index = null;
        mst = null;
        dtisId = null;
        isSuccess = false;

        if (line.contains("BUG")) {
            String regex = "BUG: (.*?)\\s*(--\\s*)?INDEX = (\\d+) -- MST = (.*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                message = matcher.group(1);
                index = matcher.group(3);
                mst = matcher.group(4);

            } else { // cần xem xét. Có thể ko cần
                System.out.println("No match found for line: " + line);
                String[] sLine = line.split("MST = ");
                if (sLine.length == 2 && sLine[1] != null && !sLine[1].isEmpty())
                    System.exit(0);
            }

        }
//        else if (!line.contains("MST")) {

            //            Xu ly cho file warning
//            if (line.length() <= 9) {
//                message = "PARAMETER IS INVALID";
//            } else
//                message = "UNKNOWN EXCEPTION || ERROR CONNECTING TO ENTITY, PLEASE TRY AGAIN LATER";
//
//            index = null;
//
//            Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} - )?(\\d+)$");
//            Matcher matcher = pattern.matcher(line);
//
//            if (matcher.matches()) {
//                mst = matcher.group(2);  // Trả về MST
//            } else {
//                System.out.println("No match found for line: " + line);
//                mst = null;  // Trả về null nếu không khớp
//                System.exit(0);
//            }

//            mst = line;
//        }
        else if (line.contains("INDEX")){
            isSuccess = true;
            String regex = "INDEX = (\\d+) -- MST = (.*)( -- DTIS_ID = (\\S+))?";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                index = matcher.group(1);
                mst = matcher.group(2);
                message = "SUCCESSFULLY";
                dtisId = matcher.group(4) != null ? matcher.group(4) : "";
            }
        }

        if (mst != null && mst.length() == 13) {
            String part1 = mst.substring(0, 10);
            String part2 = mst.substring(10);

            mst = part1 + "-" + part2;
        }
    }

    private static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("ERROR WRITE FILE: " + fileName + " with error: " + e.getMessage());
        }
    }

    private static String createTab(int level) {
        String tab = "";
        if ((level - 1) != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++) {
                sb.append("\t");
            }
            tab = sb.toString();
        }
        return tab;
    }
}
