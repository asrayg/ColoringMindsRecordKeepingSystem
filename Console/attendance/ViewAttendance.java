package attendance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ViewAttendance {
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
	            AttendanceMain.main(args);
			}

			if (checkName(studentName) && checkValidity(studentName)) {
				validName = true;
			} else if (!checkValidity(studentName)) {
				System.out.println(studentName + " is not valid. Please renew/register " + studentName + " to view.");
			} else {
				System.out.println("Invalid name. Please try again.");
			}
		}

		boolean validChoice = false;
		while (!validChoice) {
			System.out.println(
					"Enter 'm' to see attendance for a specific month and year, or 'y' for the entire year, or 'q' to quit:");
			String choice = scanner.nextLine();

			if (choice.equalsIgnoreCase("m")) {
				int month = 0;
				while (month < 1 || month > 12) {
					System.out.println("Enter the month (1-12):");
					try {
						month = Integer.parseInt(scanner.nextLine());
						if (month < 1 || month > 12) {
							System.out.println("Invalid month. Please enter a number between 1 and 12.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter a valid number.");
					}
				}

				System.out.println("Enter the year:");
				int year = scanner.nextInt();
				scanner.nextLine();
				printAttendanceForMonth(studentName, month, year);
				validChoice = true;
			} else if (choice.equalsIgnoreCase("y")) {
				System.out.println("Enter the year:");
				int year = scanner.nextInt();
				scanner.nextLine();
				printAttendanceForYear(studentName, year);
				validChoice = true;
			} else if (choice.equalsIgnoreCase("q")) {
				System.out.println("Exiting the program...");
	            AttendanceMain.main(args);
				validChoice = true;
			} else {
				System.out.println("Invalid choice. Please try again.");
			}
		}
        AttendanceMain.main(args);
        return;
	}

	private static void printAttendanceForMonth(String studentName, int month, int year) {
		try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
			String line;
			boolean found = false;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts[0].equals(studentName) && parts.length > 4) {
					String[] dateParts = parts[4].split("-");
					int attendanceMonth = Integer.parseInt(dateParts[1]);
					int attendanceYear = Integer.parseInt(dateParts[2]);

					if (attendanceMonth == month && attendanceYear == year) {
						System.out.println("");

						System.out.println("Name: " + parts[0] + " " + "-- Phone Number: " + parts[1]);

						System.out.println("");
						System.out.println("Dates Attended: ");
						for (int i = 4; i < parts.length; i++) {
							System.out.println(parts[i]);
						}
						found = true;
					}
				}
			}
			

			if (!found) {
				System.out.println("No attendance found for " + studentName + " in " + month + "/" + year);
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	private static void printAttendanceForYear(String studentName, int year) {
		try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
			String line;
			boolean found = false;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts[0].equals(studentName) && parts.length > 4) {
					System.out.println("");

					System.out.println("Name: " + parts[0] + " " + "-- Phone Number: " + parts[1]);

					System.out.println("");
					System.out.println("Dates Attended: ");
					for (int i = 4; i < parts.length; i++) {
						System.out.println(parts[i]);
					}
					found = true;
				}
			}

			if (!found) {
				System.out.println("No attendance found for " + studentName + " in " + year);
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	private static boolean checkName(String name) {
		try {
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
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
