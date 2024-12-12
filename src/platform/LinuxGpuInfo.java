package platform;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class LinuxGpuInfo {

    // Méthode pour obtenir le nom du GPU avec OSHI
    public static String getGpuName() {
        String gpuName = getGpuNameFromOshi();  // D'abord essayer avec OSHI

        if (gpuName.equals("Unknown GPU")) {  // Si OSHI échoue, utiliser glxinfo et lspci
            gpuName = getGpuNameFromGlxInfo();
            if (gpuName.equals("Unknown GPU")) {
                gpuName = getGpuNameFromLspci();
            }
        }

        return gpuName;
    }

    // Méthode pour obtenir le nom du GPU avec OSHI
    private static String getGpuNameFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hal = systemInfo.getHardware();
            List<GraphicsCard> graphicsCards = hal.getGraphicsCards();

            if (!graphicsCards.isEmpty()) {
                GraphicsCard gpu = graphicsCards.get(0);
                return removeUnwantedParenthesesContent(gpu.getName());  // Retirer le contenu indésirable entre parenthèses
            }
        } catch (Exception e) {
            // Si l'accès à OSHI échoue, renvoyer "Unknown GPU"
        }
        return "Unknown GPU";
    }

    // Méthode pour obtenir le nom du GPU avec glxinfo
    private static String getGpuNameFromGlxInfo() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "glxinfo | grep 'Device:'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("device")) {
                    return removeUnwantedParenthesesContent(line.split(":")[1].trim());
                }
            }
        } catch (Exception e) {
            // Si glxinfo échoue, passer à lspci
        }
        return "Unknown GPU";
    }

    // Méthode pour obtenir le nom du GPU avec lspci
    private static String getGpuNameFromLspci() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("lspci");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("vga")) {
                    return removeUnwantedParenthesesContent(line.split(":")[2].trim());
                }
            }
        } catch (Exception e) {
            // Si lspci échoue, renvoyer "Unknown GPU"
        }
        return "Unknown GPU";
    }

    // Méthode pour obtenir la VRAM du GPU avec OSHI
    public static long getGpuVram() {
        long vram = getVramFromOshi();  // D'abord essayer avec OSHI

        if (vram == 0) {  // Si OSHI échoue, utiliser glxinfo et lspci
            vram = getVramFromGlxInfo();
            if (vram == 0) {
                vram = getVramFromLspci();
            }
        }

        return vram;
    }

    // Méthode pour obtenir la VRAM du GPU avec OSHI
    private static long getVramFromOshi() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hal = systemInfo.getHardware();
            List<GraphicsCard> graphicsCards = hal.getGraphicsCards();

            if (!graphicsCards.isEmpty()) {
                GraphicsCard gpu = graphicsCards.get(0);
                return gpu.getVRam() / (1024 * 1024); // Convertir en Mo
            }
        } catch (Exception e) {
            // Si l'accès à OSHI échoue, retourner 0
        }
        return 0;
    }

    // Méthode pour obtenir la VRAM du GPU avec glxinfo
    private static long getVramFromGlxInfo() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("glxinfo");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("total available memory")) { // Pour certains systèmes
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM en Mo
                } else if (line.toLowerCase().contains("video memory")) { // Pour d'autres systèmes
                    return Long.parseLong(line.replaceAll("[^0-9]", "").trim()); // VRAM en Mo
                }
            }
        } catch (Exception e) {
            // La commande a échoué ou est indisponible
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
                        if (part.matches("[0-9a-f]+K")) { // Un exemple simple
                            return Long.parseLong(part.replace("K", "").trim()) / 1024; // Convertir en Mo
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            // Si lspci échoue, retourner 0
        }
        return 0;
    }

    private static String removeUnwantedParenthesesContent(String name) {
        return name.replaceAll("\\s*\\(.*?\\)", "");
    }

    public static void main(String[] args) {
        String gpuName = getGpuName();
        long vram = getGpuVram();
        System.out.println("GPU Name: " + gpuName);
        System.out.println("GPU VRAM: " + vram + " MB");
    }
}
