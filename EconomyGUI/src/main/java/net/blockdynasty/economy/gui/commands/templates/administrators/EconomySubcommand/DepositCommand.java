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

package net.blockdynasty.economy.gui.commands.templates.administrators.EconomySubcommand;

import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IDepositUseCase;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.libs.services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class DepositCommand extends AbstractCommand {
    private final IDepositUseCase deposit;

    public DepositCommand(IDepositUseCase deposit) {
        super("give", "",List.of("player", "amount", "currency"));
        this.deposit = deposit;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return false;
        }

        double finalMount = amount;

        Result<Void> result =deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount), Context.COMMAND);

        if (!result.isSuccess()) {
            sender.sendMessage("Deposit failed "+  result.getErrorMessage()+" "+ result.getErrorCode());
        }else {
            sender.sendMessage("Deposit successful");
        }

        return true;
    }

}
