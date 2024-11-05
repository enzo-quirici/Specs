//platform/WindowsCpuInfo

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

    // Méthode pour obtenir le nombre de cœurs physiques sur Windows
    public static int getWindowsPhysicalCores() {
        int cores = Runtime.getRuntime().availableProcessors(); // Fallback to logical processors
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("wmic", "cpu", "get", "NumberOfCores");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Ignore the first line (header "NumberOfCores")
            reader.readLine();

            // Read the output and parse the number of cores
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove any surrounding spaces
                if (line.matches("\\d+")) { // If the line contains only digits, it's a valid number
                    cores = Integer.parseInt(line); // Parse the number
                    break; // Exit the loop after finding the first valid number
                }
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cores; // Return the number of physical cores
    }
}
