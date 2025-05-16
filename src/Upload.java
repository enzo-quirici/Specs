//Upload.java

import platform.LinuxGpuInfo;
import platform.MacGpuInfo;
import platform.WindowsGpuInfo;

import javax.swing.*;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Upload {

    public void uploadSpecs() {
        String[] options = {"Send to URL", "Save to JSON File"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose where to send data:",
                "Upload Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == -1) {
            JOptionPane.showMessageDialog(null, "Operation cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Retrieve system specs using methods (assumes Specs has appropriate methods)
        String osName = Specs.getOperatingSystemName();
        String osVersion = Specs.getOperatingSystemVersion();
        String cpuName = Specs.getCpuName();
        String cpuCores = String.valueOf(Specs.getCpuCores());
        String cpuThreads = String.valueOf(Specs.getCpuThreads());
        String gpuName = Specs.getGpuName();
        String gpuVram = Specs.getGpuVram(); // Return "N/A" if not available
        String ramTotal = String.valueOf(Specs.getRamSize()); // Ensure it's a numeric value in GB or MB

        // Format JSON
        String jsonData = String.format(
                "{\"os\": \"%s\", \"version\": \"%s\", \"cpu\": \"%s\", \"cores\": \"%s\", \"threads\": \"%s\", \"gpu\": \"%s\", \"vram\": \"%s\", \"ram\": \"%s\"}",
                osName, osVersion, cpuName, cpuCores, cpuThreads, gpuName, gpuVram, ramTotal
        );

        if (choice == 0) {
            // Send to URL
            String siteUrl = JOptionPane.showInputDialog(null,
                    "Enter the site URL to send data (e.g., http://localhost/receiver.php):",
                    "Site URL",
                    JOptionPane.QUESTION_MESSAGE);

            if (siteUrl == null || siteUrl.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Site URL is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Sending data: " + jsonData);  // For debugging

            if (sendDataToSite(siteUrl, jsonData)) {
                JOptionPane.showMessageDialog(null, "Data sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to send data.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Save to JSON file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save JSON File");
            fileChooser.setSelectedFile(new java.io.File("Specs.json"));

            int selection = fileChooser.showSaveDialog(null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile())) {
                    fileWriter.write(jsonData);
                    JOptionPane.showMessageDialog(null, "Data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean sendDataToSite(String siteUrl, String data) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(siteUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
