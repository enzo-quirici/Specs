// platform/LinuxCpuInfo.java

package platform;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxCpuInfo {

    public static String getCpuName() {
        String cpuName = getCpuNameFromOshi();

        if (cpuName.equals("Unknown CPU")) {
            cpuName = getCpuNameFromProcCpuinfo();
        }

        return cpuName;
    }

    private static String getCpuNameFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            return processor.getProcessorIdentifier().getName();
        } catch (Exception e) {
            return "Unknown CPU";
        }
    }

    private static String getCpuNameFromProcCpuinfo() {
        String cpuName = "Unknown CPU";
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("model name")) {
                    cpuName = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            cpuName = "Error retrieving CPU name";
        }
        return cpuName;
    }

    public static int getLinuxPhysicalCores() throws IOException {
        int cores = getPhysicalCoresFromOshi();

        if (cores == 0) {
            cores = getLinuxPhysicalCoresFromProcCpuinfo();
            if (cores == 0) {
                cores = getLinuxPhysicalCoresFromLscpu();
            }
        }

        return cores;
    }

    private static int getPhysicalCoresFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            return processor.getPhysicalProcessorCount();
        } catch (Exception e) {
            return 0;
        }
    }

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

    private static int getLinuxPhysicalCoresFromLscpu() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "lscpu | grep 'Core(s) per socket:' | awk '{print $NF}'");
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String coresLine = reader.readLine();

            if (coresLine != null && coresLine.matches("\\d+")) {
                return Integer.parseInt(coresLine.trim());
            }
        }
        return 0;
    }
}
