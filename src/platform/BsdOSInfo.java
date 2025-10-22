package platform;

import java.io.*;

public class BsdOSInfo {

    public static String getBsdOSVersion() {
        File osRelease = new File("/etc/os-release");
        if (osRelease.exists()) {
            String name = null;
            String pretty = null;
            try (BufferedReader br = new BufferedReader(new FileReader(osRelease))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("NAME=")) {
                        name = line.substring(5).replace("\"", "");
                    } else if (line.startsWith("PRETTY_NAME=")) {
                        pretty = line.substring(12).replace("\"", "");
                    }
                }
            } catch (IOException ignored) {
            }

            if (pretty != null && !pretty.isEmpty()) return pretty;
            if (name != null && !name.isEmpty()) return name;
        }

        return "Unknown BSD";
    }
}