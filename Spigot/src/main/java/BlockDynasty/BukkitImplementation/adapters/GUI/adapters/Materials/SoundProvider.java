package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials;

import BlockDynasty.BukkitImplementation.utils.Version;
import org.bukkit.Sound;

public class SoundProvider {
    private static boolean isOldSoundSys = Version.match("1.8", "1.9", "1.10","1.11");
    private static Sound click;
    private static Sound pickUp;

    static {
        if (isOldSoundSys) {
            click = Sound.valueOf("CLICK");
            pickUp = Sound.valueOf("ORB_PICKUP");
        } else {
            click = Sound.UI_BUTTON_CLICK;
            pickUp = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
        }
    }

    public static Sound getClickSound() {
        return click;
    }

    public static Sound getPickupSound() {
       return pickUp;
    }
}
