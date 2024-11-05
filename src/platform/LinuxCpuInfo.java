// platform/LinuxCpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxCpuInfo {

    /**
     * Returns the name of the CPU by reading the /proc/cpuinfo file.
     */
    public static String getCpuName() {
        String cpuName = "Unknown CPU";
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("model name")) {
                    cpuName = line.split(":")[1].trim(); // Extracts the CPU name
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
     *
     * @return Number of physical CPU cores.
     * @throws IOException If an error occurs during the execution.
     */
    public static int getLinuxPhysicalCores() throws IOException {
        int coresPerSocket = getLinuxCoresPerSocket();  // Number of cores per socket
        int sockets = getLinuxSockets();                // Number of sockets

        if (coresPerSocket > 0 && sockets > 0) {
            return coresPerSocket * sockets;  // Calculate total number of physical cores
        }
        return Runtime.getRuntime().availableProcessors(); // Fallback
    }

    /**
     * Returns the number of cores per socket by using lscpu command.
     *
     * @return Cores per socket.
     * @throws IOException If an error occurs during the execution.
     */
    private static int getLinuxCoresPerSocket() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                "LANG=C lscpu | grep 'Core(s) per socket:' | awk '{print $NF}'"
        );
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String coresPerSocketLine = reader.readLine();

            if (coresPerSocketLine != null && coresPerSocketLine.matches("\\d+")) {
                return Integer.parseInt(coresPerSocketLine.trim());
            }
        }
        return 0;  // Return 0 on failure
    }

    /**
     * Returns the number of sockets by using lscpu command.
     *
     * @return Number of sockets.
     * @throws IOException If an error occurs during the execution.
     */
    private static int getLinuxSockets() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                "LANG=C lscpu | grep 'Socket(s):' | awk '{print $NF}'"
        );
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String socketsLine = reader.readLine();

            if (socketsLine != null && socketsLine.matches("\\d+")) {
                return Integer.parseInt(socketsLine.trim());
            }
        }
        return 0;  // Return 0 on failure
    }
}
