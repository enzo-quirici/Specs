package platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BsdOSInfo {

    public static String getBsdOSVersion() {
        String osVersion = "Unknown BSD";
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("freebsd")) {
                // FreeBSD : uname -r ou freebsd-version
                Process process = new ProcessBuilder("freebsd-version").start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String version = reader.readLine();
                if (version != null && !version.isEmpty()) {
                    osVersion = "FreeBSD " + version.trim();
                }
            } else if (osName.contains("openbsd") || osName.contains("netbsd")) {
                // OpenBSD/NetBSD : uname -r pour la version
                Process process = new ProcessBuilder("uname", "-r").start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String version = reader.readLine();
                if (version != null && !version.isEmpty()) {
                    if (osName.contains("openbsd")) {
                        osVersion = "OpenBSD " + version.trim();
                    } else {
                        osVersion = "NetBSD " + version.trim();
                    }
                }
            }
        } catch (IOException e) {
            osVersion = "Error retrieving BSD OS information.";
        }

        return osVersion;
    }
}
