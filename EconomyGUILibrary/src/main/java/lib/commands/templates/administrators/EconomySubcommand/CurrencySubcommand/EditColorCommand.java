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

package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyColorUnformat;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditColorCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditColorCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("color","BlockDynastyEconomy.command.currency", List.of("currency", "color"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage("§0§lBLACK §7= black");
            sender.sendMessage("§1§lDARK BLUE §7= dark_blue");
            sender.sendMessage("§2§lDARK GREEN §7= dark_green");
            sender.sendMessage("§3§lDARK AQUA §7= dark_aqua");
            sender.sendMessage("§4§lDARK RED §7= dark_red");
            sender.sendMessage("§5§lDARK PURPLE §7= dark_purple");
            sender.sendMessage("§6§lGOLD §7= gold");
            sender.sendMessage("§7§lGRAY §7= gray");
            sender.sendMessage("§8§lDARK GRAY §7= dark_gray");
            sender.sendMessage("§9§lBLUE §7= blue");
            sender.sendMessage("§a§lGREEN §7= green");
            sender.sendMessage("§b§lAQUA §7= aqua");
            sender.sendMessage("§c§lRED §7= red");
            sender.sendMessage("§d§lLIGHT PURPLE §7= light_purple");
            sender.sendMessage("§e§lYELLOW §7= yellow");
            sender.sendMessage("§f§lWHITE §7= white|reset");
            return false;
        }


        String currencyName = args[0];
        String colorString = args[1].toUpperCase();

            try {
                editCurrencyUseCase.editColor(currencyName, colorString);
                sender.sendMessage("§7Color for §f" + currencyName + " §7updated: " + colorString);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage("§cCurrency not found.");
            } catch (CurrencyColorUnformat e) {
                sender.sendMessage("§cInvalid chat color.");
            }

        return true;
    }
}
