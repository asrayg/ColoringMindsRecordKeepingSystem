package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import attendance.AttendanceMain;
import email.EmailMain;
import fees.FeesMain;
import payment.PaymentMain;
import records.RecordsMain;

public class CommandMain {
	public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

        @SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
        boolean isValidOption = false;

        while (!isValidOption) {
            System.out.println("Please choose an option:\n" +
                    "1. Records\n" +
                    "2. Attendance\n" +
                    "3. Fees\n" +
                    "4. Payment\n" +
                    "5. Email\n" +
                    "Enter 'q' to exit");
            String option = scnr.nextLine();

            switch (option) {
                case "1":
                    RecordsMain.main(args);
                    isValidOption = true;
                    
                    break;
                case "2":
                    AttendanceMain.main(args);
                    isValidOption = true;
                    break;
                case "3":
                    FeesMain.main(args);
                    isValidOption = true;
                    break;
                case "4":
                    PaymentMain.main(args);
                    isValidOption = true;
                    break;
                case "5":
                    EmailMain.main(args);
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
