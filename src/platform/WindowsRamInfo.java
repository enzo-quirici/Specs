package platform;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class WindowsRamInfo {

    // Method to get total physical memory in MB
    public static long getRamSize() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = (long) Math.ceil(osMXBean.getTotalMemorySize() / (1024.0 * 1024.0));
        return totalPhysicalMemory;
    }

    // Method to get used physical memory in MB
    public static long getRamUsed() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemory = (long) Math.ceil(osMXBean.getTotalMemorySize() / (1024.0 * 1024.0));
        long freePhysicalMemory = (long) Math.ceil(osMXBean.getFreeMemorySize() / (1024.0 * 1024.0));
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
        return usedPhysicalMemory;
    }

    // Method to get free physical memory in MB
    public static long getRamFree() {
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long freePhysicalMemory = (long) Math.ceil(osMXBean.getFreeMemorySize() / (1024.0 * 1024.0));
        return freePhysicalMemory;
    }
}
