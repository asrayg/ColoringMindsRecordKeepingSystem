package fees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings("unused")
public class ViewStudentsThatHaveABalance {
	public static void main(String[] args) {
        
        checkLockStatusOfSystem("password.txt");

       
        File inputFile = new File("fees.txt");
        
        int counter =0;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            System.out.println("Students with Balance: ");
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
            	if(firstLine == true) {
            		firstLine = false;
            		continue;
            	}
                
            	String[] parts = line.replace("(", "").replace(")", "").split(", ");

            	

                String studentName = parts[0];
                String  phno = parts[1];
                String totalBalance = parts[2];
                
                
                if(Double.valueOf(totalBalance)>0) {
                	
                	System.out.println("Student Name: " + studentName +  " -- Ph. No: " + phno +" -- Total Balance: " + totalBalance );
                	counter++;
                }
            }
            if (counter ==0) {
            	System.out.println("All balances have been cleared.");
            }
            System.out.println("");
            System.out.println("");
            FeesMain.main(args);
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
}
