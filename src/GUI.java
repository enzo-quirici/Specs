//GUI.java

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI {
    private static ImageIcon icon;
    private static JFrame jframe;
    private static JPanel mainPanel;
    static ImageIcon osIcon;
    static ImageIcon cpuIcon;
    static ImageIcon ramIcon;
    static ImageIcon gpuIcon;

    public static void main(String[] args) {

        Upload uploadHandler = new Upload();

        // Load icons once at startup
        loadIcons();

        // Load the application icon
        icon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icon/Icon 128x128.png")));

        // Create the main window
        jframe = new JFrame("Specs");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(1200, 600);
        jframe.setLocationRelativeTo(null); // Center the window on the screen
        jframe.setIconImage(icon.getImage()); // Set the window's icon

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // File menu items
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(e -> refreshSpecs()); // Refresh via the button
        fileMenu.add(refreshMenuItem);

        // Upload menu items
        JMenuItem uploadMenuItem = new JMenuItem("Validate");
        uploadMenuItem.addActionListener(e -> uploadHandler.uploadSpecs());
        fileMenu.add(uploadMenuItem);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem autoRefreshMenuItem = new JMenuItem("Auto Refresh");
        autoRefreshMenuItem.addActionListener(e -> Refresh.showAutoRefreshDialog(jframe, mainPanel)); // Pass mainPanel here
        settingsMenu.add(autoRefreshMenuItem);

        // About menu item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> About.showAbout(jframe));
        settingsMenu.add(aboutItem);
        fileMenu.add(settingsMenu);

        // Quit menu
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.addSeparator(); // Separator before Quit
        fileMenu.add(quitMenuItem);

        // Add the File menu to the menu bar
        menuBar.add(fileMenu);

        // Stress Test menu
        JMenu stressTestMenu = new JMenu("Stress Test");
        JMenuItem stressTestMenuItem = new JMenuItem("Stress Test");
        stressTestMenuItem.addActionListener(e -> StressTest.showStressTest(jframe, icon));
        stressTestMenu.add(stressTestMenuItem);
        menuBar.add(stressTestMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> Help.showHelp(jframe, icon));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        // Create the main panel with GridLayout in 2x2
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Create and add the panels for each category
        JPanel osPanel = InfoPanel.createInfoPanel("Operating System", "Operating System :" + " " + Specs.getOperatingSystemName() + "\n" + "Version :" + " " + Specs.getOperatingSystemVersion(), osIcon);
        JPanel cpuPanel = InfoPanel.createInfoPanel("CPU", "CPU :" + " " + Specs.getCpuName() + "\n" + "Cores :" + " " + Specs.getCpuCores() + "\n" + "Threads :" + " " + Specs.getCpuThreads(), cpuIcon);
        long vram = Long.parseLong(Specs.getGpuVram()); JPanel gpuPanel = InfoPanel.createInfoPanel("GPU", "GPU :" + " " + Specs.getGpuName() + "\nVram :" + " " + (vram == 0L ? "Shared" : vram + " " + "MB"), gpuIcon);
        JPanel ramPanel = InfoPanel.createInfoPanel("RAM", "RAM (Total) :" + " " + Specs.getRamSize() + " " + "MB" + "\n" + "RAM (Used) :" + " " + Specs.getRamUsed() + " " + "MB" + "\n" + "RAM (Free) :" + " " + Specs.getRamFree() + " " + "MB", ramIcon);

        mainPanel.add(osPanel);
        mainPanel.add(cpuPanel);
        mainPanel.add(gpuPanel);
        mainPanel.add(ramPanel);

        // Add components to the window
        jframe.setJMenuBar(menuBar);
        jframe.add(mainPanel);

        // Make the window visible
        jframe.setVisible(true);

        // Start the timer for auto-refresh with the default interval, passing mainPanel
        Refresh.startAutoRefresh(mainPanel);
    }

    // Method to refresh system specifications
    private static void refreshSpecs() {
        // Reuse the already loaded icons
        updateTextArea((JPanel) mainPanel.getComponent(0), "Operating System :" + " " + Specs.getOperatingSystemName() + "\n" + "Version :" + " " + Specs.getOperatingSystemVersion(), osIcon);
        updateTextArea((JPanel) mainPanel.getComponent(1), "CPU :" + " " + Specs.getCpuName() + "\n" + "Cores :" + " " + Specs.getCpuCores() + "\n" + "Threads :" + " " + Specs.getCpuThreads(), cpuIcon);
        long vram = Long.parseLong(Specs.getGpuVram()); JPanel gpuPanel = InfoPanel.createInfoPanel("GPU", "GPU :" + " " + Specs.getGpuName() + "\nVram :" + " " + (vram == 0L ? "Shared" : vram + " " + "MB"), gpuIcon);
        updateTextArea((JPanel) mainPanel.getComponent(3), "RAM (Total) :" + " " + Specs.getRamSize() + " " + "MB" + "\n" + "RAM (Used) :" + " " + Specs.getRamUsed() + " " + "MB" + "\n" + "RAM (Free) :" + " " + Specs.getRamFree() + " " + "MB", ramIcon);
    }

    // Method to load icons once
    private static void loadIcons() {
        if (osIcon == null) {
            osIcon = InfoPanel.getOsIcon();
        }
        if (cpuIcon == null) {
            cpuIcon = InfoPanel.getCpuIcon(Specs.getCpuName());
        }
        if (ramIcon == null) {
            ramIcon = InfoPanel.getRamIcon();
        }
        if (gpuIcon == null) {
            gpuIcon = InfoPanel.getGpuIcon(Specs.getGpuName());
        }
    }

    // Helper method to update text in the panels
    static void updateTextArea(JPanel panel, String newInfo, ImageIcon icon) {
        JTextPane textPane = (JTextPane) panel.getComponent(1);
        textPane.setText(newInfo);

        JLabel iconLabel = (JLabel) panel.getComponent(0);
        iconLabel.setIcon(icon);  // Update the icon
    }
}
