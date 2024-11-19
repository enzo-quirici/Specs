//platform/MacGpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MacGpuInfo {

    public static String getGpuName() {
        String gpuName = "Unknown GPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("system_profiler", "SPDisplaysDataType");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean foundGpuSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Chipset Model:")) { // Intel and Apple Silicon
                    gpuName = line.split(":")[1].trim();
                    foundGpuSection = true;
                } else if (line.contains("Apple") && line.contains("Graphics")) { // Apple Silicon (Generic Apple GPU)
                    gpuName = "Apple Silicon";
                    foundGpuSection = true;
                }
                if (foundGpuSection && line.contains("VRAM")) {
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            gpuName = "Error retrieving GPU name";
        }
        return gpuName;
    }

    public static long getGpuVram() {
        long vram = 0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("system_profiler", "SPDisplaysDataType");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("VRAM")) { // For Intel or discrete GPUs
                    String vramStr = line.split(":")[1].trim().replaceAll("[^0-9]", ""); // Extracts VRAM in MB
                    vram = Long.parseLong(vramStr);
                    break;
                } else if (line.contains("Apple") && line.contains("Graphics")) { // Apple Silicon, no explicit VRAM
                    vram = 0; // VRAM is shared, set a default or 0 if it can't be calculated
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            vram = 0; // If the command fails
        }
        return vram; // VRAM in MB or 0 if shared (Apple Silicon)
    }

    public static void main(String[] args) {
        System.out.println("GPU Name: " + getGpuName());
        System.out.println("VRAM (MB): " + getGpuVram());
    }
}
