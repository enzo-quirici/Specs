import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Refresh {
    private static ScheduledExecutorService autoRefreshExecutor; // Executor for auto-refresh tasks
    private static int refreshInterval = 5; // Default refresh interval in seconds

    // Method to start the auto-refresh using ScheduledExecutorService
    public static void startAutoRefresh(JPanel mainPanel) {
        if (autoRefreshExecutor != null && !autoRefreshExecutor.isShutdown()) {
            autoRefreshExecutor.shutdownNow(); // Stop any previous executor if it's running
        }

        autoRefreshExecutor = Executors.newScheduledThreadPool(1); // Create a single-threaded executor

        autoRefreshExecutor.scheduleAtFixedRate(() -> refreshSpecs(mainPanel), 0, refreshInterval, TimeUnit.SECONDS); // Schedule refresh task
    }

    // Method to refresh the system specifications
    private static void refreshSpecs(JPanel mainPanel) {
        // Assuming the GUI.updateTextArea method updates a text area with system specs
        GUI.updateTextArea((JPanel) mainPanel.getComponent(0), Specs.getOperatingSystem());
        GUI.updateTextArea((JPanel) mainPanel.getComponent(1), Specs.getCpuInfo());
        GUI.updateTextArea((JPanel) mainPanel.getComponent(2), Specs.getGpuInfo());
        GUI.updateTextArea((JPanel) mainPanel.getComponent(3), Specs.getRamInfo());
    }

    // Method to show the Auto Refresh dialog
    public static void showAutoRefreshDialog(JFrame parent, JPanel mainPanel) {
        String[] options = {"1 second", "5 seconds", "10 seconds", "15 seconds", "60 seconds", "Custom", "Disabled"};
        String selected = (String) JOptionPane.showInputDialog(
                parent,
                "Select Auto Refresh Interval:",
                "Auto Refresh",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // Default selection
        );

        if (selected != null) {
            // Determine the selected interval
            switch (selected) {
                case "1 second":
                    setAutoRefresh(1, mainPanel); // 1 second
                    break;
                case "5 seconds":
                    setAutoRefresh(5, mainPanel); // 5 seconds
                    break;
                case "10 seconds":
                    setAutoRefresh(10, mainPanel); // 10 seconds
                    break;
                case "15 seconds":
                    setAutoRefresh(15, mainPanel); // 15 seconds
                    break;
                case "60 seconds":
                    setAutoRefresh(60, mainPanel); // 60 seconds
                    break;
                case "Custom":
                    String input = JOptionPane.showInputDialog(parent, "Enter custom interval in seconds (only numbers):");
                    if (input != null) {
                        try {
                            int customInterval = Integer.parseInt(input); // Interval in seconds
                            if (customInterval > 0) {
                                setAutoRefresh(customInterval, mainPanel);
                            } else {
                                JOptionPane.showMessageDialog(parent, "Please enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                                setAutoRefresh(0, mainPanel); // Disable auto refresh
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(parent, "Invalid input. Auto refresh disabled.", "Error", JOptionPane.ERROR_MESSAGE);
                            setAutoRefresh(0, mainPanel); // Disable auto refresh
                        }
                    }
                    break;
                case "Disabled":
                    setAutoRefresh(0, mainPanel); // Disable auto refresh
                    break;
            }
        }
    }

    // Method to set the auto refresh interval
    private static void setAutoRefresh(int interval, JPanel mainPanel) {
        if (autoRefreshExecutor != null && !autoRefreshExecutor.isShutdown()) {
            autoRefreshExecutor.shutdownNow(); // Stop the previous executor if it exists
        }

        if (interval > 0) {
            refreshInterval = interval; // Set the new refresh interval
            startAutoRefresh(mainPanel); // Start the auto-refresh process with the new interval
        }
    }
}
