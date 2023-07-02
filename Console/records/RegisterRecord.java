package records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RegisterRecord {
    public static void main(String[] args) {
        checkLockStatusOfSystem("password.txt");

    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        try {
        	System.out.println("Name: ");
            String name = "";
            boolean isNameValid = false;
            while (!isNameValid) {
                name = reader.readLine();
                if (checkName(name)) {
                    System.out.println("Name already exists. Please add another character to differentiate it.");
                	System.out.println("Name: ");
                } else {
                    isNameValid = true;
                }
            }
            
            System.out.println("Ph No: ");
            String phno = "";

            boolean isValidPhNo = false;
            while (!isValidPhNo) {
                phno = reader.readLine();
                if (phno.matches("\\d+")) { 
                    if (phno.length() == 10) { 
                        isValidPhNo = true;
                    } else {
                        System.out.println("Invalid phone number length. Please enter 10 digits.");
                        System.out.println("Ph No: ");
                    }
                } else {
                    System.out.println("Invalid phone number format. Please enter digits only.");
                    System.out.println("Ph No: ");
                }
            }


            System.out.println("Start Date:");
            String date = "";

            boolean isValidDate = false;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (!isValidDate) {
                date = reader.readLine();
                try {
                    LocalDate.parse(date, dateFormatter); 
                    isValidDate = true; 
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a valid date in the format (dd-MM-yyyy).");
                    System.out.println("Start Date:");
                }
            }


            System.out.println("Batch Type:"); 
            String batchType = "";

            boolean isValidBatchType = false;
            while (!isValidBatchType) {
                batchType = reader.readLine().toUpperCase();
                if (batchType.matches("[MTWRFSU1234567890]+")) {
                    isValidBatchType = true;
                } else {
                    System.out.println("Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9 only.");
                    System.out.println("Batch Type:"); 
                }
            }

            System.out.println("Mode of Instruction:"); 
            String mode = "";

            boolean isValidMode = false;
            while (!isValidMode) {
                mode = reader.readLine();
                if (mode.equalsIgnoreCase("Online") || mode.equalsIgnoreCase("Offline") || mode.equalsIgnoreCase("Combined")) {
                    isValidMode = true;
                } else {
                    System.out.println("Invalid mode of instruction. Please enter Online, Offline, or Combined.");
                    System.out.println("Mode of Instruction:"); 

                }
            }


            System.out.println("Age: ");
            int age = 0;
            boolean isValidAge = false;
            while (!isValidAge) {
                try {
                    age = Integer.parseInt(reader.readLine());
                    isValidAge = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age. Please enter a valid number.");
                    System.out.println("Age: ");
                }
            }

            System.out.println("Fees per hour: ");
            double fees = 0.0;
            boolean isValidFees = false;
            while (!isValidFees) {
                try {
                    fees = Double.parseDouble(reader.readLine());
                    isValidFees = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid fees. Please enter a valid number.");
                    System.out.println("Fees per hour: ");

                }
            }

            System.out.println("Email: "); 
            String email = "";

            boolean isValidEmail = false;
            while (!isValidEmail) {
                email = reader.readLine();
                if (email.matches(".+@.+\\.com")) {
                    isValidEmail = true;
                } else {
                    System.out.println("Invalid email format. Please enter a valid email address.");
                    System.out.println("Email: "); 

                }
            }

            System.out.println("Confirm information (Y/N):");
            String confirm = scanner.next();

            if (confirm.equalsIgnoreCase("Y")) {
                addToFile(name, phno, date, batchType, mode, age, fees, email);
                System.out.println("Record added successfully.");
            } else {
                System.out.println("Record not added. Confirmation declined.");
            }
            RecordsMain.main(args);
        } catch (IOException e) {
            System.out.println(e);
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

    private static boolean checkName(String name) {
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
				
				if(nameLine.equals(name)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void addToFile(String name, String phno, String date, String batchType, String mode, int age, double fees, String email) {
        try {
            FileWriter writer = new FileWriter("records.txt", true);
            if (new File("records.txt").length() == 0) {
                writer.write("name, phno, start date, age, batch type, mode, fees per hour, email, validity\n");
            }
            writer.write(name + ", ");
            writer.write(phno + ", ");
            writer.write(date + ", ");
            writer.write(age + ", ");
            writer.write(batchType + ", ");
            writer.write(mode + ", ");
            writer.write(fees + ", ");
            writer.write(email + ", ");
            writer.write("y"+ "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            FileWriter writer = new FileWriter("fees.txt", true);
            if (new File("fees.txt").length() == 0) {
                writer.write("name, phno, total balance, (Month-Year, fees per hour, number of classes, balance)\n");
            }
            writer.write(name + ", ");
            writer.write(phno + ", ");
            writer.write("0.00");
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            FileWriter writer = new FileWriter("attendance.txt", true);
            if (new File("attendance.txt").length() == 0) {
                writer.write("name, phno, batch type, validity, dates attended\n");
            }
            writer.write(name + ", ");
            writer.write(phno + ", ");
            writer.write(batchType + ", ");
            writer.write("y, ");
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        
        try {
            FileWriter writer = new FileWriter("payment.txt", true);
            if (new File("payment.txt").length() == 0) {
                writer.write("name, phno, (amount paid, date paid, payment type)\n");
            }
            writer.write(name + ", ");
            writer.write(phno + ", ");
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
