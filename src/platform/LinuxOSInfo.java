package platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxOSInfo {
    public static String getLinuxOSVersion() {
        String osVersion = "Unknown Linux"; // Default value
        String osName = System.getProperty("os.name").toLowerCase(); // Get the OS name

        // If the OS is Linux, try to retrieve the name and version via /etc/os-release
        if (osName.equals("linux")) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "grep -E '^(VERSION|NAME)=' /etc/os-release");
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                String name = null;
                String version = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("NAME=")) {
                        name = line.replace("NAME=", "").replace("\"", "").trim();
                    } else if (line.startsWith("VERSION=")) {
                        // Extract version and remove any content in parentheses
                        version = line.replace("VERSION=", "").replace("\"", "").trim();
                        version = version.split("\\s*\\(")[0].trim(); // Remove content in parentheses
                    }
                }

                // Combine NAME and VERSION into a single string for osVersion
                if (name != null && version != null) {
                    osVersion = name + " " + version; // Combine name and version
                }

            } catch (IOException e) {
                e.printStackTrace();
                osVersion = "Error retrieving Linux OS information."; // Fallback error message
            }
        }

        // If osVersion is still the default value, use the default Java method to get the OS version
        if (osVersion.equals("Unknown Linux")) {
            osVersion = System.getProperty("os.version");
        }

        return osVersion; // Return the determined OS version
    }
}
