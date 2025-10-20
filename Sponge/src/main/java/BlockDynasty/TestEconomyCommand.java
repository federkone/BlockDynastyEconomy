package BlockDynasty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

import java.math.BigDecimal;

public class TestEconomyCommand {

    public static void registerTestCommand(RegisterCommandEvent<Command.Parameterized> event, PluginContainer container) {
    // Register a test command for economy
    Command.Parameterized ecoTestCommand = Command.builder()
            .permission("blockdynasty.ecotester")
            .executor(context -> {
                if (context.cause().root() instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) context.cause().root();
                    // Get the economy service
                    Sponge.server().serviceProvider().economyService().ifPresent(economyService -> {
                        // Test account existence
                        var account = economyService.findOrCreateAccount(player.uniqueId());
                        if (account.isPresent()) {
                            player.sendMessage(Component.text("---------------------------------").color(NamedTextColor.GOLD));
                            player.sendMessage(Component.text("Account found: " + account.get().balance(economyService.defaultCurrency())));

                            // Test deposit
                            var result = account.get().deposit(
                                    economyService.defaultCurrency(),
                                    BigDecimal.valueOf(100),
                                    Cause.of(EventContext.empty(), container)
                            );
                            player.sendMessage(Component.text("Deposit result: " + result.result().name()));

                            // Show new balance
                            player.sendMessage(Component.text("New balance: " + account.get().balance(economyService.defaultCurrency())));

                            var resultWithdraw = account.get().withdraw(
                                    economyService.defaultCurrency(),
                                    BigDecimal.valueOf(50),
                                    Cause.of(EventContext.empty(), container)
                            );
                            player.sendMessage(Component.text("Withdraw result: " + resultWithdraw.result().name()));
                            player.sendMessage(Component.text("New balance: " + account.get().balance(economyService.defaultCurrency())));

                            var accountCris = economyService.findOrCreateAccount("Cris");

                            var resultTransfer = account.get().transfer(
                                    accountCris.get(),
                                    economyService.defaultCurrency(),
                                    BigDecimal.valueOf(1),
                                    Cause.of(EventContext.empty(), container)
                            );

                            player.sendMessage(Component.text("Transfer result: " + resultTransfer.result().name()));
                            player.sendMessage(Component.text("Your new balance: " + account.get().balance(economyService.defaultCurrency())));
                            player.sendMessage(Component.text("Cris's new balance: " + accountCris.get().balance(economyService.defaultCurrency())));
                            player.sendMessage(Component.text("---------------------------------").color(NamedTextColor.GOLD));

                        } else {
                            player.sendMessage(Component.text("Failed to find or create account!").color(NamedTextColor.RED));
                        }
                    });
                    return CommandResult.success();
                }
                return CommandResult.error(Component.text("Only players can use this command"));
            })
            .build();

        event.register(container, ecoTestCommand, "ecotest");
    }
}
