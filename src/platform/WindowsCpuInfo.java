// platform/WindowsCpuInfo

package platform;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class WindowsCpuInfo {

    public static String getCpuName() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        return processor.getProcessorIdentifier().getName();
    }

    public static int getWindowsPhysicalCores() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        return processor.getPhysicalProcessorCount();
    }

    public static int getLogicalCores() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();

        return processor.getLogicalProcessorCount();
    }
}
