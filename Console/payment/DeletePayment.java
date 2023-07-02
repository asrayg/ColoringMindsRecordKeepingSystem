package payment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DeletePayment {
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

		getPaymentDetails(studentName);
		
		System.out.println("Mark the date of the payment report you'd like to delete:");
		String date = scanner.nextLine();

		
		confirmDeclare();
		
		deletePaymentReport(studentName, date);
		PaymentMain.main(args);
		return;
	}

	private static void getPaymentDetails(String names) {
		try (BufferedReader br = new BufferedReader(new FileReader("payment.txt"))) {
            String line;
            System.out.println("");
            System.out.println("Latest Fee Reports for " +names + ": ");
            System.out.println("Name  || Payment Report: (amount paid, date paid, payment type)");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                
                    if (parts[0].equals(names)) {
                        System.out.print(parts[0] + " || ");
                        if (parts.length >= 11) {
                            for (int j = parts.length - 9; j < parts.length; j++) {
                                if(j == parts.length - 9) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        } else if (parts.length >= 8) {
                            for (int j = parts.length - 6; j < parts.length; j++) {
                                if(j == parts.length - 6) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        } else if (parts.length >= 5) {
                            for (int j = parts.length - 3; j < parts.length; j++) {
                                if(j == parts.length - 3) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        }else {
                        	System.out.println(" No Fee Records to Delete");
                        	String[] args = null;
							PaymentMain.main(args );
            				return;
                        }
                        
                        
                        System.out.println(""); 
                    }
                }
                
            
            System.out.println(""); 
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	private static void deletePaymentReport(String name, String date) {
		String paymentFilename = "payment.txt";
		String tempFilename = "payment_temp.txt";
		String feesFilename = "fees.txt"; 

		try (BufferedReader brPayment = new BufferedReader(new FileReader(paymentFilename));
				BufferedWriter bwPayment = new BufferedWriter(new FileWriter(tempFilename));
				BufferedReader brFees = new BufferedReader(new FileReader(feesFilename));
				BufferedWriter bwFees = new BufferedWriter(new FileWriter("fees_temp.txt"))) {

			String linePayment;
			boolean isFirstLinePayment = true;
			boolean found = false;
			double amount = 0;
			while ((linePayment = brPayment.readLine()) != null) {
				if (isFirstLinePayment) {
					isFirstLinePayment = false;
					bwPayment.write(linePayment);
					bwPayment.newLine();
					continue;
				}

				String[] partsFees = linePayment.split(", ");
				ArrayList<String> feeLine = new ArrayList<>();

				for (int i = 0; i < partsFees.length; i++) {
					feeLine.add(partsFees[i]);
				}

				String lineName = partsFees[0];
				String stramount = null;
				if (lineName.equals(name)) {
					
					for (int i = 0; i < feeLine.size(); i++) {
						if (date.equals(feeLine.get(i).replace(")", ""))) {
							
							feeLine.remove(i);
							feeLine.remove(i);
							stramount = feeLine.get(i - 1).replace("(", "");
							amount = Double.parseDouble(stramount);
							feeLine.remove(i - 1);
							found = true;
							
						}
					}
					String[] newLinePayment = new String[partsFees.length - 2];

					for (int i = 0; i < newLinePayment.length-1; i++) {
						newLinePayment[i] = feeLine.get(i);
					}
					String newLine = Arrays.toString(newLinePayment).replace("[", "").replace("]", "").replace(", null", "");
					bwPayment.write(newLine);
					bwPayment.newLine();

				} else {
					bwPayment.write(linePayment);
					bwPayment.newLine();

				}

			}

			
			if (!found) {
				System.out.println("Payment report not found for " + date);
			} else {
				System.out.println("Payment report deleted successfully.");
				updateFeeReport(name, date, amount);
			}
			    

				

			File paymentFile = new File(paymentFilename);
			File tempPaymentFile = new File(tempFilename);
			if (paymentFile.delete() && tempPaymentFile.renameTo(paymentFile)) {
				System.out.println("Payment file updated successfully.");
			} else {
				System.out.println("Failed to update payment file.");
			}
			
		
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	

private static void updateFeeReport(String name, String date, double amount) {
	String feesFilename = "fees.txt";
	String tempFilename = "fees_temp.txt";
	try (BufferedReader brFees = new BufferedReader(new FileReader(feesFilename));
		 BufferedWriter bwFees = new BufferedWriter(new FileWriter(tempFilename))) {
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
				balance += amount;

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
		
		File feesFile = new File(feesFilename);
		File tempFeesFile = new File(tempFilename);
		if (feesFile.delete() && tempFeesFile.renameTo(feesFile)) {
			System.out.println("Fees file updated successfully.");
		} else {
			System.out.println("Failed to update fees file.");
		}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

}
private static void confirmDeclare() throws FileNotFoundException, IOException {
	@SuppressWarnings("resource")
	Scanner scanner = new Scanner(System.in);
	System.out.println("Confirm information (Y/y):");
    String confirm = scanner.next();

    if (!confirm.equalsIgnoreCase("Y")) {
    	String[] args = null;
		PaymentMain.main(args );
		return;
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
