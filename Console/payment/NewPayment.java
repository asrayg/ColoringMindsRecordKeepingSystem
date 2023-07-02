package payment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("unused")
public class NewPayment {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        checkLockStatusOfSystem("password.txt");
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        boolean validName = false;
		String studentName = "";

		while (!validName) {
			System.out.println("Enter the name of the student (or 'q' to quit):");
			studentName = scanner.nextLine();

			if (studentName.equalsIgnoreCase("q")) {
				System.out.println("Exiting the program...");
				PaymentMain.main(args);
				return;
			}

			if (checkName(studentName) && checkValidity(studentName)) {
				validName = true;
			} else if (!checkValidity(studentName)) {
				System.out.println(studentName + " is not valid. Please renew/register " + studentName + " to view.");
			} else {
				System.out.println("Invalid name. Please try again.");
			}
		}

        
        
		double amount = 0.0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Enter amount paid:");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); 
            }
        }        
        System.out.println("Enter payment type:");
        String payment = scanner.next();

        String option = "";
        boolean validInput1 = false;

        while (!validInput1) {
            System.out.println("Mark paid today or for a specific day (1/2):");
            option = scanner.next();

            if (option.equals("1") || option.equals("2")) {
                validInput1 = true;
            } else {
                System.out.println("Invalid option. Please enter 1 or 2.");
            }
        }

        
        String day;
        if (option.equals("1")) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            day = currentDate.format(formatter);
        } else {
        	day = "";
            boolean validInput2 = false;

            while (!validInput2) {
                System.out.println("Enter a specific day in the format of dd-MM-yyyy:");
                day = scanner.next();

                if (isValidDate(day)) {
                    validInput = true;
                } else {
                    System.out.println("Invalid date format. Please enter a valid date in the format of dd-MM-yyyy.");
                }
            }

        }
        
        confirmDeclare();
        
        generatePaymentReport(studentName, amount, day, payment);
        PaymentMain.main(args);
		return;
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

    private static void generatePaymentReport(String name, double amount, String day, String payment) {
        String paymentFilename = "payment.txt";
        String feesFilename = "fees.txt";
        String tempFilename = "payment_temp.txt";
        try (BufferedReader brPayment = new BufferedReader(new FileReader(paymentFilename));
             BufferedWriter bwPayment = new BufferedWriter(new FileWriter(tempFilename));
             BufferedReader brFees = new BufferedReader(new FileReader(feesFilename));
             BufferedWriter bwFees = new BufferedWriter(new FileWriter("fees_temp.txt"))) {

        	
        	String lineFees;
            boolean isFirstLineFees = true;
            while ((lineFees = brFees.readLine()) != null) {
                if (isFirstLineFees) {
                    isFirstLineFees = false;
                    bwFees.write(lineFees);
                    bwFees.newLine();
                    continue;
                }
                String[] partsFees = lineFees.split(", ");
                ArrayList<String> feeLine = new ArrayList<>();

                for (int i = 0; i < partsFees.length; i++) {
                    feeLine.add(partsFees[i]);
                }

                if (partsFees[0].equals(name)) {
                    double balance = Double.parseDouble(feeLine.get(2));
                    double temp1 = balance;
                    balance -= amount;
                    if (balance < 0) {
                    	double temp = amount-temp1;
                        System.out.println("Error: Amount paid exceeds the total balance. Exceeds by: " + temp);
                        return;
                    }
                    String stringBalance = String.format("%.2f", balance);

                    feeLine.set(2, stringBalance);
                    for (int i = 0; i < partsFees.length; i++) {
                        partsFees[i] = feeLine.get(i);
                    }
                    String newLine = Arrays.toString(partsFees).replace("[", "").replace("]", "");
                    bwFees.write(newLine);
                    bwFees.newLine();
                } else {
                    String newLine = Arrays.toString(partsFees).replace("[", "").replace("]", "");
                    bwFees.write(newLine);
                    bwFees.newLine();
                }
            }
            
            String linePayment;
            boolean isFirstLinePayment = true;
            boolean found = false;
            while ((linePayment = brPayment.readLine()) != null) {
                if (isFirstLinePayment) {
                    isFirstLinePayment = false;
                    bwPayment.write(linePayment);
                    bwPayment.newLine();
                    continue;
                }

                String[] partsPayment = linePayment.split(", ");
                String studentName = partsPayment[0];
                if (studentName.equals(name)) {
                    linePayment += ", (" + amount + ", " + day + ", " + payment +")";
                    found = true;
                }

                bwPayment.write(linePayment);
                bwPayment.newLine();
            }

            if (!found) {
                System.out.println("Name not found.");
            }

            System.out.println("Payment recorded successfully.");

            File paymentFile = new File(paymentFilename);
            File feesFile = new File(feesFilename);
            File tempPaymentFile = new File(tempFilename);
            File tempFeesFile = new File("fees_temp.txt");
            if (paymentFile.delete() && feesFile.delete() && tempPaymentFile.renameTo(paymentFile) && tempFeesFile.renameTo(feesFile)) {
                System.out.println("Payment and fees files updated successfully.");
            } else {
                System.out.println("Failed to update payment and fees files.");
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
	private static boolean checkName(String name) {
        try {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("records.txt"));
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

    private static boolean checkValidity(String option) throws FileNotFoundException, IOException {
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
    
    private static boolean isValidDate(String day) {
        if (day.length() != 10)
            return false;

        String[] parts = day.split("-");
        if (parts.length != 3)
            return false;

        int dayValue, monthValue, yearValue;
        try {
            dayValue = Integer.parseInt(parts[0]);
            monthValue = Integer.parseInt(parts[1]);
            yearValue = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (yearValue < 1000 || yearValue > 9999)
            return false;

        if (monthValue < 1 || monthValue > 12)
            return false;

        if (dayValue < 1 || dayValue > getDaysInMonth(monthValue, yearValue))
            return false;

        return true;
    }

    private static int getDaysInMonth(int month, int year) {
        int[] daysInMonth = {
                31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };
        if (isLeapYear(year))
            daysInMonth[1] = 29;

        return daysInMonth[month - 1];
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static void confirmDeclare() throws FileNotFoundException, IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Confirm information (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
        	String[] args = null;
			PaymentMain.main(args);
			return;
        } 
		
	}

    
}