package platform;

import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class MacRamInfo {

    // Method to get total physical memory in MB
    public static long getRamSize() {
        long totalPhysicalMemory = 0;

        try {
            // Retrieve total physical memory using OperatingSystemMXBean
            OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            totalPhysicalMemory = osMXBean.getTotalMemorySize() / (1024 * 1024); // Convert to MB
        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Indicating an error
        }

        return totalPhysicalMemory;
    }

    // Method to get used physical memory in MB
    public static long getRamUsed() {
        long usedPhysicalMemory = 0;
        long pagesWired = 0;
        long pagesActive = 0;
        long pageSize = 4096; // Default page size, often 4 KB on macOS

        try {
            // Create a ProcessBuilder to run the "vm_stat" command
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "vm_stat");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Read and extract information from "vm_stat"
            while ((line = reader.readLine()) != null) {
                if (line.contains("Pages wired down:")) {
                    pagesWired = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                } else if (line.contains("Pages active:")) {
                    pagesActive = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                }
            }

            // Calculate used memory (wired + active) in MB
            usedPhysicalMemory = ((pagesWired + pagesActive) * pageSize) / (1024 * 1024); // Convert to MB

        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Indicating an error
        }

        return usedPhysicalMemory;
    }

    // Method to get free physical memory in MB
    public static long getRamFree() {
        long freePhysicalMemory = 0;

        try {
            long totalPhysicalMemory = getRamSize();
            long usedPhysicalMemory = getRamUsed();
            freePhysicalMemory = totalPhysicalMemory - usedPhysicalMemory;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Indicating an error
        }

        return freePhysicalMemory;
    }
}
