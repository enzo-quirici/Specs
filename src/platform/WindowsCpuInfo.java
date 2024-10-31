package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WindowsCpuInfo {
    public static String getCpuName() {
        String cpuName = "Unknown CPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("wmic", "cpu", "get", "name");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Ignore the first line (the header "Name")
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    cpuName = line; // Extracts the CPU name
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            cpuName = "Error retrieving CPU name";
        }
        return cpuName;
    }
}
