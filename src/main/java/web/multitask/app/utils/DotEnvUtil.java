package web.multitask.app.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DotEnvUtil {

    static String detectedOS;

    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    public static String getOperatingSystemType() {
        return detectedOS != null ? detectedOS :
                (detectedOS = getOSFromSystemProperty());
    }

    private static String getOSFromSystemProperty() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (OS.contains("mac") || OS.contains("darwin")) return "MacOS";
        if (OS.contains("win")) return "Windows";
        if (OS.contains("nux")) return "Linux";
        return "Other";
    }

    private static final Map<String, String> OS_PATH_MAP = new HashMap<>();
    
    static {
        OS_PATH_MAP.put("MacOS", "/opt/dotenv/");
        OS_PATH_MAP.put("Linux", "/opt/dotenv/");
        OS_PATH_MAP.put("Windows", "abcdefghijklmnopqrstuvwxyz");
    }

    public static String getDotEnvPath(String projectName) {
        String detectedOs = DotEnvUtil.getOperatingSystemType();

        return OS_PATH_MAP.getOrDefault(detectedOs, "")
            + (detectedOs.equals("Windows")
                ? getWindowsPath(projectName)
                : projectName);
    }

    private static String getWindowsPath(String projectName) {
        return OS_PATH_MAP.get("Windows")
            .chars()
            .mapToObj(letter -> "/" + (char) letter + ":/dotenv/" + projectName)
            .filter(path -> new File(path).exists())
            .findFirst()
            .orElse("");
    }

}
