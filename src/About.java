//About.java

import javax.swing.*;
import java.util.Objects;

public class About {
    public static void showAbout(JFrame parent) {
        // Load the icon from the Icon folder inside src
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/icon/Icon 128x128.png")));
        JOptionPane.showMessageDialog(parent,
                "Java Specs\nVersion 1.1\n© 2024",
                "About",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
    }
}
