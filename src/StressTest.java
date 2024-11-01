//StressTest.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StressTest {
    private static Timer countdownTimer;
    private static ExecutorService stressExecutor;

    // Method to display the Stress Test window
    public static void showStressTest(JFrame parent, ImageIcon icon) {
        // Create a new window for the stress test
        JFrame StressTestFrame = new JFrame("Stress Test");
        StressTestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        StressTestFrame.setSize(600, 400);

        // Panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label for the countdown timer
        JLabel timerLabel = new JLabel("Time left : 60 seconds", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(timerLabel, BorderLayout.NORTH);

        // Create a text area to provide user information
        JTextArea infoText = new JTextArea();
        infoText.setText("Welcome to the Stress Test tool!\n" +
                "You can choose to run the test on a single core or across all available cores.\n" +
                "The test will run CPU-intensive tasks to maximize CPU usage.");
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setBackground(new Color(240, 240, 240));
        panel.add(new JScrollPane(infoText), BorderLayout.CENTER);

        // Input fields for selecting time duration (with default of 60 seconds)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2)); // Change to 3 rows for time field and buttons
        JLabel timeLabel = new JLabel("Test Duration (seconds) : ");
        JTextField timeField = new JTextField("60", 5);  // Default to 60 seconds
        timeLabel.setLabelFor(timeField);
        inputPanel.add(timeLabel);
        inputPanel.add(timeField);

        // Radio buttons for Single-Core or Multi-Core stress test
        JRadioButton singleCoreButton = new JRadioButton("Single-Core Test");
        JRadioButton multiCoreButton = new JRadioButton("Multi-Core Test", true);  // Default to Multi-Core
        ButtonGroup coreOptionGroup = new ButtonGroup();
        coreOptionGroup.add(singleCoreButton);
        coreOptionGroup.add(multiCoreButton);
        inputPanel.add(singleCoreButton);
        inputPanel.add(multiCoreButton);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Add a start button
        JButton startButton = new JButton("Start Stress Test");
        startButton.setBackground(new Color(100, 200, 100));
        startButton.setToolTipText("Start the stress test with the selected options.");
        startButton.addActionListener(e -> {
            String timeText = timeField.getText();
            try {
                int duration = Integer.parseInt(timeText);
                boolean isMultiCore = multiCoreButton.isSelected();
                startStressTest(duration, isMultiCore, timerLabel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StressTestFrame, "Please enter a valid number for the time duration.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        inputPanel.add(startButton);

        // Add a close button
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(200, 100, 100));
        closeButton.setToolTipText("Stop the stress test and close this window.");
        closeButton.addActionListener(e -> {
            if (stressExecutor != null && !stressExecutor.isShutdown()) {
                stressExecutor.shutdownNow(); // Stop any ongoing stress test
            }
            StressTestFrame.dispose();
        });
        inputPanel.add(closeButton);

        panel.add(inputPanel, BorderLayout.SOUTH); // Button panel at the bottom

        // Set the icon for the window
        StressTestFrame.setIconImage(icon.getImage());

        // Add everything to the main frame
        StressTestFrame.add(panel);
        StressTestFrame.setLocationRelativeTo(parent);
        StressTestFrame.setVisible(true);
    }

    // Method to start the stress test
    private static void startStressTest(int duration, boolean isMultiCore, JLabel timerLabel) {
        int numCores = isMultiCore ? Runtime.getRuntime().availableProcessors() : 1;
        stressExecutor = Executors.newFixedThreadPool(numCores);

        // Create and start stress tasks
        for (int i = 0; i < numCores; i++) {
            stressExecutor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    // Intensive CPU-bound task to utilize as much GHz as possible
                    double x = 0;
                    for (long j = 0; j < Long.MAX_VALUE; j++) {
                        x = Math.sin(x) * Math.cos(x) * Math.tan(x); // Intense floating-point math
                        // Check for interruption after some iterations
                        if (j % 10000000 == 0 && Thread.currentThread().isInterrupted()) {
                            break; // Break out of the loop if interrupted
                        }
                    }
                }
            });
        }

        // Countdown timer (in seconds)
        countdownTimer = new Timer(1000, new ActionListener() {
            int timeRemaining = duration;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timerLabel.setText("Time left : " + timeRemaining + " seconds");
                    timeRemaining--;
                } else {
                    ((Timer) e.getSource()).stop();
                    stressExecutor.shutdownNow(); // End the stress test
                    timerLabel.setText("Test Complete");
                    JOptionPane.showMessageDialog(null, "Stress Test Complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        countdownTimer.start();
    }
}
