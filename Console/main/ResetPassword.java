package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ResetPassword {
	public static void main(String[] args) {
		checkLockStatusOfSystem("password.txt");
		@SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
		System.out.println("Enter password: ");
		String passwordReplace = scnr.next();
		System.out.println("Enter password again: ");
		String passwordReplace1 = scnr.next();
		
		if(!passwordReplace.equals(passwordReplace1)) {
			System.out.println("Passwords do not match up.");
			return;
		}
		
		if(!passwordReplace.equals(getPasswordFromPasswordFile())){
			System.out.println("Password is incorrect.");
			return;
		}
		
		System.out.println("Enter new password: ");
		String passwordReplaceTo = scnr.next();
		
		System.out.println("Enter new password: ");
		String passwordReplaceTo1 = scnr.next();
		
		if(!passwordReplaceTo.equals(passwordReplaceTo1)) {
			System.out.println("Passwords do not match up.");
			return;
		}
		
		replaceFirstLine("password.txt", passwordReplaceTo);
	}
	
	private static void replaceFirstLine(String passwordFile, String newFirstLine) {
	    try {
	        Path filePath = Paths.get(passwordFile);
	        List<String> lines = Files.readAllLines(filePath);
	        
	        if (!lines.isEmpty()) {
	            lines.set(0, newFirstLine);
	            Files.write(filePath, lines);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	private static String getPasswordFromPasswordFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("password.txt"))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
