//Refresh.java

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Refresh {
    private static ScheduledExecutorService autoRefreshExecutor;
    private static int refreshInterval = 5; // Default interval (seconds)

    // Start the auto-refresh
    public static void startAutoRefresh(JPanel mainPanel) {
        stopAutoRefresh(); // Ensure any previous executor is stopped
        autoRefreshExecutor = Executors.newSingleThreadScheduledExecutor();

        autoRefreshExecutor.scheduleAtFixedRate(() -> refreshSpecs(mainPanel), 0, refreshInterval, TimeUnit.SECONDS);
    }

    private static void refreshSpecs(JPanel mainPanel) {
        if (mainPanel.getComponentCount() >= 4) {
            try {
                // Mise à jour des informations dans chaque panneau
                GUI.updateTextArea((JPanel) mainPanel.getComponent(0), "Operating System :" + " " + Specs.getOperatingSystemName() + "\n" + "Version :" + " " + Specs.getOperatingSystemVersion(), GUI.osIcon);
                GUI.updateTextArea((JPanel) mainPanel.getComponent(1), "CPU :" + " " + Specs.getCpuName() + "\n" + "Cores :" + " " + Specs.getCpuCores() + "\n" + "Threads :" + " " + Specs.getCpuThreads(), GUI.cpuIcon);
                GUI.updateTextArea((JPanel) mainPanel.getComponent(2), "GPU :" + " " + Specs.getGpuName() + "\n" + "Vram :" + " " + Specs.getGpuVram() + " " + "MB", GUI.gpuIcon);
                GUI.updateTextArea((JPanel) mainPanel.getComponent(3), "RAM (Total) :" + " " + Specs.getRamSize() + " " + "MB" + "\n" + "RAM (Used) :" + " " + Specs.getRamUsed() + " " + "MB" + "\n" + "RAM (Free) :" + " " + Specs.getRamFree() + " " + "MB", GUI.ramIcon);

                // Demander au garbage collector de libérer la mémoire non utilisée après la mise à jour
                System.gc();
            } catch (ClassCastException e) {
                System.err.println("Error casting component in mainPanel: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error while updating system specs: " + e.getMessage());
            }
        } else {
            System.err.println("Insufficient components in mainPanel. Expected 4, found: " + mainPanel.getComponentCount());
        }
    }


    // Display Auto Refresh dialog
    public static void showAutoRefreshDialog(JFrame parent, JPanel mainPanel) {
        String[] options = {"1 second", "5 seconds", "10 seconds", "15 seconds", "60 seconds", "Custom", "Disabled"};
        String selected = (String) JOptionPane.showInputDialog(
                parent,
                "Select Auto Refresh Interval:",
                "Auto Refresh",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if (selected != null) {
            switch (selected) {
                case "1 second" -> setAutoRefresh(1, mainPanel);
                case "5 seconds" -> setAutoRefresh(5, mainPanel);
                case "10 seconds" -> setAutoRefresh(10, mainPanel);
                case "15 seconds" -> setAutoRefresh(15, mainPanel);
                case "60 seconds" -> setAutoRefresh(60, mainPanel);
                case "Custom" -> handleCustomInterval(parent, mainPanel);
                case "Disabled" -> stopAutoRefresh();
            }
        }
    }

    // Handle custom interval
    private static void handleCustomInterval(JFrame parent, JPanel mainPanel) {
        String input = JOptionPane.showInputDialog(parent, "Enter custom interval in seconds (only numbers):");
        if (input != null) {
            try {
                int customInterval = Integer.parseInt(input);
                if (customInterval > 0) {
                    setAutoRefresh(customInterval, mainPanel);
                } else {
                    JOptionPane.showMessageDialog(parent, "Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Invalid input. Auto refresh disabled.", "Error", JOptionPane.ERROR_MESSAGE);
                stopAutoRefresh();
            }
        }
    }

    // Set auto-refresh interval
    private static void setAutoRefresh(int interval, JPanel mainPanel) {
        if (interval > 0) {
            refreshInterval = interval;
            startAutoRefresh(mainPanel);
        } else {
            stopAutoRefresh();
        }
    }

    // Stop any active auto-refresh executor
    private static void stopAutoRefresh() {
        if (autoRefreshExecutor != null && !autoRefreshExecutor.isShutdown()) {
            autoRefreshExecutor.shutdownNow();
        }
    }
}
