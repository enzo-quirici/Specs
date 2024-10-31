//InfoPanel.java

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InfoPanel {

    // Method to get the OS icon
    public static ImageIcon getOsIcon() {
        String osName = System.getProperty("os.name").toLowerCase(); // Get the OS name
        String osVersion = System.getProperty("os.version").toLowerCase(); // Get the OS version
        String iconPath;

        // Determine the icon path based on the OS
        if (osName.contains("win")) {
            iconPath = "/icon/Windows 128x128.png";
        } else if (osName.contains("mac")) {
            iconPath = "/icon/Mac OS 128x128.png";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            iconPath = "/icon/Linux 128x128.png";
        } else {
            iconPath = "/icon/Unknown 128x128.png"; // Default icon for unknown OS
        }

        return new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource(iconPath))); // Load the icon
    }

    // Method to get the CPU icon
    public static ImageIcon getCpuIcon(String cpuInfo) {
        String iconPath;

        // Determine the icon path based on the type of CPU
        if (cpuInfo.toLowerCase().contains("intel")) {
            iconPath = "/icon/Intel 128x128.png";
        } else if (cpuInfo.toLowerCase().contains("amd")) {
            iconPath = "/icon/AMD Radeon 128x128.png";
        } else {
            iconPath = "/icon/Unknown CPU 128x128.png"; // Default icon for unknown CPU
        }

        return new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource(iconPath))); // Load the icon
    }

    // Method to get the RAM icon
    public static ImageIcon getRamIcon() {
        return new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource("/icon/RAM 128x128.png"))); // Load the RAM icon
    }

    // Method to get the GPU icon
    public static ImageIcon getGpuIcon(String gpuInfo) {
        String iconPath;

        // Determine the icon path based on the type of GPU
        if (gpuInfo.toLowerCase().contains("intel")) {
            iconPath = "/icon/ARC 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("amd")) {
            iconPath = "/icon/AMD 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("nvidia")) {
            iconPath = "/icon/Nvidia 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("vmware")) {
            iconPath = "/icon/VMware 128x128.png";
        } else {
            iconPath = "/icon/Unknown GPU 128x128.png"; // Default icon for unknown GPU
        }

        return new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource(iconPath))); // Load the icon
    }

    // Method to create an information panel
    public static JPanel createInfoPanel(String title, String info, ImageIcon icon) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout to align to the left
        panel.setBorder(BorderFactory.createTitledBorder(title));

        // Create a panel for the icon and text
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // 5-pixel margin to the right of the icon

        JTextArea textArea = new JTextArea(info);
        textArea.setEditable(false); // Make the text area non-editable
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use a monospaced font
        textArea.setBackground(new Color(240, 240, 240)); // Light gray background for the text area
        textArea.setMargin(new Insets(10, 10, 10, 10)); // Add padding
        textArea.setFocusable(false); // Disable the cursor

        panel.add(iconLabel); // Add the icon to the panel
        panel.add(textArea); // Add the text next to the icon

        return panel;
    }
}
