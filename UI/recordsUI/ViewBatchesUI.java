package recordsUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ViewBatchesUI extends JFrame {

    private JTextArea outputArea;

    public ViewBatchesUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS View Batches");
        setSize(400, 485);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Batches & Their Students");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(37, 150, 190));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(new String[0]);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(173, 239, 209));
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        displayBatchesAndNames();
    }

    private void displayBatchesAndNames() {
        outputArea.setText("");
        String[] batchType = getBatches();
        String[] names = sortNames(batchType);

        printEverything(batchType, names);
    }

    private void printEverything(String[] batchType, String[] names) {
        int count = 0;
        outputArea.append("\n");
        outputArea.append("   " +getActualBatchDay(batchType[count] + ":\n\n"));
        outputArea.append("\n");
        int counter = 1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("Q1W2E3R4T5Y6")) {
                outputArea.append("\n");
                count++;
                if (count < batchType.length) {

                    outputArea.append("   " +getActualBatchDay(batchType[count] + ":\n\n"));
                    outputArea.append("\n");
                    counter =1;
                }
            } else {
                outputArea.append("       " + counter + ". " +names[i] + "\n\n");
            }
        }
        outputArea.setFont(new Font("Menlo", Font.BOLD, 13));
    }

    private String[] sortNames(String[] batchType) {
        ArrayList<String> names = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("records.txt"));

            for (int i = 0; i < batchType.length; i++) {
                String line;
                boolean isFirstLinePayment = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLinePayment) {
                        isFirstLinePayment = false;
                        continue;
                    }

                    String[] parts = line.split(", ");
                    if (batchType[i].equals(parts[4]) && parts[parts.length - 1].equals("y")) {
                        names.add(parts[0]);
                    }
                }
                names.add("Q1W2E3R4T5Y6");
                br = new BufferedReader(new FileReader("records.txt"));
            }
            return names.toArray(new String[names.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getBatches() {

        ArrayList<String> batchTypes = new ArrayList<>();
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

                if (batchTypes.size() == 0) {
                    batchTypes.add(parts[4]);
                }
                int count = 0;
                for (int i = 0; i < batchTypes.size(); i++) {
                    if (!parts[4].equals(batchTypes.get(i))) {
                        count++;
                    }
                }

                if (count == batchTypes.size()) {
                    batchTypes.add(parts[4]);
                }
            }

            String[] array = batchTypes.toArray(new String[batchTypes.size()]);

            return array;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getActualBatchDay(String batch) {
        StringBuilder actualDays = new StringBuilder();

        for (char c : batch.toCharArray()) {
            switch (c) {
                case 'M':
                    actualDays.append("Monday-");
                    break;
                case 'T':
                    actualDays.append("Tuesday-");
                    break;
                case 'W':
                    actualDays.append("Wednesday-");
                    break;
                case 'R':
                    actualDays.append("Thursday-");
                    break;
                case 'F':
                    actualDays.append("Friday-");
                    break;
                case 'S':
                    actualDays.append("Saturday-");
                    break;
                case 'U':
                    actualDays.append("Sunday-");
                    break;
                case '1':
                    actualDays.append(" 1 ");
                    break;
                case '2':
                    actualDays.append(" 2 ");
                    break;
                case '3':
                    actualDays.append(" 3 ");
                    break;
                case '4':
                    actualDays.append(" 4 ");
                    break;
                case '5':
                    actualDays.append(" 5 ");
                    break;
                case '6':
                    actualDays.append(" 6 ");
                    break;
                case '7':
                    actualDays.append(" 7 ");
                    break;
                case '8':
                    actualDays.append(" 8 ");
                    break;
                case '9':
                    actualDays.append(" 9 ");
                    break;
                case '0':
                    actualDays.append(" 0 ");
                    break;
                default:
                    break;
            }
        }

        if (actualDays.length() > 0) {
            actualDays.deleteCharAt(actualDays.length() - 1);
        }

        return actualDays.toString();
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ViewBatchesUI().setVisible(true);
            }
        });
    }
}

