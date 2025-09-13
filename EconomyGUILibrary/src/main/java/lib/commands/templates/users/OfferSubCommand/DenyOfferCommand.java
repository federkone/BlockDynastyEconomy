package lib.commands.templates.users.OfferSubCommand;

import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.util.List;

public class DenyOfferCommand extends AbstractCommand {
    private final CancelOfferUseCase cancelOfferUseCase;

    public DenyOfferCommand(CancelOfferUseCase cancelOfferUseCase) {
        super("deny","", List.of("player"));
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
            sender.sendMessage("player offline");
            return false;
        }

        Result<Void> result =cancelOfferUseCase.execute(playerFrom.getUniqueId());
        if (result.isSuccess()) {
            playerFrom.sendMessage("La oferta de "+sender.getName()+" ha sido rechazada");
            sender.sendMessage("La oferta para "+playerFrom.getName()+" ha sido rechazada");
        }else{
            switch (result.getErrorCode()){
                case OFFER_NOT_FOUND:
                    sender.sendMessage("");
                    break;
                default:
                    sender.sendMessage("error inesperado");
                    //playerFrom.sendMessage(messageService.getUnexpectedErrorMessage());
                    break;
            }
        }
        return true;
    }
}
