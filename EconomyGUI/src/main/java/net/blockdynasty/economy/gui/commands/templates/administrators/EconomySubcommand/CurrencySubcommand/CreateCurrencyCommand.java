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

package net.blockdynasty.economy.gui.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import net.blockdynasty.economy.core.aplication.useCase.currency.CreateCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import net.blockdynasty.economy.core.domain.persistence.Exceptions.TransactionException;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;

import java.util.List;

public class CreateCurrencyCommand extends AbstractCommand {
    private final CreateCurrencyUseCase createCurrencyUseCase;

    public CreateCurrencyCommand(CreateCurrencyUseCase createCurrencyUseCase) {
        super("create", "",List.of("singular", "plural"));
        this.createCurrencyUseCase = createCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String single = args[0];
        String plural = args[1];

        try {
            createCurrencyUseCase.execute(single, plural);
            sender.sendMessage("Created currency: " + single);
        } catch (CurrencyAlreadyExist e) {
            sender.sendMessage("Currency Already Exist.");
        } catch (TransactionException e) {
            sender.sendMessage("An error occurred while creating the currency.");
        }

        return true;
    }
}
