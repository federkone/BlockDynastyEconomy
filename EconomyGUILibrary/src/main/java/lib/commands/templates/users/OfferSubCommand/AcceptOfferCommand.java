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

package lib.commands.templates.users.OfferSubCommand;

import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.util.List;

public class AcceptOfferCommand extends AbstractCommand {
    private final AcceptOfferUseCase acceptOfferUseCase;

    public AcceptOfferCommand(AcceptOfferUseCase acceptOfferUseCase) {
        super("accept","", List.of("player"));
        this.acceptOfferUseCase = acceptOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerNme = args[0];
        IEntityCommands playerFrom = CommandsFactory.getPlatformAdapter().getPlayer(playerNme);

        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(" El jugador no está en línea.");
            return false;
        }

        //if(Message.getEnableDistanceLimitOffer()){
         //   double distance = Message.getDistanceLimitOffer();
          //  if(player.getLocation().distance(playerFrom.getLocation())>distance){
           //     sender.sendMessage(Message.getTooFar(distance));
            //    return false;
            //}
        //}

        Result<Void> result = acceptOfferUseCase.execute(sender.getUniqueId(), playerFrom.getUniqueId());


        if (result.isSuccess()) {
            // Mensaje para playerFrom (jugador que hizo la oferta)
            playerFrom.sendMessage(" ");
            //player.sendMessage("");
        } else {
            switch (result.getErrorCode()) {
                case OFFER_NOT_FOUND:
                    sender.sendMessage("");
                    break;
                case INSUFFICIENT_FUNDS:
                    sender.sendMessage("No tienes suficientes fondos para esta oferta");
                    playerFrom.sendMessage("El jugador no tiene suficiente dinero para la oferta");
                    break;
                case DATA_BASE_ERROR:
                    sender.sendMessage("");
                    playerFrom.sendMessage("");
                    break;
                default:
                    sender.sendMessage("unexpected error");
                    break;
            }
           return false;
        }
        return true;
    }
}
