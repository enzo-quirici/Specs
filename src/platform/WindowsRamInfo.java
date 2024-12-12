// platform/WindowsRamInfo.java

package platform;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class WindowsRamInfo {
    public static String getRamInfo() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Total physical memory in MB
        long totalPhysicalMemory = (long) Math.ceil(osMXBean.getTotalMemorySize() / (1024.0 * 1024.0));

        // Free physical memory in MB
        long freePhysicalMemory = (long) Math.ceil(osMXBean.getFreeMemorySize() / (1024.0 * 1024.0));

        // Used physical memory in MB
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

        return "RAM (Total) : " + totalPhysicalMemory + " MB\n" +
                "RAM (Used) : " + usedPhysicalMemory + " MB\n" +
                "RAM (Free) : " + freePhysicalMemory + " MB";
    }
}
