package com.EandI.ProjectD;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class CleanUp {

    //Splits string from given point, returns given index
    public static String splitString(String str, String regex, int index) {
        String[] splitArray = str.split(regex);
        return splitArray[index];
    }

    //Skips a given amount of lines
    public static void skipLine(Scanner scanner, int amount) {
        for(int i = 0; i < amount; i++) {
            scanner.nextLine();
        }
    }

    //Imma let u guess what this one does
    public static double calcPercent(double amount, double total) {
        return Math.round((amount * 100) / total);
    }

    //Tbh idk how this works but it does - never question the stack overflow gods
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void main(String[] args) {

        int fileCount = 780; // total amount of files we want to scan
        int goodData = 0;
        int missingData = 0;

        for(int i = 0; i < fileCount; i++) {
            try {
                //Scan file and create new output file if there is none
                File file = new File("src/com/EandI/ProjectD/raw/tempStats (" + (i + 1) + ").txt");
                FileWriter fileWriter = new FileWriter("src/com/EandI/ProjectD/clean/FinalTempData.txt", true);
                Scanner scanner = new Scanner(file);

                //Get Name of data location
                String fileData = (scanner.nextLine()).replaceAll(" ", "");
                String title = splitString(fileData, ",",1) + " " + splitString(fileData, ",", 2);
                fileWriter.write("\n" + "-----" + title + "-----");

                skipLine(scanner, 3);

                while (scanner.hasNext()) {
                    //Get year and temp data
                    fileData = (scanner.nextLine()).replaceAll(" ", "");
                    String year = splitString(fileData, ",", 0);
                    double annualTemp = Double.parseDouble(splitString(fileData, ",", 25));

                    //Some fancy optimizations to extract MORE DATA
                    if (annualTemp == -9999.9) {
                        double winter = Double.parseDouble(splitString(fileData, ",", 27));
                        double spring = Double.parseDouble(splitString(fileData, ",", 29));
                        double summer = Double.parseDouble(splitString(fileData, ",", 31));
                        double autumn = Double.parseDouble(splitString(fileData, ",", 33));

                        if (winter == -9999.9 || spring == -9999.9 || summer == -9999.9 || autumn == -9999.9) {
                            fileWriter.write("\n" + year + ": NA");
                            missingData++;
                        }
                        else {
                            annualTemp = round((winter + spring + summer + autumn) / 4, 1);
                            fileWriter.write("\n" + year + ": " + annualTemp);
                            goodData++;
                        }
                    }
                    else {
                        //Write to file if we gucci
                        fileWriter.write("\n" + year + ": " + annualTemp);
                        goodData++;
                    }
                }
                scanner.close();
                fileWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found!");
            } catch (IOException e) {
                System.out.println("Error: Failed to write to file!");
            }
        }

        //Debug
        int totalData = goodData + missingData;
        System.out.println("Good: " + goodData + " (" + calcPercent(goodData, totalData) + "%)");
        System.out.println("Missing: " + missingData + " (" + calcPercent(missingData, totalData) + "%)");
    }
}
