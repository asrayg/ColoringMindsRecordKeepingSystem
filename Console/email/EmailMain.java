package email;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import main.CommandMain;

public class EmailMain {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

    	Scanner scnr = new Scanner(System.in);
        boolean isValidOption = false;

        while (!isValidOption) {
            System.out.println("Please choose an option:\n" +
                    "1. Generate normal email\n" +
                    "2. Send specific email\n" +
                    "3. Send emails to students with a balance\n" +
                    "4. Go back to the main menu\n" +
                    "Enter 'q' to exit");
            String option = scnr.nextLine();

            switch (option) {
                case "1":
                    GenerateEmail.main(args);
                    isValidOption = true;
                    break;
                case "2":
                    GenerateSpecificEmail.main(args);
                    isValidOption = true;
                    break;
                case "3":
                    GenerateBalanceEmail.main(args);
                    isValidOption = true;
                    break;
                case "4":
                    CommandMain.main(args);
                    isValidOption = true;
                    break;
                case "q":
                    System.out.println("Exiting the program...");
                    isValidOption = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again or enter 'q' to exit.");
            }
        }
        scnr.close();
    }
    
    private static void checkLockStatusOfSystem(String passwordFile) {
        String thirdLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
            String line;
            boolean isFirstLine = true;
            int lineCount = 0;

            while ((line = br.readLine()) != null && lineCount < 3) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else if (lineCount == 2) {
                    thirdLine = line;
                    break;
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (thirdLine.equals("locked")) {
            System.exit(0);
        }
    }

}
