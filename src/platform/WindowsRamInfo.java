// platform/WindowsRamInfo.java

package platform;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class WindowsRamInfo {
    public static String getRamInfo() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = osMXBean.getTotalMemorySize() / (1024 * 1024); // Total memory in MB
        long freePhysicalMemory = osMXBean.getFreeMemorySize() / (1024 * 1024); // Free memory in MB
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory; // Used memory in MB

        return "RAM (Total) : " + totalPhysicalMemory + " MB\n" +
                "RAM (Used) : " + usedPhysicalMemory + " MB\n" +
                "RAM (Free) : " + freePhysicalMemory + " MB";
    }
}
