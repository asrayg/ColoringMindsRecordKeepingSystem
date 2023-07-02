package recordsUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mainUI.CommandUI;

public class RecordsUI {
    private JFrame frame;
    private JButton registerButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JButton viewBatchesButton;
    private JButton backButton;
    
    public RecordsUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Records Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 485);
        frame.setLocationRelativeTo(null);
        
        

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(37, 150, 190));
        
        ImageIcon imageIcon = new ImageIcon("c_minds.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        panel.add(imageLabel);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 105, 400, 30); 
        highlightPanel.setBackground(new Color(0, 32, 63)); 
        highlightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Records Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);

        registerButton = new JButton("Register");
        registerButton.setBounds(50, 150, 300, 40);
        registerButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterRecordUI.main(null);
                frame.dispose();
            }
        });
        panel.add(registerButton);

        editButton = new JButton("Edit");
        editButton.setBounds(50, 200, 300, 40);
        editButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditRecordsUI.main(null);
				frame.dispose();
            }
        });
        panel.add(editButton);

        deleteButton = new JButton("Delete/Renew");
        deleteButton.setBounds(50, 250, 300, 40);
        deleteButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteRecordsUI.main(null);
				frame.dispose();
            }
        });
        panel.add(deleteButton);

        viewButton = new JButton("View");
        viewButton.setBounds(50, 300, 300, 40);
        viewButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReadRecordsUI.main(null);
				frame.dispose();
            }
        });
        panel.add(viewButton);
        
        viewBatchesButton = new JButton("Batches");
        viewBatchesButton.setBounds(50, 350, 300, 40);
        viewBatchesButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewBatchesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewBatchesUI.main(null);
				frame.dispose();
            }
        });
        panel.add(viewBatchesButton);

        backButton = new JButton("Back");
        backButton.setBounds(150, 410, 100, 30);
        backButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommandUI.main(null);
                frame.dispose();
            }
        });
        panel.add(backButton);

        Dimension frameSize = frame.getSize();
        Dimension panelSize = panel.getPreferredSize();
        panel.setBounds(
            (frameSize.width - panelSize.width) / 2,
            (frameSize.height - panelSize.height) / 2 - 20, 
            panelSize.width,
            panelSize.height
        );

        frame.getContentPane().add(panel);
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

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        RecordsUI ui = new RecordsUI();
        ui.show();
    }
}



