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

import net.blockdynasty.economy.core.aplication.useCase.currency.DeleteCurrencyUseCase;
import net.blockdynasty.economy.core.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import net.blockdynasty.economy.core.domain.persistence.Exceptions.TransactionException;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;

import java.util.List;

public class DeleteCurrencyCommand extends AbstractCommand {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public DeleteCurrencyCommand( DeleteCurrencyUseCase deleteCurrencyUseCase) {
        super("delete", "",List.of("currency"));
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;

    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String currencyName = args[0];

        try {
            deleteCurrencyUseCase.deleteCurrency(currencyName);
            sender.sendMessage( "Deleted currency: " + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage(e.getMessage()+" ensure you has another currency setted as default.");
        } catch (TransactionException e) {
            sender.sendMessage("Error while deleting currency: " + e.getMessage());
        }

        return false;
    }
}
