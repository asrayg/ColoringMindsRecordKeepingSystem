package attendance;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DeleteAttendance {
    public static void main(String[] args) {
        checkLockStatusOfSystem("password.txt");

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the names whose attendance you want to modify (comma-separated):");
        String namesInput = scanner.nextLine();
        List<String> names = Arrays.asList(namesInput.split(", "));

        try {
            List<String> validNames = new ArrayList<>();

            for (String name : names) {
                name = name.trim();

                if (name.equalsIgnoreCase("q")) {
                    System.out.println("Exiting the program...");
                    AttendanceMain.main(args);
                }

                if (checkName(name) && checkValidity(name)) {
                    validNames.add(name);
                } else if (!checkValidity(name)) {
                    System.out.println(name + " is not valid. Please renew/register " + name + " to view.");
                } 
            }

            if (validNames.isEmpty()) {
                System.out.println("No valid names found. Exiting the program...");
                AttendanceMain.main(args);
            }
            
            getPreviousAttendance(validNames);

            System.out.println("Enter the date you want to delete from attendance (dd-MM-yyyy) or 'l' to delete the last date:");
            String dateToDelete = scanner.nextLine();

            boolean isValidDate = false;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (!isValidDate) {
                if (dateToDelete.equalsIgnoreCase("l")) {
                    isValidDate = true;
                } else {
                    try {
                        LocalDate.parse(dateToDelete, dateFormatter);
                        isValidDate = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Please enter a valid date in the format dd-MM-yyyy or 'l' to delete the last date.");
                        System.out.println("Type the date you want to add attendance for:");
                        dateToDelete = scanner.nextLine();
                    }
                }
            }
            
            confirmDeclare();


            File file = new File("attendance.txt");
            String tempFile = "temp.txt";

            try (BufferedReader br = new BufferedReader(new FileReader(file));
                 FileWriter fw = new FileWriter(tempFile, true)) {

                List<String> modifiedLines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(", ");
                    boolean tempVariable = false;
                    if (parts.length >= 5 && validNames.contains(parts[0].trim()) && parts[3].equals("y")) {
                    	if(!dateToDelete.equals("l")) {
                    		if (!checkDate(parts[0], parts, dateToDelete)) {
                    			System.out.println("There was no attendance for " + parts[0] + " on " + dateToDelete + " to delete" );
                    		}
                    	}
                    	
                        if (dateToDelete.equals("l")) {
                        	tempVariable = true;
                            dateToDelete = parts[parts.length - 1];
                        }
                        List<String> dates = new ArrayList<>();
                        for (int i = 4; i < parts.length; i++) {
                            if (!parts[i].trim().equals(dateToDelete)) {
                                dates.add(parts[i]);
                            }
                        }
                        parts = Arrays.copyOf(parts, 4 + dates.size());
                        for (int i = 0; i < dates.size(); i++) {
                            parts[4 + i] = dates.get(i);
                        }
                    }

                    modifiedLines.add(String.join(", ", parts));
                    if (tempVariable) {
                    	dateToDelete = "l";
                    }
                }

                FileWriter originalFileWriter = new FileWriter(file);
                for (String modifiedLine : modifiedLines) {
                    originalFileWriter.write(modifiedLine + System.lineSeparator());
                }
                originalFileWriter.close();
                System.out.println("Attendance updated successfully.");
                AttendanceMain.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private static void getPreviousAttendance(List<String> validNames) {
        try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                for (String name : validNames) {
                    if (parts[0].contains(name) && parts.length > 4) {
                        System.out.print("Name: " + parts[0]+ "        ");
                        int numOfDates = Math.min(5, parts.length - 4);
                        System.out.print("Last " + numOfDates + " Dates Attended: ");
                        for (int i = parts.length - numOfDates; i < parts.length; i++) {
                            System.out.print(parts[i] + " ");
                        }
                        System.out.println("");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
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
    
    private static boolean checkDate(String name, String[] dates, String date) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 0 && parts[0].trim().equals(name)) {
	                for(int i = 0; i<dates.length; i++) {
                		if (dates[i].equals(date)) {
                			return true;
                		}
	                }
                }
            }
        }
        return false;
    }

    private static void confirmDeclare() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Confirm information (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
        	System.exit(0);
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


