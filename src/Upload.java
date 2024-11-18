import platform.LinuxGpuInfo;
import platform.MacGpuInfo;
import platform.WindowsGpuInfo;

import javax.swing.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Upload {

    // Main method called by the menu
    public void uploadSpecs() {
        // 1. Ask for the URL of the site (where PHP will receive the data)
        String siteUrl = JOptionPane.showInputDialog(null, "Enter the site URL to send data (e.g., http://localhost/receiver.php):", "Site URL", JOptionPane.QUESTION_MESSAGE);

        if (siteUrl == null || siteUrl.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Site URL is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Collect system information via Specs.java
        String osInfo = Specs.getOperatingSystem();
        String cpuInfo = Specs.getCpuInfo();
        String gpuInfo = Specs.getGpuInfo();
        String ramInfo = Specs.getRamInfo();

        // 3. Parse and format the system information for the new format
        String os = parseOperatingSystem(osInfo);
        String version = parseVersion(osInfo);
        String cpu = parseCpuName(cpuInfo);
        String cores = parseCpuCores(cpuInfo);
        String threads = parseCpuThreads(cpuInfo);
        String gpu = parseGpuName(gpuInfo);
        String vram = getVram(); // Use the new getVram method
        String ram = parseRamTotal(ramInfo);

        // 4. Create a JSON object with the cleaned system information
        String jsonData = String.format(
                "{\"os\": \"%s\", \"version\": \"%s\", \"cpu\": \"%s\", \"cores\": \"%s\", \"threads\": \"%s\", \"gpu\": \"%s\", \"vram\": \"%s\", \"ram\": \"%s\"}",
                os, version, cpu, cores, threads, gpu, vram, ram
        );

        // 5. Send the data to the site
        System.out.println("Sending data: " + jsonData);  // For debugging, print the data being sent
        if (sendDataToSite(siteUrl, jsonData)) {
            JOptionPane.showMessageDialog(null, "Data sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to send data. Please check the site URL.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to send data to a website
    private boolean sendDataToSite(String siteUrl, String data) {
        HttpURLConnection connection = null;

        try {
            // Create a URL and open a connection
            URL url = new URL(siteUrl);
            connection = (HttpURLConnection) url.openConnection();

            // Configure the connection
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Allows sending data in the body of the request
            connection.setRequestProperty("Content-Type", "application/json");

            // Write the data to the request body
            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }

            // Check the server's response
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Method to get GPU VRAM
    private String getVram() {
        String osName = System.getProperty("os.name").toLowerCase();
        long gpuVram = 0;

        try {
            if (osName.contains("mac")) {
                gpuVram = MacGpuInfo.getGpuVram();
            } else if (osName.contains("linux")) {
                gpuVram = LinuxGpuInfo.getGpuVram();
            } else if (osName.contains("win")) {
                gpuVram = WindowsGpuInfo.getGpuVram();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return VRAM as a string; if GPU VRAM is 0, assume it's unavailable
        return gpuVram > 0 ? gpuVram + "MB" : "N/A";
    }

    // Helper methods to parse the system information into the required format
    private String parseOperatingSystem(String osInfo) {
        return osInfo.split("\n")[0].split(":")[1].trim();
    }

    private String parseVersion(String osInfo) {
        return osInfo.split("\n")[1].split(":")[1].trim();
    }

    private String parseCpuName(String cpuInfo) {
        return cpuInfo.split("\n")[0].split(":")[1].trim();
    }

    private String parseCpuCores(String cpuInfo) {
        return cpuInfo.split("\n")[1].split(":")[1].trim();
    }

    private String parseCpuThreads(String cpuInfo) {
        return cpuInfo.split("\n")[2].split(":")[1].trim();
    }

    private String parseGpuName(String gpuInfo) {
        return gpuInfo.split("\n")[0].split(":")[1].trim();
    }

    private String parseRamTotal(String ramInfo) {
        return ramInfo.split("\n")[0].split(":")[1].trim().split(" ")[0];
    }
}
