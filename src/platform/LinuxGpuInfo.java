// platform/LinuxGpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxGpuInfo {

    // Méthode pour obtenir le nom du GPU
    public static String getGpuName() {
        String gpuName = "Unknown GPU";
        try {
            // Essayer d'abord de récupérer le nom du GPU avec glxinfo
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "glxinfo | grep 'Device:'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("device")) {
                    gpuName = line.split(":")[1].trim(); // Extraction du nom du GPU
                    gpuName = removeUnwantedParenthesesContent(gpuName); // Supprimer le contenu indésirable
                    break;
                }
            }

            process.waitFor();
        } catch (Exception e) {
            // Si glxinfo échoue, nous n'avons pas besoin de changer gpuName
        }

        // Si glxinfo échoue, essayer lspci
        if (gpuName.equals("Unknown GPU")) { // Si on n'a pas encore trouvé le nom
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("lspci");
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("vga")) {
                        gpuName = line.split(":")[2].trim(); // Extraction du nom du GPU
                        gpuName = removeUnwantedParenthesesContent(gpuName); // Supprimer le contenu indésirable
                        break;
                    }
                }

                process.waitFor();
            } catch (Exception e) {
                gpuName = "Error retrieving GPU name"; // Gestion de l'erreur
            }
        }

        return gpuName;
    }

    // Méthode pour supprimer le contenu entre parenthèses sauf pour (R), (r), (TM), (tm), (C) ou (c)
    private static String removeUnwantedParenthesesContent(String name) {
        // Supprimer le contenu entre parenthèses sauf pour (R), (r), (TM), (tm), (C), (c) et d'autres mentions
        return name.replaceAll("\\s*\\((?![Rr]|TM|tm|C|c).*?\\)", ""); // Supprime les parenthèses sauf les mentions spécifiques
    }

    // Méthode pour obtenir la VRAM du GPU
    public static long getGpuVram() {
        long vram = 0;

        // Essayer d'abord d'obtenir la VRAM depuis glxinfo
        vram = getVramFromGlxInfo();
        if (vram > 0) return vram; // Retourner si la VRAM a été trouvée

        // Si non trouvée, essayer lspci
        vram = getVramFromLspci();
        return vram; // Retourne 0 si non trouvée
    }

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
            // La commande a échoué ou est indisponible
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
