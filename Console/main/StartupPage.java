package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author      Asray Gopa
 * @version     1.00
 * @since       June 10th, 2023
 */

public class StartupPage {
    private static final String PASSWORD_FILE = "password.txt";

    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem(PASSWORD_FILE);

        File passwordFile = new File(PASSWORD_FILE);
        if (passwordFile.length() == 0) {
            registerPassword();
        } else {
            verifyPassword();
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

    private static void registerPassword() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE, true))) {
            @SuppressWarnings("resource")
			Scanner scnr = new Scanner(System.in);
            System.out.println("Enter the password you want: ");
            String password = scnr.next();

            System.out.println("Enter password again: ");
            String passwordConfirm = scnr.next();

            System.out.println("Confirm information (Y/y):");
            String confirm = scnr.next();

            if (!confirm.equalsIgnoreCase("Y")) {
                return;
            }

            if (password.equals(passwordConfirm)) {
                writer.println(password);
                writer.println("0");
                writer.println("unlocked");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void verifyPassword() throws IOException {
        @SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
        System.out.println("Enter password: ");
        String passwordCheck = scnr.next();
        int counter = getNumber();

        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            @SuppressWarnings("unused")
			String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!passwordCheck.equals(getPasswordFromPasswordFile())) {
            counter++;
            if (counter >= 5) {
                lockFile();
                System.out.println("Too many incorrect attempts. System has been locked. Call customer support to reopen.");
                return;
            } else {
                System.out.println("Password is incorrect. Try again. Attempts remaining: " + (5 - counter));
                incrementCounter(counter);
                System.out.println("Enter password: ");
                passwordCheck = scnr.next();
            }
        }

        resetCounter();
        CommandMain.main(null);
    }

    private static void lockFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }

            lines[2] = "locked";

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getNumber() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            boolean isFirstLine = true;
            int lineIndex = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else if (lineIndex == 1) {
                    return Integer.parseInt(line);
                }

                lineIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void incrementCounter(int counter) {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }

            lines[1] = Integer.toString(counter);

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resetCounter() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }

            lines[1] = "0";

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPasswordFromPasswordFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
