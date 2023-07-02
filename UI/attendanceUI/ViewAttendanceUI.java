package attendanceUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class ViewAttendanceUI extends JFrame {
	private JTextField nameField;
	private JTextField monthField;
	private JTextField yearField;
	private JTextArea outputArea;

	public ViewAttendanceUI() {
        checkLockStatusOfSystem("password.txt");

		setTitle("CMRKS View Student Attendance");
		setSize(400, 485);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(37, 150, 190));

		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		namePanel.setBackground(new Color(37, 150, 190));

		JLabel nameLabel = new JLabel("Enter student name:");
		nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

		nameField = new JTextField(20);
		nameField.setPreferredSize(new Dimension(200, 25));

		namePanel.add(nameLabel);
		namePanel.add(nameField);

		JPanel monthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		monthPanel.setBackground(new Color(37, 150, 190));

		//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		//        searchPanel.setBackground(new Color(37, 150, 190));

		JLabel monthLabel = new JLabel("Month");
		monthLabel.setFont(new Font("Verdana", Font.BOLD, 12));
		monthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		monthField = new JTextField(5);
		monthField.setPreferredSize(new Dimension(50, 25));

		JLabel yearLabel = new JLabel("Year");
		yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
		yearLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		yearField = new JTextField(5);
		yearField.setPreferredSize(new Dimension(50, 25));

		monthPanel.add(monthLabel);
		monthPanel.add(monthField);
		monthPanel.add(yearLabel);
		monthPanel.add(yearField);

		JButton searchButton = new JButton("Search");

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchPanel.setBackground(new Color(37, 150, 190));
		monthPanel.add(searchButton);

		outputArea = new JTextArea();
		outputArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setPreferredSize(new Dimension(380, 400));

		mainPanel.add(namePanel);
		mainPanel.add(monthPanel);
		//        mainPanel.add(yearPanel);
		mainPanel.add(searchPanel);
		mainPanel.add(scrollPane);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AttendanceUI.main(new String[0]);
				dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(new Color(173, 239, 209));
		buttonPanel.add(backButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText().trim();
				String monthText = monthField.getText().trim();
				String yearText = yearField.getText().trim();

				if (name.isEmpty()) {
					showError("Please enter a student name.");
					return;
				}

				try {
					if (!isValidName(name)) {
						showError(name + " is invalid. Please renew it to view.");
						return;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				int month = -1;
				int year = -1;

				if (!monthText.isEmpty()) {
					try {
						month = Integer.parseInt(monthText);
						if (month < 1 || month > 12) {
							showError("Invalid month. Please enter a number between 1 and 12.");
							return;
						}
					} catch (NumberFormatException ex) {
						showError("Invalid month. Please enter a valid number.");
						return;
					}
				}

				if (!yearText.isEmpty()) {
					try {
						year = Integer.parseInt(yearText);
					} catch (NumberFormatException ex) {
						showError("Invalid year. Please enter a valid number.");
						return;
					}
				}

				String result;
				if (month != -1 && year != -1) {
					result = getAttendanceForMonth(name, month, year);
				} else if (year != -1) {
					result = getAttendanceForYear(name, year);
				} else {
					showError("Please enter a month and/or year.");
					return;
				}

				outputArea.setText(result);
			}
		});
	}

	private boolean isValidName(String name) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length > 0 && parts[0].trim().equals(name)) {
					if (parts[8].equals("y")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getAttendanceForMonth(String studentName, int month, int year) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
			String line;
			boolean found = false;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts[0].equals(studentName) && parts.length > 4) {
					sb.append("\n");
					sb.append("   Batch Type: ").append(parts[2]).append("\n").append("   Phone Number: ")
					.append(parts[1]);
					sb.append("\n");
					for (int i = 4; i < parts.length; i++) {
						String[] dateParts = parts[i].split("-");
						int attendanceMonth = Integer.parseInt(dateParts[1]);
						int attendanceYear = Integer.parseInt(dateParts[2]);
						if (attendanceMonth == month && attendanceYear == year) {
							sb.append("\n");

							sb.append("     " + parts[i]);

							found = true;
						}
					}
				}
			}
			Font lucidaFont = new Font("Menlo", Font.BOLD, outputArea.getFont().getSize());
			outputArea.setFont(lucidaFont);

			if (!found) {
				sb.append("   No attendance found for ").append(studentName).append(" in ").append(month).append("-")
				.append(year);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
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

	private String getAttendanceForYear(String studentName, int year) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
			String line;
			boolean found = false;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts[0].equals(studentName) && parts.length > 4) {
					sb.append("\n");
					sb.append("   Batch Type: ").append(parts[2]).append("\n").append("   Phone Number: ")
					.append(parts[1]);
					sb.append("\n");
					for (int i = 4; i < parts.length; i++) {
						String[] dateParts = parts[i].split("-");
						int attendanceYear = Integer.parseInt(dateParts[2]);
						if (attendanceYear == year) {
							sb.append("\n");

							sb.append("     " + parts[i]);

							found = true;
						}
					}
				}
			}
			Font lucidaFont = new Font("Menlo", Font.BOLD, outputArea.getFont().getSize());
			outputArea.setFont(lucidaFont);

			if (!found) {
				sb.append("   No attendance found for ").append(studentName).append(" in ").append(year);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ViewAttendanceUI().setVisible(true);
			}
		});
	}
}
