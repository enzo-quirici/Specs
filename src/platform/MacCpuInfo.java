//platform/MacCpuInfo.java

package platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MacCpuInfo {
    public static String getCpuName() {
        String cpuName = "Unknown CPU";
        try {
            Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            cpuName = reader.readLine().trim(); // Read and trim the line
        } catch (Exception e) {
            cpuName = "Error retrieving CPU name"; // In case of error
        }
        return cpuName;
    }
}
