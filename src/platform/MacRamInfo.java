// platform/MacRamInfo.java

package platform;

import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class MacRamInfo {

    public static String getRamInfo() {
        long totalPhysicalMemory = 0;
        long usedPhysicalMemory = 0;
        long freePhysicalMemory = 0;

        try {
            // Create a ProcessBuilder to run the "vm_stat" command
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "vm_stat");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            long pagesFree = 0;
            long pagesWired = 0;
            long pagesActive = 0;
            long pageSize = 4096; // Default page size, often 4 KB on macOS

            // Read and extract information from "vm_stat"
            while ((line = reader.readLine()) != null) {
                if (line.contains("Pages free:")) {
                    pagesFree = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                } else if (line.contains("Pages wired down:")) {
                    pagesWired = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                } else if (line.contains("Pages active:")) {
                    pagesActive = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                }
            }

            // Retrieve total physical memory using OperatingSystemMXBean
            OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            totalPhysicalMemory = osMXBean.getTotalMemorySize() / (1024 * 1024); // Convert to MB

            // Calculate used memory (wired + active) and free memory
            usedPhysicalMemory = ((pagesWired + pagesActive) * pageSize) / (1024 * 1024); // Convert to MB
            freePhysicalMemory = totalPhysicalMemory - usedPhysicalMemory;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving RAM information for macOS.";
        }

        // Return the RAM information as a formatted string
        return "RAM (Total) : " + totalPhysicalMemory + " MB\n" +
                "RAM (Used) : " + usedPhysicalMemory + " MB\n" +
                "RAM (Free) : " + freePhysicalMemory + " MB";
    }
}
