package fees;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GenerateFees {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        int month = 0;
        int year = 0;

        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter the month (1-12) or 'q' to quit:");
            String monthInput = scanner.nextLine();

            if (monthInput.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                FeesMain.main(args);
            }

            try {
                month = Integer.parseInt(monthInput);
                if (month < 1 || month > 12) {
                    System.out.println("Invalid month. Please enter a number between 1 and 12.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.println("Enter the year or 'q' to quit:");
            String yearInput = scanner.nextLine();

            if (yearInput.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                FeesMain.main(args);
            }

            try {
                year = Integer.parseInt(yearInput);
                if (year < 1000 || year > LocalDate.now().getYear()) {
                    System.out.println("Invalid year. Please enter a 4-digit year not exceeding the current year.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }


        confirmDeclare();
        
        generateFeeReport(month, year);
        FeesMain.main(args);

    }

    private static void generateFeeReport(int month, int year) {
        try (BufferedReader brAttendance = new BufferedReader(new FileReader("attendance.txt"));
             BufferedReader brRecords = new BufferedReader(new FileReader("records.txt"))) {

            List<String> attendanceLines = new ArrayList<>();
            String lineAttendance;
            boolean isFirstLine = true;
            while ((lineAttendance = brAttendance.readLine()) != null) {
            	if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }
                attendanceLines.add(lineAttendance);
            }
            
            String lineRecords;
            isFirstLine = true;
            while ((lineRecords = brRecords.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                String[] partsRecords = lineRecords.split(", ");
                String studentName = partsRecords[0];
                String phoneNumber = partsRecords[1];
                
                String feesPerHour = partsRecords[6];

                int numberOfClasses = getNumberOfClasses(month, year, attendanceLines, studentName);
                double balance = Double.parseDouble(feesPerHour) * numberOfClasses;

                String monthYear = String.format("%02d-%04d", month, year);
                String feeReport = ", (" + monthYear + ", " + feesPerHour + ", " + numberOfClasses + ", " + balance + ")";

                if (checkValidity(studentName)) {
                	updateFeeReport(studentName, phoneNumber, feeReport);
                }
            }

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateFeeReport(String studentName, String phoneNumber, String feeReport) throws IOException {
        File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
            	
                if (line.startsWith(studentName + ", " + phoneNumber)) {
                    String[] parts = line.split(", ");
                    
                    for(int i = 0; i<parts.length; i++) {
                    	if(feeReport.substring(2,10).equals(parts[i])){
                    		System.out.println("Fee report already exists.");
                            String[] args = null;
							FeesMain.main(args );
                    	}
                    }
                    double currentBalance = Double.parseDouble(parts[2]);

                    line += feeReport;

                    double newBalance = 0.0;
                    String[] feeParts = feeReport.split(", ");
                    for (int i = 2; i < feeParts.length; i += 4) {
                        double feePerHour = Double.parseDouble(feeParts[i]);
                        int numberOfClasses = Integer.parseInt(feeParts[i + 1]);
                        double balance = feePerHour * numberOfClasses;
                        newBalance += balance;
                    }
                    double totalBalance = currentBalance + newBalance;

                    line = line.replace(", " + currentBalance + ", ", ", " + String.format("%.2f", newBalance) + ", ");
                    line = line.replace(parts[2], String.format("%.2f", totalBalance));
                }
                bw.write(line);
                bw.newLine();
            }
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }


    private static int getNumberOfClasses(int month, int year, List<String> attendanceLines, String studentName) {
        int classCount = 0;

        for (String attendanceLine : attendanceLines) {
            String[] partsAttendance = attendanceLine.split(", ");
            String currentStudentName = partsAttendance[0];

            if (currentStudentName.equals(studentName)) {
                String[] datesAttended = Arrays.copyOfRange(partsAttendance, 4, partsAttendance.length);

                for (String dateAttended : datesAttended) {
                    String[] dateParts = dateAttended.split("-");
                    int attendanceMonth = Integer.parseInt(dateParts[1]);
                    int attendanceYear = Integer.parseInt(dateParts[2]);

                    if (attendanceMonth == month && attendanceYear == year) {
                        classCount++;
                    }
                }
            }
        }

        return classCount;
    }

    private static void confirmDeclare() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Confirm information (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
            String[] args = null;
			FeesMain.main(args);
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


}
