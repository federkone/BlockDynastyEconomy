package lib.commands.templates.users.OfferSubCommand;

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.util.List;

public class CancelOfferCommand extends AbstractCommand {

    private final CancelOfferUseCase cancelOfferUseCase;
    public CancelOfferCommand(CancelOfferUseCase cancelOfferUseCase ) {
        super("cancel", "", List.of("player"));
        this.cancelOfferUseCase = cancelOfferUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerNme = args[0];
        IEntityCommands playerFrom = CommandsFactory.getPlatformAdapter().getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage("");
            return false;
        }

        Result<Void> result =cancelOfferUseCase.execute(playerFrom.getUniqueId());
        if(result.isSuccess()){
            //playerFrom.sendMessage("La oferta de "+sender.getName()+" ha sido cancelada");
            //playerFrom.sendMessage(messageService.getOfferCancelMessage(sender.getName()));
            //sender.sendMessage("La oferta para "+playerFrom.getName()+" ha sido cancelada");
            //sender.sendMessage(messageService.getOfferCancelToMessage(playerFrom.getName()));
        }else{
            switch (result.getErrorCode()){
                case OFFER_NOT_FOUND:
                    sender.sendMessage(" offers not found");
                    break;
                default:
                    sender.sendMessage("error inesperado");
                    //playerFrom.sendMessage();
                    break;
            }
        }
        return false;
    }
}
