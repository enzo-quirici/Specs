package platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxOSInfo {
    public static String getLinuxOSVersion() {
        String osVersion = "Unknown Linux";
        String osName = System.getProperty("os.name").toLowerCase();

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
                        version = line.replace("VERSION=", "").replace("\"", "").trim();
                    }
                }

                // Cas Arch Linux
                if ((version != null && version.toLowerCase().contains("arch")) ||
                        (name != null && name.toLowerCase().contains("arch"))) {
                    Process unameProc = new ProcessBuilder("uname", "-r").start();
                    BufferedReader unameReader = new BufferedReader(new InputStreamReader(unameProc.getInputStream()));
                    String kernelVersion = unameReader.readLine().trim();
                    String cleanedKernelVersion = kernelVersion.split("-")[0];
                    osVersion = cleanedKernelVersion + " Arch Linux";
                }
                // Cas Gentoo Linux
                else if ((version != null && version.toLowerCase().contains("gentoo")) ||
                        (name != null && name.toLowerCase().contains("gentoo"))) {
                    Process unameProc = new ProcessBuilder("uname", "-r").start();
                    BufferedReader unameReader = new BufferedReader(new InputStreamReader(unameProc.getInputStream()));
                    String kernelVersion = unameReader.readLine().trim();
                    String cleanedKernelVersion = kernelVersion.split("-")[0];
                    osVersion = cleanedKernelVersion + " Gentoo Linux";
                }
                // Cas général
                else if (name != null && version != null) {
                    version = version.split("\\s*\\(")[0].trim(); // enlever le contenu entre parenthèses
                    osVersion = name + " " + version;
                }

            } catch (IOException e) {
                e.printStackTrace();
                osVersion = "Error retrieving Linux OS information.";
            }
        }

        if (osVersion.equals("Unknown Linux")) {
            osVersion = System.getProperty("os.version");
        }

        return osVersion;
    }
}
