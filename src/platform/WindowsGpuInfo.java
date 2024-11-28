//platform/WindowsGpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WindowsGpuInfo {
    public static String getGpuName() {
        String gpuName = "Unknown GPU";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("wmic", "path", "win32_VideoController", "get", "name");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Ignore the first line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    gpuName = line; // Extracts the GPU name
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
            ProcessBuilder processBuilder = new ProcessBuilder("wmic", "path", "win32_VideoController", "get", "AdapterRAM");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Ignore the first line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        long currentVram = Long.parseLong(line);
                        vram = Math.max(vram, currentVram);
                    } catch (NumberFormatException e) {
                        System.err.println("Error when converting VRAM" + line);
                    }
                }
            }

            process.waitFor();
        } catch (Exception e) {
            System.err.println("Error retrieving VRAM Size :" + e.getMessage());
        }

        return vram / (1024 * 1024); // Conversion en MB
    }
}