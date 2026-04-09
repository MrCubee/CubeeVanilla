package fr.mrcubee.vanilla.nms;

import org.bukkit.Bukkit;

public class NMS {
    
    public static String getNMSVersion() {
        final String mcVersion = Bukkit.getServer().getBukkitVersion();

        Bukkit.getLogger().info("Version: " + mcVersion);
        switch (mcVersion) {
            case "26.1.1-R0.1-SNAPSHOT":
            case "26.1-R0.1-SNAPSHOT":
                return "v26_1_R1";
            case "1.21.11":
                return  Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        return null;
    }

}
