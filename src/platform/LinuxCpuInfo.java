// platform/LinuxCpuInfo.java

package platform;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxCpuInfo {

    /**
     * Returns the name of the CPU by trying OSHI first, then fallback to /proc/cpuinfo if it fails.
     */
    public static String getCpuName() {
        String cpuName = getCpuNameFromOshi();  // First, try with OSHI

        if (cpuName.equals("Unknown CPU")) {  // If OSHI fails, fallback to /proc/cpuinfo
            cpuName = getCpuNameFromProcCpuinfo();
        }

        return cpuName;
    }

    /**
     * Try to get the CPU name from OSHI.
     *
     * @return CPU name or "Unknown CPU" if not found.
     */
    private static String getCpuNameFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            return processor.getProcessorIdentifier().getName();  // Get CPU name from OSHI
        } catch (Exception e) {
            // If OSHI fails, return "Unknown CPU"
            return "Unknown CPU";
        }
    }

    /**
     * Get the CPU name from /proc/cpuinfo.
     *
     * @return CPU name or "Error retrieving CPU name" if not found.
     */
    private static String getCpuNameFromProcCpuinfo() {
        String cpuName = "Unknown CPU";
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("model name")) {
                    cpuName = line.split(":")[1].trim(); // Extract the CPU name
                    break;
                }
            }
        } catch (IOException e) {
            cpuName = "Error retrieving CPU name"; // In case of error
        }
        return cpuName;
    }

    /**
     * Returns the total number of physical CPU cores on a Linux system.
     * This method tries to use OSHI first and falls back to lscpu and /proc/cpuinfo if it fails.
     *
     * @return Number of physical CPU cores.
     */
    public static int getLinuxPhysicalCores() throws IOException {
        int cores = getPhysicalCoresFromOshi();  // First, try with OSHI

        if (cores == 0) {  // If OSHI fails, fallback to old methods
            cores = getLinuxPhysicalCoresFromProcCpuinfo();
            if (cores == 0) {
                cores = getLinuxPhysicalCoresFromLscpu();
            }
        }

        return cores;
    }

    /**
     * Get the number of physical CPU cores from OSHI.
     *
     * @return Number of physical CPU cores or 0 if not found.
     */
    private static int getPhysicalCoresFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            return processor.getPhysicalProcessorCount();  // Get physical cores from OSHI
        } catch (Exception e) {
            // If OSHI fails, return 0
            return 0;
        }
    }

    /**
     * Get the number of physical CPU cores from /proc/cpuinfo.
     *
     * @return Number of physical CPU cores or 0 if not found.
     */
    private static int getLinuxPhysicalCoresFromProcCpuinfo() throws IOException {
        int cores = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("processor")) {
                    cores++;
                }
            }
        }
        return cores;
    }

    /**
     * Get the number of physical CPU cores using lscpu command.
     *
     * @return Number of physical CPU cores or 0 if not found.
     * @throws IOException If an error occurs during execution.
     */
    private static int getLinuxPhysicalCoresFromLscpu() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "lscpu | grep 'Core(s) per socket:' | awk '{print $NF}'");
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String coresLine = reader.readLine();

            if (coresLine != null && coresLine.matches("\\d+")) {
                return Integer.parseInt(coresLine.trim());
            }
        }
        return 0;  // Return 0 if method fails
    }
}
