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

package BlockDynasty.BukkitImplementation.scheduler;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.BukkitImplementation.utils.Version;
import BlockDynasty.FoliaImplementation.scheduler.SchedulerFolia;
import abstractions.platform.scheduler.ContextualTask;
import abstractions.platform.scheduler.IScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public class SchedulerFactory {
    private static IScheduler schedulerInstance;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        SchedulerFactory.plugin = plugin;
    }

    public static IScheduler getScheduler() {
        if (schedulerInstance != null) {
            return schedulerInstance;
        }

        if(Version.hasFoliaScheduler()) {
            schedulerInstance = SchedulerFolia.init(plugin);
            Console.log("Folia detected, applying Folia scheduler implementation.");
        }else{
            schedulerInstance = SchedulerBukkit.init(plugin);
            Console.log("Bukkit detected, applying Bukkit scheduler implementation.");
        }
        return schedulerInstance;
    }

    public static void run(ContextualTask contextualTask){
        getScheduler().run(contextualTask);
    }

    public static void runAsync(ContextualTask contextualTask) {
        getScheduler().runAsync(contextualTask);
    }
    public static void runLater(long delay, ContextualTask contextualTask) {
        getScheduler().runLater(delay, contextualTask);
    }
}