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

package net.blockdynasty.economy.gui.commands.templates.users.OfferSubCommand;

import net.blockdynasty.economy.core.aplication.useCase.offer.CancelOfferUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.gui.commands.PlatformCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;

import java.util.List;

public class DenyOfferCommand extends AbstractCommand {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final PlatformCommand platformAdapter;

    public DenyOfferCommand(CancelOfferUseCase cancelOfferUseCase, PlatformCommand platformAdapter) {
        super("deny","", List.of("player"));
        this.platformAdapter= platformAdapter;
        this.cancelOfferUseCase = cancelOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerNme = args[0];
        IPlayer playerFrom = platformAdapter.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage("player offline");
            return false;
        }

        Player player = new Player(playerFrom.getUniqueId(), playerFrom.getName());

        Result<Void> result =cancelOfferUseCase.execute(player);
        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
            return false;
        }
        return true;
    }
}
