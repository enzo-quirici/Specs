//package/MacCpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MacCpuInfo {

    public static String getCpuName() {
        String cpuName = "Unknown CPU";
        try {
            Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            if (result != null && !result.trim().isEmpty()) {
                cpuName = result.trim(); // Name for Intel processors
            } else {
                cpuName = "Apple Silicon"; // Generic name for Apple Silicon
            }
        } catch (Exception e) {
            cpuName = "Error retrieving CPU name";
        }
        return cpuName;
    }

    public static int getMacPhysicalCores() {
        int cores = Runtime.getRuntime().availableProcessors(); // Fallback to logical processors
        try {
            Process process = Runtime.getRuntime().exec("sysctl -n hw.physicalcpu");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.matches("\\d+")) { // Check if the line contains a number
                cores = Integer.parseInt(line.trim()); // Parse the number of physical cores
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cores; // Return the number of physical cores
    }

    public static void main(String[] args) {
        System.out.println("CPU Name: " + getCpuName());
        System.out.println("Physical Cores: " + getMacPhysicalCores());
    }
}
