// InfoPanel.java

import platform.LinuxOSInfo;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InfoPanel {

    // Method to get the OS icon
    public static ImageIcon getOsIcon() {
        String osName = System.getProperty("os.name").toLowerCase(); // Get the OS name
        String iconPath;

        // Handle Windows OS
        if (osName.contains("win")) {
            String osVersion = System.getProperty("os.name").toLowerCase(); // Get the OS version
            if (osVersion.contains("10")) {
                iconPath = "/icon/Windows 10 128x128.png";
            } else if (osVersion.contains("11")) {
                iconPath = "/icon/Windows 11 128x128.png";
            } else {
                iconPath = "/icon/Windows 128x128.png"; // Default Windows icon
            }
        }
        // Handle macOS
        else if (osName.contains("mac")) {
            String osVersion = System.getProperty("os.version").toLowerCase(); // Get the OS version
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
        }
        // Handle Linux
        else if (osName.contains("nix") || osName.contains("nux")) {
            String osVersion = LinuxOSInfo.getLinuxOSVersion(); // Get Linux version from the new class
            // Determine the icon path based on the OS version
            if (osVersion.toLowerCase().contains("ubuntu")) {
                iconPath = "/icon/Ubuntu Linux 128x128.png";
            } else if (osVersion.toLowerCase().contains("debian")) {
                iconPath = "/icon/Debian Linux 128x128.png";
            } else if (osVersion.toLowerCase().contains("fedora")) {
                iconPath = "/icon/Fedora Linux 128x128.png";
            } else if (osVersion.toLowerCase().contains("gentoo")) {
                iconPath = "/icon/Gentoo Linux 128x128.png";
            } else if (osVersion.toLowerCase().contains("arch")) {
                iconPath = "/icon/Arch Linux 128x128.png";
            } else if (osVersion.toLowerCase().contains("manjaro")) {
                iconPath = "/icon/Manjaro Linux 128x128.png";
            } else {
                iconPath = "/icon/GNU Linux 128x128.png"; // Default Linux icon
            }
        } else {
            iconPath = "/icon/Unknown 128x128.png"; // Default icon for unknown OS
        }

        // Load the icon and handle potential issues
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource(iconPath), "Icon not found: " + iconPath));
        return icon; // Return the loaded icon
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

        // Set the background color of the panel (optional)
        panel.setBackground(new Color(240, 240, 240)); // Light gray background for the panel

        // Create a panel for the icon and text
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // 5-pixel margin to the right of the icon

        // Use JTextPane for transparency
        JTextPane textPane = new JTextPane();
        textPane.setText(info);
        textPane.setEditable(false); // Make the text area non-editable
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use a monospaced font
        textPane.setBackground(new Color(240, 240, 240)); // Light yellow background for the text pane
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        textPane.setFocusable(false); // Disable the cursor

        panel.add(iconLabel); // Add the icon to the panel
        panel.add(textPane); // Add the text next to the icon

        return panel;
    }
}