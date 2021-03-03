package com.EandI.ProjectD;

import java.io.*;
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

    public static void main(String[] args) {

        int fileCount = 780;

        for(int i = 0; i < fileCount; i++) {
            try {
                File file = new File("src/com/EandI/ProjectD/raw/tempStats (" + (i + 1) + ").txt");
                FileWriter fileWriter = new FileWriter("src/com/EandI/ProjectD/clean/FinalTempData.txt", true);
                Scanner scanner = new Scanner(file);

                //Get Name of data location
                String fileData = (scanner.nextLine()).replaceAll(" ", "");
                String title = splitString(fileData, ",",1) + " " + splitString(fileData, ",", 2);
                fileWriter.write("\n" + "-----" + title + "-----");

                skipLine(scanner, 3);

                //Get year and temp data
                while (scanner.hasNext()) {
                    fileData = (scanner.nextLine()).replaceAll(" ", "");
                    String year = splitString(fileData, ",", 0);
                    String annualTemp = splitString(fileData, ",", 25);

                    if (annualTemp.equals("-9999.9")) {
                        annualTemp = "NA";
                    }
                    fileWriter.write("\n" + year + ": " + annualTemp);
                }
                scanner.close();
                fileWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found!");
            } catch (IOException e) {
                System.out.println("Error: Failed to write to file!");
            }
        }
    }
}
