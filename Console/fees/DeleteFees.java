package fees;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeleteFees {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the names (comma-separated, type 'all' to delete for all names):");
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
        	}else {
        		System.out.println("Names are not in the database. Try again");
        	}
            FeesMain.main(args);
        }
        
        if(!names[0].equals("all")){
        	getNameFeeInfo(names);
        }
        
        
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
                if (year < 1000 || year > 9999) {
                    System.out.println("Invalid year. Please enter a 4-digit year not exceeding the current year.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        
        confirmDeclare();
        
        if (names[0].equalsIgnoreCase("all")) {
            deleteAllFeeReports(month, year);
        } else {
            deleteFeeReport(names, month, year);
        }
        FeesMain.main(args);
    }

    

    private static void getNameFeeInfo(String[] names) {
        try (BufferedReader br = new BufferedReader(new FileReader("fees.txt"))) {
            String line;
            System.out.println("");
            System.out.println("Latest Fee Reports for name/s: ");
            System.out.println("Name  || Fee Report: (Month-Year, fees per hour, number of classes, Balance)");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                for (int i = 0; i < names.length; i++) {
                    if (parts[0].contains(names[i])) {
                        System.out.print(parts[0] + " || ");
                        if (parts.length >= 15) {
                            for (int j = parts.length - 12; j < parts.length; j++) {
                                if(j == parts.length - 12) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        } else if (parts.length >= 11) {
                            for (int j = parts.length - 8; j < parts.length; j++) {
                                if(j == parts.length - 8) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        } else if (parts.length >= 7) {
                            for (int j = parts.length - 4; j < parts.length; j++) {
                                if(j == parts.length - 4) {
                                	System.out.print(parts[j]);
                                }else {
                                	System.out.print(", " + parts[j]);
                                }
                            }
                        }else {
                        	System.out.println(" No Fee Records to Delete");
                        }
                        
                        
                        System.out.println(""); 
                    }
                }
                
            }
            System.out.println(""); 
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

    
	private static void deleteAllFeeReports(int month, int year) {
    	File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                } else {
                    String[] parts = line.split(", ");
                    String studentName = parts[0];
                    String phoneNumber = parts[1];
                    String totalBalance = parts[2];
                    
                    if(!line.contains(String.format("%02d-%04d", month, year))) {
                		System.out.println(studentName + " did not have a fee report for " + String.format("%02d-%04d", month, year));
                	}

                    
                    if(!checkValidity(studentName)) {
                    	bw.write(line);
                        bw.newLine();
                    	continue;
                    }

                    List<String> feeReports = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        feeReports.add(parts[i]);
                    }

                    List<String> updatedFeeReports = new ArrayList<>();
                    int count = 0;
                    int elementNumber = -1; 

                    for (String feeReport : feeReports) {
                        String[] reportParts = feeReport.split(", ");
                        String reportMonthYear = reportParts[0].substring(1);
                        if (!reportMonthYear.equals(String.format("%02d-%04d", month, year))) {
                            updatedFeeReports.add(feeReport);
                        } else {
                            elementNumber = count;
                        }
                        count++;
                    }

                    double subtractingAmount = Double.parseDouble(updatedFeeReports.get(elementNumber + 2).replace(")", ""));
                    double newTotalBalance = Double.parseDouble(totalBalance) - subtractingAmount;

                    if (elementNumber != -1 && elementNumber + 2 < updatedFeeReports.size()) {
                        updatedFeeReports.remove(elementNumber + 2);
                        updatedFeeReports.remove(elementNumber + 1);
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append(studentName).append(", ").append(phoneNumber).append(", ").append(newTotalBalance);

                    for (int i = 0; i < updatedFeeReports.size(); i++) {
                        if (i != elementNumber) {
                            sb.append(", ").append(updatedFeeReports.get(i));
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    private static void deleteFeeReport(String[] names, int month, int year) throws ArrayIndexOutOfBoundsException{
        File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                } else {
                	
                    String[] parts = line.split(", ");
                    String studentName = parts[0];
                    String totalBalance = parts[2];
                    String phoneNumber = parts[1];
                    
                    boolean hasDate = false;
                    for(int i = 0; i<names.length; i++) {
                    	if(studentName.equals(names[i])) {
                    		hasDate = true;
                    	}
                    }

                    if(!line.contains(String.format("%02d-%04d", month, year))&& hasDate) {
                		System.out.println(studentName + " did not have a fee report for " + String.format("%02d-%04d", month, year));
                	}
                    
                    boolean shouldDelete = false;
                    for (String name : names) {
                        if (studentName.equals(name.trim())) {
                            shouldDelete = true;
                            break;
                        }
                    }

                    if(!checkValidity(studentName)) {
                    	System.out.println(studentName + " is not valid. Please renew " + studentName);
                    	bw.write(line);
                        bw.newLine();
                    	continue;
                    }
                    if (!shouldDelete) {
                        bw.write(line);
                        bw.newLine();
                        continue;
                    }

                    List<String> feeReports = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        feeReports.add(parts[i]);
                    }

                    List<String> updatedFeeReports = new ArrayList<>();
                    int count = 0;
                    int elementNumber = -1;

                    for (String feeReport : feeReports) {
                        String[] reportParts = feeReport.split(", ");
                        if (reportParts.length >= 1) {  
                            String reportMonthYear = reportParts[0].substring(1);
                            if (!reportMonthYear.equals(String.format("%02d-%04d", month, year))) {
                                updatedFeeReports.add(feeReport);
                            } else {
                                elementNumber = count;
                            }
                            count++;
                        }
                    }


                    double subtractingAmount = Double.parseDouble(updatedFeeReports.get(elementNumber + 2).replace(")", ""));
                    double newTotalBalance = Double.parseDouble(totalBalance) - subtractingAmount;

                    if(newTotalBalance<0) {
                    	newTotalBalance = 0.00;
                    }
                    if (elementNumber != -1 && elementNumber + 2 < updatedFeeReports.size()) {
                        updatedFeeReports.remove(elementNumber + 2);
                        updatedFeeReports.remove(elementNumber + 1);
                    }

                    
                    StringBuilder sb = new StringBuilder();
                    sb.append(studentName).append(", ").append(phoneNumber).append(", ").append(newTotalBalance);

                    for (int i = 0; i < updatedFeeReports.size(); i++) {
                        if (i != elementNumber) {
                            sb.append(", ").append(updatedFeeReports.get(i));
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
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
		if(name.equals("all")) {
			return true;
		}
		
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static void confirmDeclare() throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Confirm information (Y/y):");
        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("Y")) {
            String[] args = null;
			FeesMain.main(args );
        } 
		
	}

	
}
