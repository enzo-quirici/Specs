//platform/WindowsGpuInfo.java

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

        // Try getting VRAM from glxinfo first
        vram = getVramFromGlxInfo();
        if (vram > 0) return vram; // Return if VRAM found

        // If not found, try lspci
        vram = getVramFromLspci();
        return vram; // Return 0 if not found
    }

    private static long getVramFromGlxInfo() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("glxinfo");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("total available memory")) { // For some systems
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM in MB
                } else if (line.toLowerCase().contains("video memory")) { // For other systems
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM in MB
                }
            }
        } catch (Exception e) {
            // Command failed or unavailable
        }
        return 0;
    }

    private static long getVramFromLspci() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("lspci", "-v");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean foundVGA = false;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("vga compatible controller")) {
                    foundVGA = true;
                }
                if (foundVGA && line.toLowerCase().contains("memory at")) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.matches("[0-9a-f]+K")) { // A simple example
                            return Long.parseLong(part.replace("K", "").trim()) / 1024; // Convert to MB
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            // Command failed or unavailable
        }
        return 0;
    }

    public static void main(String[] args) {
        String gpuName = getGpuName();
        long vram = getGpuVram();
        System.out.println("GPU Name: " + gpuName);
        System.out.println("GPU VRAM: " + vram + " MB");
    }
}
