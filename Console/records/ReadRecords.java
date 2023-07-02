package records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadRecords {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        checkLockStatusOfSystem("password.txt");

    	File file = new File("records.txt");

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name(s) you'd like to look up (separated by commas) or type 'all':");
        String input = scanner.nextLine();
        System.out.println();

        String[] selectedNames = input.split(",");
        List<String> validNames = new ArrayList<>();

        for (String name : selectedNames) {
            name = name.trim();

            if (name.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                System.exit(0);
            }

            if (checkName(name) && checkValidity(name)) {
                validNames.add(name);
            } else if (checkValidity(name) == false) {
            	System.out.println("Name is not valid. Please renew/register " + name + " to view");
            } 
        }

        if (validNames.isEmpty()) {
            System.out.println("No valid names found. Exiting the program...");
            RecordsMain.main(args);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            if (validNames.contains("all")) {
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] parts = line.split(", ");
                    if (parts.length > 0 && !parts[parts.length - 1].equals("n")) {
                        printRecord(parts);
                    }
                }
            } else {
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length > 0 && validNames.contains(parts[0].trim()) && !parts[parts.length - 1].equals("n")) {
                        printRecord(parts);
                    }
                }
            }
            RecordsMain.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printRecord(String[] parts) {
        System.out.println("");
        System.out.println("Name          : " + parts[0]);
        System.out.println("Phone Number  : " + parts[1]);
        System.out.println("Start Date    : " + parts[2]);
        System.out.println("Age           : " + parts[3]);
        System.out.println("Batch Type    : " + parts[4]);
        System.out.println("Mode          : " + parts[5]);
        System.out.println("Fees Per Hour : " + parts[6]);
        System.out.println("Email         : " + parts[7]);
        System.out.println("Validity      : y");
        System.out.println("");
    }

    private static boolean checkName(String name) {
        if (name.equals("all")) {
            return true;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            boolean isFirstLinePayment = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLinePayment) {
                    isFirstLinePayment = false;
                    continue;
                }

                String[] parts = line.split(", ");
                String nameLine = parts[0];

                if (nameLine.equals(name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    
    private static boolean checkValidity(String option) throws FileNotFoundException, IOException {
    	if (option.equals("all")) {
            return true;
        }
    	
    	try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length > 0 && parts[0].trim().equals(option)) {
					if (parts[8].equals("y")) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
