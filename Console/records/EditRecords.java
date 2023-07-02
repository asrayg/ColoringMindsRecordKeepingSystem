package records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EditRecords {
	public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

		@SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
		System.out.println("Enter the name you'd like to edit");
		String option = scnr.next();

		while (!checkName(option)) {
			System.out.println("Name not found. Please try again or enter 'q' to exit.");
			System.out.println("Enter the name you'd like to edit");
			option = scnr.next();

			
			if (option.equalsIgnoreCase("q")) {
				System.out.println("Exiting the program...");
				RecordsMain.main(args);
			}
		}

		if (!checkValidity(option)) {
			System.out.println("Name not valid. Please renew to edit.");
			RecordsMain.main(args);
		}

		getName(option);

		File recordsFile = new File("records.txt");
		File attendanceFile = new File("attendance.txt");
		File feesFile = new File("fees.txt");
		File paymentFile = new File("payment.txt");
		String tempRecordsFile = "temp_records.txt";
		String tempAttendanceFile = "temp_attendance.txt";
		String tempFeesFile = "temp_fees.txt";
		String tempPaymentFile = "temp_payment.txt";

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
				BufferedReader brFees = new BufferedReader(new FileReader(feesFile));
				BufferedReader brPayment = new BufferedReader(new FileReader(paymentFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);
				FileWriter fwAttendance = new FileWriter(tempAttendanceFile, true);
				FileWriter fwFees = new FileWriter(tempFeesFile, true);
				FileWriter fwPayment = new FileWriter(tempPaymentFile, true)) {

			System.out.println("Enter the field you'd like to edit:");
			System.out.println(
					"phno (1), start date (2), age (3), batch type (4), mode (5), fees per hour (6), email (7)");
			int editOption = scnr.nextInt();

			String editValue = "";

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			@SuppressWarnings({ "unused", "resource" })
			Scanner scanner = new Scanner(System.in);

			try {
				if (editOption == 1) {
					System.out.println("Ph No: ");
					String phno = "";

					boolean isValidPhNo = false;
					while (!isValidPhNo) {
						phno = reader.readLine();
						if (phno.matches("\\d+")) {
							if (phno.length() == 10) {
								editValue = phno;
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
				} else if (editOption == 2) {
					System.out.println("Start Date:");
					@SuppressWarnings("unused")
					String date = "";

					boolean isValidDate = false;
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					String date1 = "";

					while (!isValidDate) {
					    date1 = reader.readLine();
					    try {
					        LocalDate parsedDate = LocalDate.parse(date1, dateFormatter); 
					        editValue = parsedDate.format(dateFormatter);
					        isValidDate = true; 
					    } catch (DateTimeParseException e) {
					        System.out.println("Invalid date format. Please enter a valid date in the format (dd-MM-yyyy).");
					        System.out.println("Start Date:");
					    }
					}

				} else if (editOption == 4) {
					System.out.println("Batch Type:");
					String batchType = "";

					boolean isValidBatchType = false;
					while (!isValidBatchType) {
						batchType = reader.readLine().toUpperCase();
						if (batchType.matches("[MTWRFSU1234567890]+")) {
							editValue = batchType;
							isValidBatchType = true;
						} else {
							System.out.println("Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9 only.");
							System.out.println("Batch Type:");
						}
					}
				} else if (editOption == 5) {
					System.out.println("Mode of Instruction:");
					String mode = "";

					boolean isValidMode = false;
					while (!isValidMode) {
						mode = reader.readLine();
						if (mode.equalsIgnoreCase("Online") || mode.equalsIgnoreCase("Offline")
								|| mode.equalsIgnoreCase("Combined")) {
							editValue = mode;
							isValidMode = true;
						} else {
							System.out
							.println("Invalid mode of instruction. Please enter Online, Offline, or Combined.");
							System.out.println("Mode of Instruction:");
						}
					}
				} else if (editOption == 3) {
					System.out.println("Age: ");
					int age = 0;
					boolean isValidAge = false;
					while (!isValidAge) {
						try {
							age = Integer.parseInt(reader.readLine());
							editValue = String.valueOf(age);
							isValidAge = true;
						} catch (NumberFormatException e) {
							System.out.println("Invalid age. Please enter a valid number.");
							System.out.println("Age: ");
						}
					}
				} else if (editOption == 6) {
					System.out.println("Fees per hour: ");
					double fees = 0.0;
					boolean isValidFees = false;
					while (!isValidFees) {
						try {
							fees = Double.parseDouble(reader.readLine());
							editValue = String.valueOf(fees);
							isValidFees = true;
						} catch (NumberFormatException e) {
							System.out.println("Invalid fees. Please enter a valid number.");
							System.out.println("Fees per hour: ");
						}
					}
				} else if (editOption == 7) {
					System.out.println("Email: ");
					String email = "";

					boolean isValidEmail = false;
					while (!isValidEmail) {
						email = reader.readLine();
						if (email.matches(".+@.+\\.com")) {
							editValue = email;
							isValidEmail = true;
						} else {
							System.out.println("Invalid email format. Please enter a valid email address.");
							System.out.println("Email: ");
						}
					}
				}

				confirmDeclare();
	            	
				String lineRecords;
				while ((lineRecords = brRecords.readLine()) != null) {
					String[] partsRecords = lineRecords.split(", ");
					if (partsRecords.length > 0 && partsRecords[0].trim().equals(option)) {
						partsRecords[editOption] = editValue;
					}

					fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
				}

				String lineAttendance;
				while ((lineAttendance = brAttendance.readLine()) != null) {
					String[] partsAttendance = lineAttendance.split(", ");
					if (partsAttendance.length > 0 && partsAttendance[0].trim().equals(option) && editOption == 1) {
						partsAttendance[editOption] = editValue;
					} else if (partsAttendance.length > 0 && partsAttendance[0].trim().equals(option)
							&& editOption == 4) {
						partsAttendance[2] = editValue;
					}

					fwAttendance.write(String.join(", ", partsAttendance) + System.lineSeparator());
				}

				String lineFees;
				while ((lineFees = brFees.readLine()) != null) {
					String[] partsFees = lineFees.split(", ");
					if (partsFees.length > 0 && partsFees[0].trim().equals(option) && editOption == 1) {
						partsFees[editOption] = editValue;
					}

					fwFees.write(String.join(", ", partsFees) + System.lineSeparator());
				}

				String linePayment;
				while ((linePayment = brPayment.readLine()) != null) {
					String[] partsPayment = linePayment.split(", ");
					if (partsPayment.length > 0 && partsPayment[0].trim().equals(option) && editOption == 1) {
						partsPayment[editOption] = editValue;
					}

					fwPayment.write(String.join(", ", partsPayment) + System.lineSeparator());
				}

				fwRecords.flush();
				fwRecords.close();
				fwAttendance.flush();
				fwAttendance.close();
				fwFees.flush();
				fwFees.close();
				fwPayment.flush();
				fwPayment.close();

				File tempRecords = new File(tempRecordsFile);
				if (recordsFile.delete()) {
					tempRecords.renameTo(recordsFile);
				}

				File tempAttendance = new File(tempAttendanceFile);
				if (attendanceFile.delete()) {
					tempAttendance.renameTo(attendanceFile);
				}

				File tempFees = new File(tempFeesFile);
				if (feesFile.delete()) {
					tempFees.renameTo(feesFile);
				}

				File tempPayment = new File(tempPaymentFile);
				if (paymentFile.delete()) {
					tempPayment.renameTo(paymentFile);
				}
				System.out.println("");
				System.out.println("Edit Done");
				RecordsMain.main(args);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void confirmDeclare() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Confirm information (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
        	RecordsMain.main(null);
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


	private static void getName(String option) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length > 0 && parts[0].trim().equals(option) && !parts[parts.length - 1].equals("n")) {
					System.out.println("");
					System.out.println("Current Record:");
					System.out.println("");
					System.out.println("Name          : " + parts[0]);
			        System.out.println("Phone Number  : " + parts[1]);
			        System.out.println("Start Date    : " + parts[2]);
			        System.out.println("Age           : " + parts[3]);
			        System.out.println("Batch Type    : " + parts[4]);
			        System.out.println("Mode          : " + parts[5]);
			        System.out.println("Fees Per Hour : " + parts[6]);
			        System.out.println("Email         : " + parts[7]);
			        System.out.println("");
					break;
				}
			}
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

}
