package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxGpuInfo {
    public static String getGpuName() {
        String gpuName = "Unknown GPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("lspci");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("vga")) {
                    gpuName = line.split(":")[2].trim(); // Extracts the GPU name
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
            // Use glxinfo or check lspci to get video memory on certain distributions
            ProcessBuilder processBuilder = new ProcessBuilder("glxinfo", "|", "grep", "Video memory");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("video memory")) {
                    // Extracting video memory (in MB)
                    vram = Long.parseLong(line.replaceAll("[^0-9]", "").trim());
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            vram = 0; // If the command fails or is not available
        }
        return vram; // VRAM in MB
    }
}
