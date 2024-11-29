import java.io.IOException;
import java.util.Locale;
import platform.*;

public class Specs {

    // Method to get the operating system information
    public static String getOperatingSystem() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");

        // If the OS is Linux, try to retrieve the name and version via /etc/os-release
        if (osName.toLowerCase().equals("linux")) {
            osVersion = LinuxOSInfo.getLinuxOSVersion();
        }

        // Replace dashes with spaces in both osName and osVersion
        osName = osName.replace("-", " ");
        osVersion = osVersion.replace("-", " ");

        // Return the OS information (name and version)
        return "Operating System : " + osName + "\nVersion : " + osVersion;
    }

    // Method to get CPU information
    public static String getCpuInfo() {
        String cpuName = "";
        String osName = System.getProperty("os.name").toLowerCase();

        // Determine the CPU name based on the OS
        if (osName.contains("mac")) {
            cpuName = MacCpuInfo.getCpuName();
        } else if (osName.contains("linux")) {
            cpuName = LinuxCpuInfo.getCpuName();
        } else if (osName.contains("win")) {
            cpuName = WindowsCpuInfo.getCpuName();
        } else {
            cpuName = "Unknown CPU";
        }

        // Remove parentheses (and their content) from the CPU name
        cpuName = cpuName.replaceAll("\\(.*?\\)", "").trim();

        // Get the number of physical cores based on the OS
        int physicalCores = getPhysicalCores();
        int logicalCores = Runtime.getRuntime().availableProcessors(); // Logical cores (threads)

        return "CPU : " + cpuName + "\nPhysical Cores : " + physicalCores + "\nLogical Cores : " + logicalCores;
    }

    // Method to get the number of physical cores based on the OS
    public static int getPhysicalCores() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        try {
            if (osName.contains("win")) {
                return WindowsCpuInfo.getWindowsPhysicalCores();
            } else if (osName.contains("mac")) {
                return MacCpuInfo.getMacPhysicalCores(); // Utilisation de MacCpuInfo pour macOS
            } else if (osName.contains("linux")) {
                return LinuxCpuInfo.getLinuxPhysicalCores();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Runtime.getRuntime().availableProcessors(); // Fallback to logical processors if an error occurs
    }

    // Method to get GPU information
    public static String getGpuInfo() {
        String gpuName = "";
        long gpuVram = 0;
        String osName = System.getProperty("os.name").toLowerCase();

        // Determine the GPU name and VRAM based on the OS
        if (osName.contains("mac")) {
            gpuName = MacGpuInfo.getGpuName();
            gpuVram = MacGpuInfo.getGpuVram();
        } else if (osName.contains("linux")) {
            gpuName = LinuxGpuInfo.getGpuName();
            gpuVram = LinuxGpuInfo.getGpuVram();
        } else if (osName.contains("win")) {
            gpuName = WindowsGpuInfo.getGpuName();
            gpuVram = WindowsGpuInfo.getGpuVram();
        } else {
            gpuName = "Unknown GPU";
        }

        // Remove parentheses (and their content) from the GPU name, except on Linux
        if (!osName.contains("linux")) {
            gpuName = gpuName.replaceAll("\\(.*?\\)", "").trim();
        }

        return "GPU : " + gpuName + "\nVRAM : " + gpuVram + " MB"; // Return GPU information
    }


    // Method to get RAM information (delegated to OS-specific classes)
    public static String getRamInfo() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            return LinuxRamInfo.getRamInfo();
        } else if (osName.contains("mac")) {
            return MacRamInfo.getRamInfo();
        } else if (osName.contains("win")) {
            return WindowsRamInfo.getRamInfo();
        } else {
            return "OS not supported for RAM info retrieval.";
        }
    }
}