// InfoPanel.java

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InfoPanel {

    // Method to get the OS icon
    public static ImageIcon getOsIcon() {
        String osName = System.getProperty("os.name").toLowerCase(); // Get the OS name
        String osVersion = System.getProperty("os.version").toLowerCase(); // Get the OS version
        String iconPath;

        // Determine the icon path based on the OS and version
        if (osName.contains("win")) {
            if (osName.contains("10")) {
                iconPath = "/icon/Windows 10 128x128.png";
            } else if (osName.contains("11")) {
                iconPath = "/icon/Windows 11 128x128.png";
            } else {
                iconPath = "/icon/Windows 128x128.png"; // Default Windows icon
            }
        } else if (osName.contains("mac")) {
            if (osVersion.contains("11")) {
                iconPath = "/icon/Mac OS 11 128x128.png";
            } else if (osVersion.contains("12")) {
                iconPath = "/icon/Mac OS 12 128x128.png";
            } else if (osVersion.contains("13")) {
                iconPath = "/icon/Mac OS 13 128x128.png";
            } else if (osVersion.contains("14")) {
                iconPath = "/icon/Mac OS 14 128x128.png";
            } else if (osVersion.contains("15")) {
                iconPath = "/icon/Mac OS 15 128x128.png";
            } else {
                iconPath = "/icon/Mac OS 128x128.png"; // Default Mac OS icon
            }
        } else if (osName.contains("nix") || osName.contains("nux")) {
            // Check for specific Linux distributions
            if (osVersion.contains("ubuntu")) {
                iconPath = "/icon/Ubuntu Linux 128x128.png";
            } else if (osVersion.contains("debian")) {
                iconPath = "/icon/Debian Linux 128x128.png";
            } else if (osVersion.contains("fedora")) {
                iconPath = "/icon/Fedora Linux 128x128.png";
            } else if (osVersion.contains("gentoo")) {
                iconPath = "/icon/Gentoo Linux 128x128.png";
            } else if (osVersion.contains("arch")) {
                iconPath = "/icon/Arch Linux 128x128.png";
            } else if (osVersion.contains("manjaro")) {
                iconPath = "/icon/Manjaro Linux 128x128.png";
            } else {
                iconPath = "/icon/GNU Linux 128x128.png"; // Default Linux icon
            }
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
            iconPath = "/icon/AMD 128x128.png";
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
            iconPath = "/icon/AMD Radeon 128x128.png";
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

        // Use JTextPane for transparency
        JTextPane textPane = new JTextPane();
        textPane.setText(info);
        textPane.setEditable(false); // Make the text area non-editable
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use a monospaced font
        textPane.setBackground(new Color(0, 0, 0, 0)); // Set the background to transparent
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        textPane.setFocusable(false); // Disable the cursor

        panel.add(iconLabel); // Add the icon to the panel
        panel.add(textPane); // Add the text next to the icon

        return panel;
    }
}
