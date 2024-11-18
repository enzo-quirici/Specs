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
    static ImageIcon gpuIcon; // Global icons

    public static void main(String[] args) {

        Upload uploadHandler = new Upload();

        // Load icons once at startup
        loadIcons();

        // Load the application icon
        icon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icon/Icon 128x128.png")));

        // Create the main window
        jframe = new JFrame("Java Specs");
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
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows and 2 columns for OS, CPU, GPU, and RAM

        // Create and add the panels for each category
        JPanel osPanel = InfoPanel.createInfoPanel("Operating System", Specs.getOperatingSystem(), osIcon);
        JPanel cpuPanel = InfoPanel.createInfoPanel("CPU", Specs.getCpuInfo(), cpuIcon);
        JPanel gpuPanel = InfoPanel.createInfoPanel("GPU", Specs.getGpuInfo(), gpuIcon);
        JPanel ramPanel = InfoPanel.createInfoPanel("RAM", Specs.getRamInfo(), ramIcon);

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
        updateTextArea((JPanel) mainPanel.getComponent(0), Specs.getOperatingSystem(), osIcon);
        updateTextArea((JPanel) mainPanel.getComponent(1), Specs.getCpuInfo(), cpuIcon);
        updateTextArea((JPanel) mainPanel.getComponent(2), Specs.getGpuInfo(), gpuIcon);
        updateTextArea((JPanel) mainPanel.getComponent(3), Specs.getRamInfo(), ramIcon);
    }

    // Method to load icons once
    private static void loadIcons() {
        if (osIcon == null) {
            osIcon = InfoPanel.getOsIcon();
        }
        if (cpuIcon == null) {
            cpuIcon = InfoPanel.getCpuIcon(Specs.getCpuInfo());
        }
        if (ramIcon == null) {
            ramIcon = InfoPanel.getRamIcon();
        }
        if (gpuIcon == null) {
            gpuIcon = InfoPanel.getGpuIcon(Specs.getGpuInfo());
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
