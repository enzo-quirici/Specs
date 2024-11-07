import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI {
    private static ImageIcon icon;
    private static JFrame jframe;
    private static JPanel mainPanel; // Declare mainPanel here so it's accessible everywhere

    static ImageIcon osIcon;
    static ImageIcon cpuIcon;
    static ImageIcon ramIcon;
    static ImageIcon gpuIcon; // Icônes globales

    public static void main(String[] args) {
        // Charger les icônes une seule fois au démarrage
        loadIcons();

        // Charger l'icône de l'application
        icon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icon/Icon 128x128.png")));

        // Créer la fenêtre principale
        jframe = new JFrame("Java Specs");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(1200, 600);
        jframe.setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        jframe.setIconImage(icon.getImage()); // Définir l'icône de la fenêtre

        // Créer la barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Éléments du menu File
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(e -> refreshSpecs()); // Rafraîchir via le bouton
        fileMenu.add(refreshMenuItem);

        // Menu Settings
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem autoRefreshMenuItem = new JMenuItem("Auto Refresh");
        autoRefreshMenuItem.addActionListener(e -> Refresh.showAutoRefreshDialog(jframe, mainPanel)); // Passer mainPanel ici
        settingsMenu.add(autoRefreshMenuItem);

        // Éléments du menu About
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> About.showAbout(jframe));
        settingsMenu.add(aboutItem);
        fileMenu.add(settingsMenu);

        // Menu Quitter
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.addSeparator(); // Séparateur avant Quit
        fileMenu.add(quitMenuItem);

        // Ajouter le menu File à la barre de menu
        menuBar.add(fileMenu);

        // Menu Stress Test
        JMenu stressTestMenu = new JMenu("Stress Test");
        JMenuItem stressTestMenuItem = new JMenuItem("Stress Test");
        stressTestMenuItem.addActionListener(e -> StressTest.showStressTest(jframe, icon));
        stressTestMenu.add(stressTestMenuItem);
        menuBar.add(stressTestMenu);

        // Menu Help
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(e -> Help.showHelp(jframe, icon));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        // Créer le panneau principal avec GridLayout en 2x2
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 lignes et 2 colonnes pour OS, CPU, GPU, et RAM

        // Créer et ajouter les panneaux pour chaque catégorie
        JPanel osPanel = InfoPanel.createInfoPanel("Operating System", Specs.getOperatingSystem(), osIcon);
        JPanel cpuPanel = InfoPanel.createInfoPanel("CPU", Specs.getCpuInfo(), cpuIcon);
        JPanel gpuPanel = InfoPanel.createInfoPanel("GPU", Specs.getGpuInfo(), gpuIcon);
        JPanel ramPanel = InfoPanel.createInfoPanel("RAM", Specs.getRamInfo(), ramIcon);

        mainPanel.add(osPanel);
        mainPanel.add(cpuPanel);
        mainPanel.add(gpuPanel);
        mainPanel.add(ramPanel);

        // Ajouter les composants à la fenêtre
        jframe.setJMenuBar(menuBar);
        jframe.add(mainPanel);

        // Rendre la fenêtre visible
        jframe.setVisible(true);

        // Démarrer le timer pour l'auto-refresh avec l'intervalle par défaut, en passant mainPanel
        Refresh.startAutoRefresh(mainPanel);
    }

    // Méthode pour rafraîchir les spécifications système
    private static void refreshSpecs() {
        // Réutiliser les icônes déjà chargées
        updateTextArea((JPanel) mainPanel.getComponent(0), Specs.getOperatingSystem(), osIcon);
        updateTextArea((JPanel) mainPanel.getComponent(1), Specs.getCpuInfo(), cpuIcon);
        updateTextArea((JPanel) mainPanel.getComponent(2), Specs.getGpuInfo(), gpuIcon);
        updateTextArea((JPanel) mainPanel.getComponent(3), Specs.getRamInfo(), ramIcon);
    }

    // Méthode pour charger les icônes une seule fois
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

    // Méthode d'assistance pour mettre à jour le texte dans les panneaux
    static void updateTextArea(JPanel panel, String newInfo, ImageIcon icon) {
        JTextPane textPane = (JTextPane) panel.getComponent(1);
        textPane.setText(newInfo);

        JLabel iconLabel = (JLabel) panel.getComponent(0);
        iconLabel.setIcon(icon);  // Mise à jour de l'icône
    }
}
