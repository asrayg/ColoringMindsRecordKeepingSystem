package payment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ViewPayment {
	public static void main(String[] args) throws FileNotFoundException, IOException {
        checkLockStatusOfSystem("password.txt");

		@SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
		
		boolean validName = false;
		String studentName = "";

		while (!validName) {
			System.out.println("Enter the name of the student (or 'q' to quit):");
			studentName = scnr.nextLine();

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
		
		boolean validInput = false;
        int year = 0;

        while (!validInput) {
            System.out.println("Enter the year (4 digits) or 'q' to quit:");
            String yearInput = scnr.nextLine();

            if (yearInput.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                PaymentMain.main(args);
				return;
            }

            try {
                year = Integer.parseInt(yearInput);
                if (year < 1000 || year > 9999) {
                    System.out.println("Invalid year. Please enter a 4-digit year.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        String yearString = Integer.toString(year);
		
		viewPaymentDetails(studentName, yearString);
		PaymentMain.main(args);
		return;
	}
	
	
	private static void viewPaymentDetails(String name,  String year) {
        File inputFile = new File("payment.txt");
        File batchFile = new File("records.txt");
        File feesFile = new File("fees.txt");
        boolean found = true;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)); 
        		BufferedReader fr = new BufferedReader(new FileReader(feesFile)); 
        		BufferedReader dr = new BufferedReader(new FileReader(batchFile))) {
            String line;
            String line1;
            String line2;
            boolean firstLine = true;
            
            String batchType = null;
            while ((line1 = dr.readLine()) != null) {
            	if(firstLine == true) {
            		firstLine = false;
            		continue;
            	}
            	
            	String[] parts = line1.split(", ");
            	if(parts[0].equals(name)) {
            		batchType = parts[4];
            		break;
            	}
            	

            }
            
            firstLine = true;
            String balance = null;
            while ((line2 = fr.readLine()) != null) {
            	if(firstLine == true) {
            		firstLine = false;
            		continue;
            	}
            	
            	String[] parts = line2.split(", ");
            	if(parts[0].equals(name)) {
            		balance = parts[2];
            		break;
            	}
            	

            }
            
            firstLine = true;
            while ((line = br.readLine()) != null) {
            	if(firstLine == true) {
            		firstLine = false;
            		continue;
            	}
                
            	String[] parts = line.replace("(", "").replace(")", "").split(", ");


                String studentName = parts[0];
                                
               
                if(studentName.equals(name)) {
                	found = false;
                	System.out.println("");
                	System.out.println("Student Name: " + studentName + " -- Balance: " + balance + " -- Batch Type: " + batchType);
                    System.out.println("");
                	int count = 0;
                	for(int i =3; i<parts.length; i = i+3) {
                		
                		if (parts[i].subSequence(6, 10).equals(year)) {
                			System.out.println("Day-Month-Year: " + parts[i] + " Fee Paid: " + parts[i-1] + " Payment Type: " + parts[i+1]);
                			count++;
                		}
                	}
                	if(count == 0) {
                		System.out.println(name + " does not have any payment records in " + year);
                	}
                }
            }
            
            
            if (found) {
				
				System.out.println(name + " does not have any payment records in " + year);
				
			}
        }

        
        catch (IOException e) {
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
		} catch (IOException e) {
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
