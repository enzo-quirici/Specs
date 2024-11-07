// InfoPanel.java

import platform.LinuxOSInfo;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InfoPanel {

    // General method to load an icon from a path
    private static ImageIcon loadIcon(String path) {
        try {
            return new ImageIcon(Objects.requireNonNull(InfoPanel.class.getResource(path), "Icon not found: " + path));
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            return new ImageIcon(); // Return an empty icon in case of error
        }
    }

    // Method to get the OS icon
    public static ImageIcon getOsIcon() {
        String osName = System.getProperty("os.name").toLowerCase(); // Get the OS name
        String osVersion = System.getProperty("os.version", "").toLowerCase(); // Get the OS version

        String iconPath = getOsIconPath(osName, osVersion);
        return loadIcon(iconPath);
    }

    // Determine OS icon path based on the OS and version
    private static String getOsIconPath(String osName, String osVersion) {
        if (osName.contains("win")) {
            if (osName.contains("10")) return "/icon/Windows 10 128x128.png";
            if (osName.contains("11")) return "/icon/Windows 11 128x128.png";
            return "/icon/Windows 128x128.png"; // Default Windows icon
        } else if (osName.contains("mac")) {
            return getMacOsIconPath(osVersion);
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return getLinuxOsIconPath(LinuxOSInfo.getLinuxOSVersion());
        } else {
            return "/icon/Unknown 128x128.png"; // Default for unknown OS
        }
    }

    // Get macOS icon path based on version
    private static String getMacOsIconPath(String osVersion) {
        if (osVersion.contains("11")) return "/icon/Mac OS 11 128x128.png";
        if (osVersion.contains("12")) return "/icon/Mac OS 12 128x128.png";
        if (osVersion.contains("13")) return "/icon/Mac OS 13 128x128.png";
        if (osVersion.contains("14")) return "/icon/Mac OS 14 128x128.png";
        if (osVersion.contains("15")) return "/icon/Mac OS 15 128x128.png";
        return "/icon/Mac OS 128x128.png"; // Default Mac OS icon
    }

    // Get Linux OS icon path based on the version (Ubuntu, Debian, etc.)
    private static String getLinuxOsIconPath(String osVersion) {
        if (osVersion.toLowerCase().contains("ubuntu")) return "/icon/Ubuntu Linux 128x128.png";
        if (osVersion.toLowerCase().contains("debian")) return "/icon/Debian Linux 128x128.png";
        if (osVersion.toLowerCase().contains("fedora")) return "/icon/Fedora Linux 128x128.png";
        if (osVersion.toLowerCase().contains("gentoo")) return "/icon/Gentoo Linux 128x128.png";
        if (osVersion.toLowerCase().contains("arch")) return "/icon/Arch Linux 128x128.png";
        if (osVersion.toLowerCase().contains("manjaro")) return "/icon/Manjaro Linux 128x128.png";
        return "/icon/GNU Linux 128x128.png"; // Default Linux icon
    }

    // Method to get the CPU icon
    public static ImageIcon getCpuIcon(String cpuInfo) {
        String iconPath = cpuInfo.toLowerCase().contains("intel") ? "/icon/Intel 128x128.png" :
                cpuInfo.toLowerCase().contains("amd") ? "/icon/AMD 128x128.png" :
                        "/icon/Unknown CPU 128x128.png"; // Default CPU icon
        return loadIcon(iconPath);
    }

    // Method to get the RAM icon
    public static ImageIcon getRamIcon() {
        return loadIcon("/icon/RAM 128x128.png"); // Load RAM icon
    }

    // Method to get the GPU icon
    public static ImageIcon getGpuIcon(String gpuInfo) {
        String iconPath;
        if (gpuInfo.toLowerCase().contains("intel")) {
            iconPath = "/icon/ARC 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("amd")) {
            iconPath = "/icon/AMD Radeon 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("nvidia")) {
            iconPath = "/icon/Nvidia 128x128.png";
        } else if (gpuInfo.toLowerCase().contains("vmware")) {
            iconPath = "/icon/VMware 128x128.png";
        } else {
            iconPath = "/icon/Unknown GPU 128x128.png"; // Default GPU icon
        }
        return loadIcon(iconPath);
    }

    // Method to create an information panel with transparent background
    public static JPanel createInfoPanel(String title, String info, ImageIcon icon) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout to align to the left
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // 5-pixel margin to the right of the icon

        // Use JTextPane for transparency
        JTextPane textPane = new JTextPane();
        textPane.setText(info);
        textPane.setEditable(false); // Make the text area non-editable
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use a monospaced font
        textPane.setBackground(new Color(0, 0, 0, 0)); // Transparent background for the text pane
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        textPane.setFocusable(false); // Disable the cursor
        textPane.setOpaque(false); // Ensure the text area is transparent

        panel.add(iconLabel); // Add the icon to the panel
        panel.add(textPane); // Add the text next to the icon

        return panel;
    }
}