package platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BsdRamInfo {

    // Method to get total physical memory in MB
    public static long getRamSize() {
        long totalMemory = 0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sysctl", "-n", "hw.physmem");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                totalMemory = Long.parseLong(line.trim()) / (1024 * 1024); // Bytes -> MB
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return totalMemory;
    }

    // Method to get free physical memory in MB
    public static long getRamFree() {
        long freeMemory = 0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sysctl", "-n", "vm.stats.vm.v_free_count");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                long freePages = Long.parseLong(line.trim());
                // page size
                ProcessBuilder pbPage = new ProcessBuilder("sysctl", "-n", "hw.pagesize");
                Process pageProcess = pbPage.start();
                BufferedReader pageReader = new BufferedReader(new InputStreamReader(pageProcess.getInputStream()));
                long pageSize = Long.parseLong(pageReader.readLine().trim());
                freeMemory = (freePages * pageSize) / (1024 * 1024); // Bytes -> MB
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return freeMemory;
    }

    // Method to get used physical memory in MB
    public static long getRamUsed() {
        long total = getRamSize();
        long free = getRamFree();
        if (total >= 0 && free >= 0) {
            return total - free;
        } else {
            return -1;
        }
    }
}
