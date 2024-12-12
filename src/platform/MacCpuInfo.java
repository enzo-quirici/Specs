// package/MacCpuInfo.java

package platform;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class MacCpuInfo {

    public static String getCpuName() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        // Get the CPU name from the processor
        return processor.getProcessorIdentifier().getName();
    }

    public static int getMacPhysicalCores() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        // Get the number of physical cores
        return processor.getPhysicalProcessorCount();
    }

    public static int getLogicalCores() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        // Get the number of logical cores
        return processor.getLogicalProcessorCount();
    }
}
