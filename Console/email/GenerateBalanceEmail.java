package email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GenerateBalanceEmail {
	public static void main(String[] args) throws IOException {
        checkLockStatusOfSystem("password.txt");

		@SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);
		System.out.println("This will send an email to all students with a balance. Do you wish to coninue (y)");
		String option = scnr.next();
		if (option.equals("y") || option.equals("Y")) {
			generateEmail();
		}else {
			System.out.println("Exiting program...");
			EmailMain.main(args);
            return;
		}
		EmailMain.main(args);
        return;
	}

	private static void generateEmail() {
		@SuppressWarnings("resource")
		Scanner scnr = new Scanner(System.in);

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
        final String emailReal = email;
        
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(emailReal, password);
            }
        });
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
				int balance = getBalance(name);
				if (isValid.equals("y") && balance > 0) {

					try {
						MimeMessage message = new MimeMessage(session);

						message.setFrom(new InternetAddress(email));

						message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

						message.setSubject("Coloring Minds: Balance To Be Paid");

						String text = "Hi,\n\n" +

								"You have total balance to pay is Rs. " + balance + ".\n\n"
								+ "This is an auto-generated email. Please contact 81937383388.\n\n" + "Thanks,\n"
								+ "Neelima Gopa";
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

	@SuppressWarnings("null")
	private static int getBalance(String name) {
		File inputFile = new File("fees.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;

			boolean firstLine = true;

			while ((line = br.readLine()) != null) {
				if (firstLine == true) {
					firstLine = false;
					continue;
				}

				String[] parts = line.split(", ");

				String studentName = parts[0];
				String total = parts[2];
				int totalBalance = Integer.parseInt(total);
				if (name.equals(studentName)) {
					return totalBalance;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}
	}

