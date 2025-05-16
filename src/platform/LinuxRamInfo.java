package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxRamInfo {

    // Method to get total physical memory in MB
    public static long getRamSize() {
        long totalPhysicalMemory = 0;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "cat /proc/meminfo | grep MemTotal");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    totalPhysicalMemory = Long.parseLong(line.split("\\s+")[1]) / 1024; // Convert kB to MB
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Indicating an error
        }

        return totalPhysicalMemory;
    }

    // Method to get used physical memory in MB
    public static long getRamUsed() {
        long totalPhysicalMemory = getRamSize();
        long freePhysicalMemory = getRamFree();
        return totalPhysicalMemory - freePhysicalMemory;
    }

    // Method to get free physical memory in MB
    public static long getRamFree() {
        long freePhysicalMemory = 0;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "cat /proc/meminfo | grep MemAvailable");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("MemAvailable:")) {
                    freePhysicalMemory = Long.parseLong(line.split("\\s+")[1]) / 1024; // Convert kB to MB
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Indicating an error
        }

        return freePhysicalMemory;
    }
}
