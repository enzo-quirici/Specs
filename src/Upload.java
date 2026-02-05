import javax.swing.*;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Upload {

    public void uploadSpecs() {
        // ===== 1. OWNER NAME =====
        String owner = JOptionPane.showInputDialog(
                null,
                "Enter the owner's name:",
                "Owner",
                JOptionPane.QUESTION_MESSAGE
        );
        if (owner == null || owner.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Owner name is required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ===== 2. DEVICE FRIENDLY NAME =====
        String denomination = JOptionPane.showInputDialog(
                null,
                "Enter a friendly name for the device\n(ex: Gaming PC, Pro Laptop, Windows Shitbox, etc.):",
                "Friendly Name",
                JOptionPane.QUESTION_MESSAGE
        );
        // If empty or cancelled → default value
        if (denomination == null || denomination.trim().isEmpty()) {
            denomination = "Unnamed";
        }

        // ===== DESTINATION CHOICE =====
        String[] options = {"Send to a URL", "Save to a JSON file"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Where do you want to send / save the data?",
                "Upload Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == -1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Operation cancelled.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // ===== GET SYSTEM SPECIFICATIONS =====
        String osName     = Specs.getOperatingSystemName();
        String osVersion  = Specs.getOperatingSystemVersion();
        String cpuName    = Specs.getCpuName();
        String cpuCores   = String.valueOf(Specs.getCpuCores());
        String cpuThreads = String.valueOf(Specs.getCpuThreads());
        String gpuName    = Specs.getGpuName();
        String gpuVram    = Specs.getGpuVram(); // "N/A" if unavailable
        String ramTotal   = String.valueOf(Specs.getRamSize());

        // ===== BUILD JSON =====
        String jsonData = String.format(
                "{"
                        + "\"os\": \"%s\", "
                        + "\"version\": \"%s\", "
                        + "\"cpu\": \"%s\", "
                        + "\"cores\": \"%s\", "
                        + "\"threads\": \"%s\", "
                        + "\"gpu\": \"%s\", "
                        + "\"vram\": \"%s\", "
                        + "\"ram\": \"%s\", "
                        + "\"owner\": \"%s\", "
                        + "\"denomination\": \"%s\", "
                        + "\"device\": \"desktop\""
                        + "}",
                osName,
                osVersion,
                cpuName,
                cpuCores,
                cpuThreads,
                gpuName,
                gpuVram,
                ramTotal,
                owner,
                denomination
        );

        // ===== SEND OR SAVE =====
        if (choice == 0) {
            // Send to URL
            String siteUrl = JOptionPane.showInputDialog(
                    null,
                    "Enter the destination URL (ex: http://localhost/receiver.php):",
                    "Server URL",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (siteUrl == null || siteUrl.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "URL is required!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            System.out.println("Sending data: " + jsonData);

            if (sendDataToSite(siteUrl, jsonData)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Data sent successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to send data.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            // Local save
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save JSON file");

            // Use the friendly name in the suggested filename (sanitize special characters)
            String safeName = denomination.replaceAll("[^a-zA-Z0-9]", "_");
            fileChooser.setSelectedFile(
                    new java.io.File("Specs_" + safeName + ".json")
            );

            int selection = fileChooser.showSaveDialog(null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile())) {
                    fileWriter.write(jsonData);
                    JOptionPane.showMessageDialog(
                            null,
                            "File saved successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error while saving file: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
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
                os.write(data.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
