// platform/LinuxRamInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxRamInfo {
    public static String getRamInfo() {
        long totalPhysicalMemory = 0;
        long freePhysicalMemory = 0;
        long usedPhysicalMemory = 0;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "cat /proc/meminfo | grep -E 'MemTotal|MemAvailable'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            long memTotal = 0;
            long memAvailable = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    memTotal = Long.parseLong(line.split("\\s+")[1]); // Total memory in kB
                } else if (line.startsWith("MemAvailable:")) {
                    memAvailable = Long.parseLong(line.split("\\s+")[1]); // Available memory in kB
                }
            }

            // Convert from kB to MB
            totalPhysicalMemory = memTotal / 1024; // Total memory in MB
            freePhysicalMemory = memAvailable / 1024; // Free memory in MB
            usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory; // Used memory
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving RAM information for Linux.";
        }

        return "RAM (Total): " + totalPhysicalMemory + " MB\n" +
                "RAM (Used): " + usedPhysicalMemory + " MB\n" +
                "RAM (Free): " + freePhysicalMemory + " MB";
    }
}
