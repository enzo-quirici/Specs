//Specs.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Locale;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import platform.*;

public class Specs {

    // Method to get the operating system information
    public static String getOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osVersion = System.getProperty("os.version");
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
                return getWindowsPhysicalCores();
            } else if (osName.contains("mac")) {
                return getMacPhysicalCores();
            } else if (osName.contains("linux")) {
                return getLinuxPhysicalCores();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Runtime.getRuntime().availableProcessors(); // Fallback to logical processors if an error occurs
    }

    // Method to get physical cores on Windows
    private static int getWindowsPhysicalCores() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "wmic cpu get NumberOfCores");
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int cores = 0;

        // Read the output and parse the number of cores
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Remove any surrounding spaces
            if (line.matches("\\d+")) { // If the line contains only digits, it's a valid number
                cores = Integer.parseInt(line.trim()); // Parse the number
                break; // Exit the loop after finding the first valid number
            }
        }

        if (cores > 0) {
            return cores; // Return the number of cores if found
        } else {
            return Runtime.getRuntime().availableProcessors(); // Fallback to logical processors
        }
    }
    // Méthode pour obtenir le nombre de cœurs physiques sur Linux
    private static int getLinuxPhysicalCores() throws IOException {
        int coresPerSocket = getLinuxCoresPerSocket();  // Nombre de cœurs par socket
        int sockets = getLinuxSockets();                // Nombre de sockets

        if (coresPerSocket > 0 && sockets > 0) {
            return coresPerSocket * sockets;  // Calcul du nombre total de cœurs physiques
        }
        return Runtime.getRuntime().availableProcessors(); // Fallback
    }

    // Méthode pour obtenir le nombre de cœurs par socket sur Linux
    private static int getLinuxCoresPerSocket() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                "LANG=C lscpu | grep 'Core(s) per socket:' | awk '{print $NF}'"
        );
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String coresPerSocketLine = reader.readLine();

        if (coresPerSocketLine != null && coresPerSocketLine.matches("\\d+")) {
            return Integer.parseInt(coresPerSocketLine.trim());
        }
        return 0;  // Retourne 0 en cas d'échec
    }

    // Méthode pour obtenir le nombre de sockets sur Linux
    private static int getLinuxSockets() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                "LANG=C lscpu | grep 'Socket(s):' | awk '{print $NF}'"
        );
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String socketsLine = reader.readLine();

        if (socketsLine != null && socketsLine.matches("\\d+")) {
            return Integer.parseInt(socketsLine.trim());
        }
        return 0;  // Retourne 0 en cas d'échec
    }
    // Method to get physical cores on Mac
    private static int getMacPhysicalCores() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sysctl", "-n", "hw.physicalcpu");
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null && line.matches("\\d+")) {
            return Integer.parseInt(line.trim()); // Return the number of cores if found
        }
        return Runtime.getRuntime().availableProcessors(); // Fallback to logical processors
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

        return "GPU : " + gpuName + "\nVRAM : " + gpuVram + " MB"; // Return GPU information
    }

    // Method to get RAM information (excluding cache and swap on macOS and Linux)
    public static String getRamInfo() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = osMXBean.getTotalMemorySize() / (1024 * 1024); // Total memory in MB
        long usedPhysicalMemory = 0; // Used physical memory
        long freePhysicalMemory = 0; // Free physical memory

        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("linux")) {
                // Read memory information directly from /proc/meminfo
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
            } else if (osName.contains("mac")) {
                // macOS: Use vm_stat to calculate RAM info, focusing on used pages excluding inactive/cached
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "vm_stat");
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                long pagesFree = 0;
                long pagesWired = 0;
                long pagesActive = 0;
                long pageSize = 4096; // Page size in bytes (typically 4 KB on macOS)

                while ((line = reader.readLine()) != null) {
                    if (line.contains("Pages free:")) {
                        pagesFree = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                    } else if (line.contains("Pages wired down:")) {
                        pagesWired = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                    } else if (line.contains("Pages active:")) {
                        pagesActive = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                    }
                }

                // Calculate used memory as active + wired pages only (no cache)
                usedPhysicalMemory = ((pagesWired + pagesActive) * pageSize) / (1024 * 1024); // Convert to MB
                freePhysicalMemory = totalPhysicalMemory - usedPhysicalMemory;

            } else {
                // Fallback for other OS (Windows)
                freePhysicalMemory = osMXBean.getFreeMemorySize() / (1024 * 1024); // Free memory in MB
                usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory; // Calculate used memory
            }

            // Calculate used memory
            if (usedPhysicalMemory == 0) {
                usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory; // Fallback
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving RAM information.";
        }

        return "RAM (Total): " + totalPhysicalMemory + " MB\n" +
                "RAM (Used): " + usedPhysicalMemory + " MB\n" +
                "RAM (Free): " + freePhysicalMemory + " MB"; // Return RAM information
    }
}
