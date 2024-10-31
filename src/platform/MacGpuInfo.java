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
                if (line.contains("Chipset Model:")) {
                    gpuName = line.split(":")[1].trim(); // Extracts the GPU name
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
                if (line.contains("VRAM")) {
                    String vramStr = line.split(":")[1].trim().replaceAll("[^0-9]", ""); // Extracts VRAM in MB
                    vram = Long.parseLong(vramStr);
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            vram = 0; // If the command fails
        }
        return vram; // VRAM in MB
    }
}
