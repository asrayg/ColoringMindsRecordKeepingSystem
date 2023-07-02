package records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DeleteRecords {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Delete or Renew (D/R) or Quit (Q)");
        String option = scanner.next().toLowerCase();
        File recordsFile = new File("records.txt");
        File attendanceFile = new File("attendance.txt");
        String tempRecordsFile = "temp_records.txt";
        String tempAttendanceFile = "temp_attendance.txt";

        while (!option.equals("d") && !option.equals("r") && !option.equals("q")) {
            System.out.println("Invalid option. Please enter 'd' to delete, 'r' to renew, or 'q' to quit.");
            option = scanner.next().toLowerCase();
        }

        if (option.equals("d")) {
            deleteRecords(scanner, recordsFile, attendanceFile, tempRecordsFile, tempAttendanceFile);
        } else if (option.equals("r")) {
            renewRecords(scanner, recordsFile, attendanceFile, tempRecordsFile, tempAttendanceFile);
        } else if (option.equals("q")) {
            System.out.println("Exiting the program...");
            RecordsMain.main(args);
        }

        scanner.close();
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

    private static void deleteRecords(Scanner scanner, File recordsFile, File attendanceFile, String tempRecordsFile,
                                      String tempAttendanceFile) throws IOException {
        System.out.println("Enter the name you would like to delete");
        String nameDelete = scanner.next();

        String[] args = null;
		while (!checkName(nameDelete)) {
            System.out.println("Name not found. Please try again or enter 'q' to exit.");
            System.out.println("Enter the name you'd like to delete");
            nameDelete = scanner.next();

            if (nameDelete.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                RecordsMain.main(args);
            }
        }

        if (checkBalance(nameDelete)) {
            RecordsMain.main(args);
        }

        if (!checkValidity(nameDelete)) {
            System.out.println("Name has already been deleted");
            RecordsMain.main(args);
        }

        confirmDeclare(scanner, args);

        try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
             BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
             FileWriter fwRecords = new FileWriter(tempRecordsFile, true);
             FileWriter fwAttendance = new FileWriter(tempAttendanceFile, true)) {

            String recordsLine;
            String attendanceLine;

            while ((recordsLine = brRecords.readLine()) != null && (attendanceLine = brAttendance.readLine()) != null) {
                String[] recordsParts = recordsLine.split(", ");
                String[] attendanceParts = attendanceLine.split(", ");
                if (recordsParts.length > 0 && recordsParts[0].trim().equals(nameDelete)) {
                    recordsParts[recordsParts.length - 1] = "n";
                    attendanceParts[3] = "n";
                }

                fwRecords.write(String.join(", ", recordsParts) + System.lineSeparator());
                fwAttendance.write(String.join(", ", attendanceParts) + System.lineSeparator());
            }
        }

        File tempRecords = new File(tempRecordsFile);
        File tempAttendance = new File(tempAttendanceFile);
        if (recordsFile.delete() && attendanceFile.delete()) {
            tempRecords.renameTo(recordsFile);
            tempAttendance.renameTo(attendanceFile);
        }

        RecordsMain.main(args);
    }

    private static void renewRecords(Scanner scanner, File recordsFile, File attendanceFile, String tempRecordsFile,
                                     String tempAttendanceFile) throws IOException {
        System.out.println("Enter the name you would like to renew");
        String nameRenew = scanner.next();

        String[] args = null;
		while (!checkName(nameRenew)) {
            System.out.println("Name not found. Please try again or enter 'q' to exit.");
            System.out.println("Enter the name you'd like to delete");
            nameRenew = scanner.next();

            if (nameRenew.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                RecordsMain.main(args);
            }
        }

        if (checkValidity(nameRenew)) {
            System.out.println("Name is already active");
            RecordsMain.main(args);
        }

        confirmDeclare(scanner, args);

        try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
             BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
             FileWriter fwRecords = new FileWriter(tempRecordsFile, true);
             FileWriter fwAttendance = new FileWriter(tempAttendanceFile, true)) {

            String recordsLine;
            String attendanceLine;

            while ((recordsLine = brRecords.readLine()) != null && (attendanceLine = brAttendance.readLine()) != null) {
                String[] recordsParts = recordsLine.split(", ");
                String[] attendanceParts = attendanceLine.split(", ");
                if (recordsParts.length > 0 && recordsParts[0].trim().equals(nameRenew)) {
                    recordsParts[recordsParts.length - 1] = "y";
                    attendanceParts[3] = "y";
                }

                fwRecords.write(String.join(", ", recordsParts) + System.lineSeparator());
                fwAttendance.write(String.join(", ", attendanceParts) + System.lineSeparator());
            }
        }

        File tempRecords = new File(tempRecordsFile);
        File tempAttendance = new File(tempAttendanceFile);
        if (recordsFile.delete() && attendanceFile.delete()) {
            tempRecords.renameTo(recordsFile);
            tempAttendance.renameTo(attendanceFile);
        }

        RecordsMain.main(args);
    }

    private static boolean checkBalance(String name) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("fees.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 0 && parts[0].trim().equals(name)) {
                    if (!parts[2].equals("0.00") ) {
                        System.out.println(name + " has a balance of Rs. " + parts[2] + ". Record cannot be deleted without payment");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void confirmDeclare(Scanner scanner, String[] args) throws IOException {
        System.out.println("Confirm (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
            RecordsMain.main(args);
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
}

