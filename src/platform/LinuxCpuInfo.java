//platform/LinuxCpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LinuxCpuInfo {
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
}
