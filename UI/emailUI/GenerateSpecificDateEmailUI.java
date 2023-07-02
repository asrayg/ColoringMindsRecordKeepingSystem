package emailUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



@SuppressWarnings({ "serial", "unused" })
public class GenerateSpecificDateEmailUI extends JFrame {
    private JTextField monthField;
    private JTextField yearField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField datesField;

    public GenerateSpecificDateEmailUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS Remove Specific Dates From Email");
    	setSize(400, 485);
    	setBackground(new Color(37, 150, 190));
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	JPanel panel = new JPanel();
    	panel.setLayout(null);

    	int y = 61;
    	int z = 85;

    	// Adjust the bounds of the labels and fields as per your requirement
    	JLabel monthLabel = new JLabel("Month");
    	monthLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	monthLabel.setBounds(y + 10, z + 10, 100, 20);

    	monthField = new JTextField(); 
    	monthField.setBounds(y + 90, z + 10, 180, 20);

    	JLabel yearLabel = new JLabel("Year");
    	yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	yearLabel.setBounds(y + 10, z + 40, 150, 20);
    	yearField = new JTextField();
    	yearField.setBounds(y + 90, z + 40, 180, 20);

    	JLabel emailLabel = new JLabel("Email");
    	emailLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	emailLabel.setBounds(y + 10, z + 95, 100, 20);
    	emailField = new JTextField();
    	emailField.setBounds(y + 90, z + 95, 180, 20);

    	JLabel passwordLabel = new JLabel("Password");
    	passwordLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	passwordLabel.setBounds(y + 10, z + 125, 100, 20);
    	passwordField = new JPasswordField();
    	passwordField.setBounds(y + 90, z + 125, 180, 20);

    	// Dates
    	JLabel datesLabel = new JLabel("Dates");
    	datesLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	datesLabel.setBounds(y + 10, z + 180, 180, 20);
    	datesField = new JTextField();
    	datesField.setBounds(y + 90, z + 180, 180, 20);

    	panel.add(monthLabel);
    	panel.add(monthField);
    	panel.add(yearLabel);
    	panel.add(yearField);
    	panel.add(emailLabel);
    	panel.add(emailField);
    	panel.add(passwordLabel);
    	panel.add(passwordField);
    	panel.add(datesLabel);
    	panel.add(datesField);

    	// Generate Button
    	JButton generateButton = new JButton("Generate");
    	generateButton.setBounds(254, 310, 80, 25);
    	generateButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        int month;
    	        int year;
    	        String email;
    	        String password;
    	        String datesToRemove;

    	        try {
    	            month = Integer.parseInt(monthField.getText());
    	            year = Integer.parseInt(yearField.getText());
    	            email = emailField.getText();
    	            password = new String(passwordField.getPassword());
    	            datesToRemove = datesField.getText();

    	            if (month < 1 || month > 12) {
    	                JOptionPane.showMessageDialog(null, "Invalid month. Please enter a number between 1 and 12.");
    	                return;
    	            }

    	            if (year < 1000 || year > LocalDate.now().getYear()) {
    	                JOptionPane.showMessageDialog(null, "Invalid year. Please enter a 4-digit year not exceeding the current year.");
    	                return;
    	            }

    	            if (!email.matches(".+@.+\\.com")) {
    	                JOptionPane.showMessageDialog(null, "Invalid email format. Please enter a valid email address.");
    	                return;
    	            }

    	            if (datesToRemove.isEmpty()) {
    	                JOptionPane.showMessageDialog(null, "Please enter dates to remove.");
    	                return;
    	            }

    	            String[] datesToRemoveArr = datesToRemove.split(",");
    	            List<String> trimmedDates = new ArrayList<>();
    	            for (String date : datesToRemoveArr) {
    	                trimmedDates.add(date.trim());
    	            }
    	            String[] array = trimmedDates.toArray(new String[0]);

    	            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to generate the email?", "Confirmation", JOptionPane.YES_NO_OPTION);
    	            if (option == JOptionPane.YES_OPTION) {
    	                try {
    	                    generateSpecDateEmail(month, year, email, password, array);
    	                    JOptionPane.showMessageDialog(null, "Email generation completed successfully!");
    	                } catch (IOException | MessagingException ex) {
    	                    ex.printStackTrace();
    	                    JOptionPane.showMessageDialog(null, "Error sending email");
    	                }
    	            }

    	        } catch (NumberFormatException ex) {
    	            JOptionPane.showMessageDialog(null, "Invalid input format. Please enter valid numbers for month and year.");
    	        }
    	    }
    	});

    	panel.add(new JLabel()); // Empty label for alignment
    	panel.add(generateButton);
    	JButton cancelButton = new JButton("Back");
    	cancelButton.setBounds(320, 428, 75, 30);
    	cancelButton.setBackground(new Color(173, 239, 209));
    	cancelButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        EmailUI.main(null);
    	        dispose();
    	    }
    	});
    	panel.add(cancelButton);

    	JPanel highlightPanel = new JPanel();
    	highlightPanel.setBounds(15, 40, 370, 325);
    	highlightPanel.setBackground(new Color(72, 170, 173));
    	highlightPanel.setLayout(null);
    	panel.add(highlightPanel);
    	JPanel highlightPanel1 = new JPanel();
    	highlightPanel1.setBounds(0, 422, 400, 325);
    	highlightPanel1.setBackground(new Color(173, 239, 209));
    	highlightPanel1.setLayout(null);
    	panel.add(highlightPanel1);
    	panel.setBackground(new Color(37, 150, 190));

    	add(panel);
    	setVisible(true);

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


    private static void generateSpecDateEmail(int month, int year, final String email, final String password, String[] datesToRemoveArr) throws IOException, MessagingException {
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GenerateSpecificDateEmailUI();
            }
        });
    }
}
