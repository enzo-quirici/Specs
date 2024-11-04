//GUI.java

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI {
    private static ImageIcon icon;
    private static JFrame jframe;
    private static JPanel mainPanel; // Declare mainPanel here so it's accessible everywhere

    public static void main(String[] args) {
        // Load the application icon
        icon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icon/Icon 128x128.png")));

        // Create the main window
        jframe = new JFrame("Java Specs");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(1200, 600);
        jframe.setLocationRelativeTo(null); // Center the window on the screen
        jframe.setIconImage(icon.getImage()); // Set the window icon

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // File menu items
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(e -> refreshSpecs()); // Refresh via the button
        fileMenu.add(refreshMenuItem);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem autoRefreshMenuItem = new JMenuItem("Auto Refresh");
        autoRefreshMenuItem.addActionListener(e -> Refresh.showAutoRefreshDialog(jframe, mainPanel)); // Pass mainPanel here
        settingsMenu.add(autoRefreshMenuItem);

        // About menu
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
        JMenu StressTestMenu = new JMenu("Stress Test");
        JMenuItem StressTestMenuItem = new JMenuItem("Stress Test");
        StressTestMenuItem.addActionListener(e -> StressTest.showStressTest(jframe, icon));
        StressTestMenu.add(StressTestMenuItem);
        menuBar.add(StressTestMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> Help.showHelp(jframe, icon));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        // Create the main panel with GridLayout in 2x2
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows and 2 columns for OS, CPU, GPU, and RAM

        // Create and add panels for each category
        JPanel osPanel = InfoPanel.createInfoPanel("Operating System", Specs.getOperatingSystem(), InfoPanel.getOsIcon());
        JPanel cpuPanel = InfoPanel.createInfoPanel("CPU", Specs.getCpuInfo(), InfoPanel.getCpuIcon(Specs.getCpuInfo()));
        JPanel gpuPanel = InfoPanel.createInfoPanel("GPU", Specs.getGpuInfo(), InfoPanel.getGpuIcon(Specs.getGpuInfo()));
        JPanel ramPanel = InfoPanel.createInfoPanel("RAM", Specs.getRamInfo(), InfoPanel.getRamIcon());

        mainPanel.add(osPanel);
        mainPanel.add(cpuPanel);
        mainPanel.add(gpuPanel);
        mainPanel.add(ramPanel);

        // Add components to the window
        jframe.setJMenuBar(menuBar);
        jframe.add(mainPanel);

        // Make the window visible
        jframe.setVisible(true);

        // Start the auto-refresh timer with the default interval, passing mainPanel
        Refresh.startAutoRefresh(mainPanel);
    }

    // Method to refresh the system specifications
    private static void refreshSpecs() {
        // Refresh specifications for each category
        updateTextArea((JPanel) mainPanel.getComponent(0), Specs.getOperatingSystem());
        updateTextArea((JPanel) mainPanel.getComponent(1), Specs.getCpuInfo());
        updateTextArea((JPanel) mainPanel.getComponent(2), Specs.getGpuInfo());
        updateTextArea((JPanel) mainPanel.getComponent(3), Specs.getRamInfo());
    }


    // Helper method to update the text pane in a panel
    static void updateTextArea(JPanel panel, String newInfo) {
        JTextPane textPane = (JTextPane) panel.getComponent(1); // Safe cast assuming the structure is correct
        textPane.setText(newInfo);
    }

}