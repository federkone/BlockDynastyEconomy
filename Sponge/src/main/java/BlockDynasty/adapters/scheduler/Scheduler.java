package BlockDynasty.adapters.scheduler;

import BlockDynasty.SpongePlugin;
import lib.scheduler.ContextualTask;
import lib.scheduler.IScheduler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.time.Duration;

public class Scheduler implements IScheduler {

    @Override
    public void runLater(long delay, ContextualTask contextualTask) {
        Duration delayDuration = Duration.ofMillis(delay * 50);

        Sponge.server().scheduler().submit(
                Task.builder()
                        .plugin(SpongePlugin.getPlugin())
                        .delay(delayDuration)
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }

    @Override
    public void runAsync(ContextualTask contextualTask) {
        Sponge.asyncScheduler().submit(
                Task.builder()
                        .plugin(SpongePlugin.getPlugin())
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }

    @Override
    public void run(ContextualTask contextualTask) {
        Sponge.server().scheduler().submit(
                Task.builder()
                        .plugin(SpongePlugin.getPlugin())
                        .execute(contextualTask.getRunnable())
                        .build()
        );
    }
}
