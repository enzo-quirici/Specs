//platform/MacGpuInfo.java

package platform;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

public class MacGpuInfo {

    public static String getGpuName() {
        String gpuName = "Unknown GPU";
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            List<GraphicsCard> graphicsCards = hardware.getGraphicsCards();

            if (!graphicsCards.isEmpty()) {
                GraphicsCard gpu = graphicsCards.get(0);
                gpuName = gpu.getName();
            }
        } catch (Exception e) {
            gpuName = "Error retrieving GPU name";
        }
        return gpuName;
    }

    public static long getGpuVram() {
        long vram = 0;
        try {
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            List<GraphicsCard> graphicsCards = hardware.getGraphicsCards();

            if (!graphicsCards.isEmpty()) {
                GraphicsCard gpu = graphicsCards.get(0);
                vram = gpu.getVRam();
            }
        } catch (Exception e) {
            vram = 0;
        }
        return vram / (1024 * 1024);
    }
}
