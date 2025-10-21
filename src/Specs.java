import java.io.IOException;
import java.util.Locale;
import platform.*;

public class Specs {

    public static String getOperatingSystemName() {
        String osName = System.getProperty("os.name");
        osName = osName.replace("-", " ");
        return osName;
    }

    public static String getOperatingSystemVersion() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osVersion = System.getProperty("os.version");

        if (osName.contains("linux")) {
            osVersion = LinuxOSInfo.getLinuxOSVersion();
        } else if (osName.contains("bsd")) {
            osVersion = BsdOSInfo.getBsdOSVersion();
        }

        osVersion = osVersion.replace("-", " ");
        return osVersion;
    }

    public static String getCpuName() {
        String cpuName = "";
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            cpuName = MacCpuInfo.getCpuName();
        } else if (osName.contains("linux")) {
            cpuName = LinuxCpuInfo.getCpuName();
        } else if (osName.contains("win")) {
            cpuName = WindowsCpuInfo.getCpuName();
        } else if (osName.contains("bsd")) {
            cpuName = BsdCpuInfo.getCpuName();
        } else {
            cpuName = "Unknown CPU";
        }

        cpuName = cpuName
                .replaceAll("\\(.*?\\)", "")
                .replace("-", " ")
                .replaceAll("@?\\s*\\d+(\\.\\d+)?\\s*ghz", "")
                .replaceAll("(?i)\\b\\d{1,2}(st|nd|rd|th)\\s+gen\\b", "")
                .replaceAll("\\s+", " ")
                .trim();

        return cpuName;
    }

    public static int getCpuCores() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        try {
            if (osName.contains("win")) {
                return WindowsCpuInfo.getWindowsPhysicalCores();
            } else if (osName.contains("mac")) {
                return MacCpuInfo.getMacPhysicalCores();
            } else if (osName.contains("linux")) {
                return LinuxCpuInfo.getLinuxPhysicalCores();
            } else if (osName.contains("bsd")) {
                return BsdCpuInfo.getBsdPhysicalCores();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getCpuThreads() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String getGpuName() {
        String gpuName = "";
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            gpuName = MacGpuInfo.getGpuName();
        } else if (osName.contains("linux")) {
            gpuName = LinuxGpuInfo.getGpuName();
        } else if (osName.contains("win")) {
            gpuName = WindowsGpuInfo.getGpuName();
        } else if (osName.contains("bsd")) {
            gpuName = BsdGpuInfo.getGpuName();
        } else {
            gpuName = "Unknown GPU";
        }

        if (!osName.contains("linux")) {
            gpuName = gpuName.replaceAll("\\(.*?\\)", "").trim();
        }

        return gpuName;
    }

    public static String getGpuVram() {
        long gpuVram = 0;
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            gpuVram = MacGpuInfo.getGpuVram();
        } else if (osName.contains("linux")) {
            gpuVram = LinuxGpuInfo.getGpuVram();
        } else if (osName.contains("win")) {
            gpuVram = WindowsGpuInfo.getGpuVram();
        } else if (osName.contains("bsd")) {
            gpuVram = BsdGpuInfo.getGpuVram();
        } else {
            gpuVram = -1;
        }

        return String.valueOf(gpuVram);
    }

    public static long getRamSize() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            return LinuxRamInfo.getRamSize();
        } else if (osName.contains("mac")) {
            return MacRamInfo.getRamSize();
        } else if (osName.contains("win")) {
            return WindowsRamInfo.getRamSize();
        } else if (osName.contains("bsd")) {
            return BsdRamInfo.getRamSize();
        } else {
            return -1;
        }
    }

    public static long getRamUsed() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            return LinuxRamInfo.getRamUsed();
        } else if (osName.contains("mac")) {
            return MacRamInfo.getRamUsed();
        } else if (osName.contains("win")) {
            return WindowsRamInfo.getRamUsed();
        } else if (osName.contains("bsd")) {
            return BsdRamInfo.getRamUsed();
        } else {
            return -1;
        }
    }

    public static long getRamFree() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            return LinuxRamInfo.getRamFree();
        } else if (osName.contains("mac")) {
            return MacRamInfo.getRamFree();
        } else if (osName.contains("win")) {
            return WindowsRamInfo.getRamFree();
        } else if (osName.contains("bsd")) {
            return BsdRamInfo.getRamFree();
        } else {
            return -1;
        }
    }
}
