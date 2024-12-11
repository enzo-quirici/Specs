//platform/WindowsGpuInfo.java

package platform;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;

public class WindowsGpuInfo {
    public static String getGpuName() {
        SystemInfo si = new SystemInfo();
        String gpuName = "Unknown GPU";

        for (GraphicsCard gpu : si.getHardware().getGraphicsCards()) {
            gpuName = gpu.getName();
            break;
        }

        return gpuName;
    }

    public static long getGpuVram() {
        SystemInfo si = new SystemInfo();
        long vram = 0;

        for (GraphicsCard gpu : si.getHardware().getGraphicsCards()) {
            vram = Math.max(vram, gpu.getVRam());
        }

        return vram / (1024 * 1024);
    }
}
