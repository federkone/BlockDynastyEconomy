/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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
package com.blockdynasty.velocity;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandReload  {

    public static BrigadierCommand createBrigadierCommand(final Velocity plugin) {
        LiteralCommandNode<CommandSource> ecoNode = LiteralArgumentBuilder
                .<CommandSource>literal("bd")
                .then(
                        LiteralArgumentBuilder.<CommandSource>literal("reload")
                                .requires(source -> source.hasPermission("blockdynastyeconomy.reload"))
                                .executes(context -> {
                                    plugin.updateConf();
                                    context.getSource().sendMessage(Component.text("Configuration reloaded.", NamedTextColor.GREEN));
                                    return 1;
                                })
                )
                .executes(context -> {
                    context.getSource().sendMessage(Component.text("Usage: /bd reload", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        return new BrigadierCommand(ecoNode);
    }
}
