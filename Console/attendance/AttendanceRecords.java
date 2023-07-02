package attendance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AttendanceRecords {
    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");
        try (Scanner scnr = new Scanner(System.in)) {
			System.out.println("Would you like to mark attendance for today? (y/n)");
			String dateOption = scnr.next();

			while (!dateOption.equalsIgnoreCase("y") && !dateOption.equalsIgnoreCase("n") && !dateOption.equalsIgnoreCase("q")) {
			    System.out.println("Invalid option. Please enter 'y' to mark attendance, 'n' to skip, or 'q' to quit.");
			    dateOption = scnr.next();
			}

			if (dateOption.equalsIgnoreCase("q")) {
			    System.out.println("Exiting the program...");
			    AttendanceMain.main(args);
			    return;
			}

			System.out.println("Enter the batch you want to look at for attendance: ");
			String batchOption = scnr.next();

			boolean isValidBatchType = false;
			while (!isValidBatchType) {
			    batchOption = batchOption.toUpperCase();
			    if (batchOption.matches("[MTWRFSU1234567890]+")) {
			        isValidBatchType = true;
			    } else {
			        System.out.println("Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9 only.");
			        System.out.println("Batch Type:");
			        batchOption = scnr.next(); 
			    }
			}

			
			File file = new File("attendance.txt");
			String tempFile = "temp.txt";

			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = currentDate.format(formatter);

			try (BufferedReader br = new BufferedReader(new FileReader(file));
			     FileWriter fw = new FileWriter(tempFile, true)) {
			    String line;
			    ArrayList<String> names = new ArrayList<>();
			    while ((line = br.readLine()) != null) {
			        String[] parts = line.split(", ");
			        if (parts.length >= 3 && parts[2].trim().equals(batchOption) && !parts[parts.length - 1].equals("n")) {
			            System.out.println(parts[0]);
			            names.add(parts[0]);
			        }
			    }
			    
			    if(names.size() == 0) {
			    	System.out.println("No such batch exists. Try again");
			        AttendanceMain.main(args);
			    }

			    System.out.println("Would you like to mark all these names as present? (y/n)");
			    String atteOption = scnr.next();
			    
			    

			    boolean isValidOption = false;
			    while (!isValidOption) {
			        if (atteOption.equalsIgnoreCase("y") || atteOption.equalsIgnoreCase("n")) {
			            isValidOption = true;
			        } else if (atteOption.equalsIgnoreCase("q")) {
			            System.out.println("Exiting the program...");
			            AttendanceMain.main(args);
			        } else {
			            System.out.println("Invalid option. Please enter 'y' to mark all as present or 'n' to not mark them.");
			            System.out.println("Would you like to mark all these names as present? (y/n)");
			            atteOption = scnr.next(); 
			        }
			    }
			    if (dateOption.equals("y") && atteOption.equals("y")) {
			        for (int i = 0; i < names.size(); i++) {
			            String name = names.get(i);
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + formattedDate;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			        }
			    } else if (dateOption.equals("n") && atteOption.equals("y")) {
			    	System.out.println("Type the date you want to add attendance for in the format of dd-MM-yyyy");
			    	String customDateOption = scnr.next();

			    	boolean isValidDate = false;
			    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			    	while (!isValidDate) {
			    	    try {
			    	        LocalDate.parse(customDateOption, dateFormatter); 
			    	        isValidDate = true; 
			    	    } catch (DateTimeParseException e) {
			    	        System.out.println("Invalid date. Please enter a valid date in the format dd-MM-yyyy.");
			    	        System.out.println("Type the date you want to add attendance for:");
			    	        customDateOption = scnr.next(); 
			    	    }
			    	}


			        for (int i = 0; i < names.size(); i++) {
			            String name = names.get(i);
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + customDateOption;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			        }
			    } else if (dateOption.equals("y") && atteOption.equals("n")) {
			        
			        
			        System.out.println("What names would you specifically like to add attendance to? Provide a comma-separated list of names ");
			        scnr.nextLine(); 

			        String input = scnr.nextLine();
			        String[] selectedNames = input.split(",");
			        List<String> validNames = new ArrayList<>();

			        for (String name : selectedNames) {
			            name = name.trim();

			            if (name.equalsIgnoreCase("q")) {
			                System.out.println("Exiting the program...");
			                AttendanceMain.main(args);
			            }

			            if (checkName(name) && checkValidity(name)) {
			                validNames.add(name);
			            } else if (!checkValidity(name)) {
			            	System.out.println(name + " is not valid. Please renew/register " + name + " to add attendance");
			            } 
			        }

			        for (String name : validNames) {
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + formattedDate;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			        }
			        String nameList = scnr.nextLine();
			        List<String> specificNames = Arrays.asList(nameList.split(", "));
			        for (String name : specificNames) {
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + formattedDate;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			        }
			    } else if (dateOption.equals("n") && atteOption.equals("n")) {
			    	System.out.println("Type the date you want to add attendance for in the format of dd-MM-yyyy");
			    	String customDateOption = scnr.next();

			    	boolean isValidDate = false;
			    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			    	while (!isValidDate) {
			    	    try {
			    	        LocalDate.parse(customDateOption, dateFormatter); 
			    	        isValidDate = true; 
			    	    } catch (DateTimeParseException e) {
			    	        System.out.println("Invalid date. Please enter a valid date in the format dd-MM-yyyy.");
			    	        System.out.println("Type the date you want to add attendance for:");
			    	        customDateOption = scnr.next(); 
			    	    }
			    	}
			        
			    	System.out.println("What names would you specifically like to add attendance to? Provide a comma-separated list of names ");
			        scnr.nextLine(); 

			        String input = scnr.nextLine();
			        String[] selectedNames = input.split(",");
			        List<String> validNames = new ArrayList<>();

			        for (String name : selectedNames) {
			            name = name.trim();

			            if (name.equalsIgnoreCase("q")) {
			                System.out.println("Exiting the program...");
			                AttendanceMain.main(args);

			            }

			            if (checkName(name) && checkValidity(name)) {
			                validNames.add(name);
			            } else if (!checkValidity(name)) {
			            	System.out.println(name + " is not valid. Please renew/register " + name + " to add attendance");
			            } 
			        }

			        for (String name : validNames) {
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + formattedDate;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			        }
			        String nameList = scnr.nextLine();
			        List<String> specificNames = Arrays.asList(nameList.split(", "));
			        for (String name : specificNames) {
			            String updatedLine = null;

			            try (BufferedReader brTemp = new BufferedReader(new FileReader(file));
			                 FileWriter fwTemp = new FileWriter(tempFile)) {
			                String tempLine;
			                while ((tempLine = brTemp.readLine()) != null) {
			                    String[] partsTemp = tempLine.split(", ");
			                    if (partsTemp.length > 0 && partsTemp[0].trim().equals(name)) {
			                        partsTemp[partsTemp.length - 1] += ", " + formattedDate;
			                    }
			                    if (updatedLine == null) {
			                        updatedLine = String.join(", ", partsTemp);
			                    } else {
			                        updatedLine += System.lineSeparator() + String.join(", ", partsTemp);
			                    }
			                }
			                fwTemp.write(updatedLine);
			            }

			            if (file.delete()) {
			                File temp = new File(tempFile);
			                temp.renameTo(file);
			            }
			    }
			    }
			    AttendanceMain.main(args);
			    return;
			} catch (IOException e) {
			    e.printStackTrace();
			}
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

