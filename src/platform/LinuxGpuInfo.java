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

        // Try getting VRAM from multiple sources
        vram = getVramFromNvidiaSmi();
        if (vram > 0) return vram; // Return if VRAM found

        vram = getVramFromGlxInfo();
        if (vram > 0) return vram; // Return if VRAM found

        vram = getVramFromLspci();
        return vram; // Return 0 if not found
    }

    private static long getVramFromNvidiaSmi() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("nvidia-smi", "--query-gpu=memory.total", "--format=csv,noheader");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM en MB
            }
        } catch (Exception e) {
            // Commande échouée ou non disponible
        }
        return 0;
    }

    private static long getVramFromGlxInfo() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("glxinfo");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("total available memory")) { // Pour certains systèmes
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM en MB
                } else if (line.toLowerCase().contains("video memory")) { // Pour d'autres systèmes
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM en MB
                }
            }
        } catch (Exception e) {
            // Commande échouée ou non disponible
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
                    // Ici, vous devrez analyser la ligne pour obtenir la VRAM
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.matches("[0-9a-f]+K")) { // Un exemple simple
                            return Long.parseLong(part.replace("K", "").trim()) / 1024; // Convertir en MB
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            // Commande échouée ou non disponible
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
