//platform/WindowsCpuInfo

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WindowsCpuInfo {

    public static String getCpuName() {
        String cpuName = "Unknown CPU";

        // Try first with the wmic command
        cpuName = getCpuNameFromWmic();

        // If the wmic method fails, use reg query as fallback
        if (cpuName.equals("Unknown CPU") || cpuName.equals("Error retrieving CPU name")) {
            cpuName = getCpuNameFromRegistry();
        }

        return cpuName;
    }

    private static String getCpuNameFromWmic() {
        String cpuName = "Unknown CPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "wmic", "cpu", "get", "name"
            );
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            reader.readLine();

            // Read the output and get the CPU name
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    cpuName = line; // The CPU name is in this line
                    break;
                }
            }
            process.waitFor();
        } catch (Exception e) {
            cpuName = "Error retrieving CPU name";
        }
        return cpuName;
    }

    // Method to get the CPU name via the reg query command
    private static String getCpuNameFromRegistry() {
        String cpuName = "Unknown CPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "reg", "query",
                    "HKEY_LOCAL_MACHINE\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0",
                    "/v", "ProcessorNameString"
            );
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Read the output of the command
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Check if the line contains "ProcessorNameString"
                if (line.contains("ProcessorNameString")) {
                    // Split the line by spaces and get the CPU name
                    String[] parts = line.split("\\s{2,}"); // Split by 2 or more spaces
                    if (parts.length > 1) {
                        cpuName = parts[parts.length - 1].trim(); // The CPU name is the last part
                    }
                    break; // We've found the CPU name, no need to continue
                }
            }
            process.waitFor();
        } catch (Exception e) {
            cpuName = "Error retrieving CPU name";
        }
        return cpuName;
    }

    // Method to get the number of physical cores on Windows
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
