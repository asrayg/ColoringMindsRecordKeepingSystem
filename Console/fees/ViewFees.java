package fees;

import java.io.*;
import java.util.*;

public class ViewFees {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the names (comma-separated):");
        String namesInput = scanner.nextLine();
        String[] names = namesInput.split(", ");
        int count = 0;
        for(int i = 0; i<names.length; i++) {
        	if(!checkName(names[i])) {	
        		System.out.println(names[i] + " is not in the records. Please register it");
        		count++;
        	}
        }
        
        if(count == names.length) {
        	if (count == 1) {
        		System.out.println("Name is not in the database. Try again");
        		System.out.println("");
        	}else {
        		System.out.println("Names are not in the database. Try again");
        	}
            FeesMain.main(args);
            return;
        }
        
        
        boolean validInput = false;
        int year = 0;

        while (!validInput) {
            System.out.println("Enter the year (4 digits) or 'q' to quit:");
            String yearInput = scanner.nextLine();

            if (yearInput.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program...");
                FeesMain.main(args);
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

        for (String name : names) {
            viewFeeDetails(name.trim(), yearString);
        }
        FeesMain.main(args);
        return;
    }

    private static void viewFeeDetails(String name, String year) {
        File inputFile = new File("fees.txt");
        File batchFile = new File("records.txt");
        boolean checker = false;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedReader dr = new BufferedReader(new FileReader(batchFile))) {
            String line;
            String line1;
            boolean firstLine = true;
            String batchType = null;
            while ((line1 = dr.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                
                String[] parts = line1.split(", ");
                
                
                
                
                if (parts[0].equals(name)) {
                    batchType = parts[4];
                    if(!checkValidity(parts[0]) ) {
                    	System.out.println(parts[0] + " is not valid. Please renew " + parts[0]);               
                    	checker = true;
                    }
                    break;
                }
            }

            firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.replace("(", "").replace(")", "").split(", ");

                if (parts.length < 6) {
                    System.out.println("Invalid format in the input file. Please ensure each line follows the format: Name, Batch Type, Fees Per Hour, Month, Classes, Total Fees");
                    String[] args = null;
					FeesMain.main(args );
                }

                String studentName = parts[0];
                String totalBalance = parts[2];

                if (studentName.equals(name) && checker == false) {
                    System.out.println("");
                    System.out.println("Student Name: " + studentName + " -- Total Balance: " + totalBalance + " -- Batch Type: " + batchType);
                    System.out.println("");

                    for (int i = 3; i < parts.length; i = i + 4) {
                        if (parts[i].substring(3, 7).equals(year)) {
                            System.out.println("Month-Year: " + parts[i] + " Fees: " + parts[i + 3] + " Per Hour: " + parts[i + 1]);
                        }
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

