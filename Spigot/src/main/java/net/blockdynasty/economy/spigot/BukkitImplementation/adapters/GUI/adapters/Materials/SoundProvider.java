/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.Materials;

import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Version;
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
