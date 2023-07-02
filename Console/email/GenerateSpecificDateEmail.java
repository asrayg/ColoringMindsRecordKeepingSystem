package email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@SuppressWarnings("unused")
public class GenerateSpecificDateEmail {

    public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");


        try (Scanner scnr = new Scanner(System.in)) {
			int month = 0;
			int year = 0;

			boolean validInput = false;
			while (!validInput) {
			    System.out.println("Enter the month (1-12) or 'q' to quit:");
			    String monthInput = scnr.nextLine();

			    if (monthInput.equalsIgnoreCase("q")) {
			        System.out.println("Exiting the program...");
			        EmailMain.main(args);
			        return;
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
			    String yearInput = scnr.nextLine();

			    if (yearInput.equalsIgnoreCase("q")) {
			        System.out.println("Exiting the program...");
			        EmailMain.main(args);
			        return;
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
			
			System.out.println("Email: "); 
			String email = "";

			boolean isValidEmail = false;
			while (!isValidEmail) {
			    email = scnr.next();
			    if (email.matches(".+@.+\\.com")) {
			        isValidEmail = true;
			    } else {
			        System.out.println("Invalid email format. Please enter a valid email address.");
			        System.out.println("Email: "); 

			    }
			}
			
			System.out.println("Password:");
			String password = scnr.next();

			
			String datesToRemove = null;
			boolean validDates = false;

			while (!validDates) {
			    System.out.println("What dates (dd-MM-yyyy) would you like to remove for next month? Write in a comma-separated list (or 'q' to quit):");
			    datesToRemove = scnr.nextLine();

			    if (datesToRemove.equalsIgnoreCase("q")) {
			        System.out.println("Exiting the program...");
			        String[] args1 = null;
					EmailMain.main(args1);
			        return;
			    }

			    String[] dateArray = datesToRemove.split(",");
			    boolean allValid = true;

			    for (int i = 0; i < dateArray.length; i++) {
			        String trimmedDate = dateArray[i].trim();

			        if (!trimmedDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
			            System.out.println("Invalid date format: " + trimmedDate + ". Please enter dates in the format dd-MM-yyyy.");
			            allValid = false;
			        }
			    }

			    if (allValid) {
			        validDates = true;
			    } else {
			        System.out.println("Please try again.");
			    }
			}
			
			String[] datesToRemoveArr = datesToRemove.split(", ");

			System.out.println("Confirm (y)");
			String option = scnr.next();
			
			if(!option.equals("y") || !option.equals("Y")) {
				System.out.println("Quitting program");
				EmailMain.main(args);
			    return;
			}
			
			generateSpecDateEmail(month, year, email, password, datesToRemoveArr);
		}
        
        EmailMain.main(args);
        return;
        
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


	private static void generateSpecDateEmail(int month, int year, String email, String password, String[] datesToRemoveArr) throws IOException {
        @SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);


        
    	
    	
    	
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(email, password);
            }
        });
        session.setDebug(true);
        session.setDebug(true);
        try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            boolean isFirstLinePayment = true;
            while ((line = br.readLine()) != null) {
            	if (isFirstLinePayment) {
					isFirstLinePayment = false;
					continue;
				}

            
                String[] parts = line.split(", ");
                String name = parts[0];
                String to = parts[7];
                String isValid = parts[8];
                String batchType = parts[4];
                if (isValid.equals("y")) {

			        try {
			            MimeMessage message = new MimeMessage(session);
			
			            message.setFrom(new InternetAddress(email));
			
			            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			            message.setSubject("Coloring Minds: "+ getMonthName(month) + " Fees and Future Class Dates for " + getMonthName(month+1));
			
			            String text = "Hi,\n\n" +
	                            "Your child, " + name + ", has attended the following dates for the month of " + getMonthName(month) + ": " + getDates( month, year,  name) + ".\n" +
	                            "Your total balance to pay is " + getBalance(name) + ".\n\n" +
	                            "For the next month, there will be classes on the following dates: " + getFutureDates(month, year, batchType) + "\n\n" +
	                            "This is an auto-generated email. Please contact 81937383388.\n\n" +
	                            "Thanks,\n" +
	                            "Neelima Gopa";
			            
			            for(int i = 0; i< datesToRemoveArr.length; i++) {
			            	text.replace(" " + datesToRemoveArr[i]+",", "");
			            }
			            
			            message.setText(text);
			
			            Transport.send(message);
			            System.out.println("");
			            System.out.println("Message sent successfully");
			        } catch (MessagingException mex) {
			            mex.printStackTrace();
			        }
			        
                }
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    
 
    
    private static String getFutureDates(int month, int year, String batchTypes) {
        LocalDate currentDate = LocalDate.of(year, month, 1);

        LocalDate futureDate = currentDate.plusMonths(1);

        DayOfWeek firstDayOfWeek = futureDate.getDayOfWeek();

        List<String> batchDates = new ArrayList<>();
        for (char batchTypeChar : batchTypes.toCharArray()) {
            String batchType = String.valueOf(batchTypeChar).toUpperCase(Locale.ENGLISH);

            int offset = 0;
            switch (batchType) {
                case "M":
                    offset = 1;
                    break;
                case "T":
                    offset = 2;
                    break;
                case "W":
                    offset = 3;
                    break;
                case "R":
                    offset = 4;
                    break;
                case "F":
                    offset = 5;
                    break;
                case "S":
                    offset = 6;
                    break;
                case "U":
                    offset = 7;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid batch type: " + batchType);
            }

            int firstBatchDay = DayOfWeek.valueOf(batchType).getValue();
            int daysToAdd;
            if (offset >= firstBatchDay) {
                daysToAdd = offset - firstBatchDay;
            } else {
                daysToAdd = 7 - (firstBatchDay - offset);
            }
            LocalDate firstBatchDate = futureDate.plusDays(daysToAdd);

            while (firstBatchDate.getMonth() == futureDate.getMonth()) {
                batchDates.add(firstBatchDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                firstBatchDate = firstBatchDate.plusWeeks(1);
            }
        }

        StringBuilder datesStringBuilder = new StringBuilder();
        for (int i = 0; i < batchDates.size(); i++) {
            datesStringBuilder.append(batchDates.get(i));
            if (i < batchDates.size() - 1) {
                datesStringBuilder.append(", ");
            }
        }
        datesStringBuilder.append(".");

        return datesStringBuilder.toString();

    }

	private static String getBalance(String name) {
    	 File inputFile = new File("fees.txt");
         
         
         try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
             String line;
             
             boolean firstLine = true;
             
             while ((line = br.readLine()) != null) {
             	if(firstLine == true) {
             		firstLine = false;
             		continue;
             	}
                 
             	String[] parts = line.split(", ");


                 String studentName = parts[0];
                 String totalBalance = parts[2];
                 
                 if (name.equals(studentName)) {
                	 return "Rs. " + totalBalance;
                 }
                 
             }
         }
         catch (IOException e) {
             e.printStackTrace();
         }
		return null;
	}

	private static String getDates(int month, int year, String studentName) throws NumberFormatException, IOException {
    	try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            String dates = "";
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts[0].equals(studentName) && parts.length > 4) {
                    String[] dateParts = parts[4].split("-");
                    int attendanceMonth = Integer.parseInt(dateParts[1]);
                    int attendanceYear = Integer.parseInt(dateParts[2]);

                    if (attendanceMonth == month && attendanceYear == year) {
                        System.out.println(parts[0] + " " + parts[1]);
                        for (int i = 4; i < parts.length; i++) {
                            dates = dates + parts[i] + ", ";
                        }
                        String modDates = dates.substring(0, dates.length() - 2);
                        modDates = modDates +".";
                        return modDates;
                    }
                }
            }

		return null;
    	}
	}

    
    private static String getMonthName(int month) {
        List<String> monthNames = Arrays.asList(
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                "November", "December");
        if (month >= 1 && month <= 12) {
            return monthNames.get(month - 1);
        }
        return "";
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

}
