package lib.templates.administrators.subMenus.accounts;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.EditAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.result.Result;
import lib.GUIFactory;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import lib.components.Materials;
import lib.templates.abstractions.AbstractGUI;

import java.util.UUID;

public class EditAccountGUI extends AbstractGUI {
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final EditAccountUseCase editAccountUseCase;
    private final SearchAccountUseCase searchAccountUseCase;
    private final ITextInput textInput;

    public EditAccountGUI(
            DeleteAccountUseCase deleteAccountUseCase,
            EditAccountUseCase editAccountUseCase,
            SearchAccountUseCase searchAccountUseCase,
            IPlayer sender, Player target, IGUI parent, ITextInput textInput) {
        super("Edit Account: "+target.getNickname(), 5,sender,parent);
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.editAccountUseCase = editAccountUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
        this.textInput = textInput;

        buttons(sender,target);
    }

    private void buttons(IPlayer sender, Player target) {
        setItem(15,createItem(Materials.REDSTONE,"Delete account","The player's account is deleted,before a confirmation"),
                f -> {
                    textInput.open(this,sender, "Delete: "+target.getNickname(), "Confirm yes/no", s ->{
                        if(s.equals("yes")){
                            Result<Void> result = deleteAccountUseCase.execute(target.getNickname());
                            if(result.isSuccess()){
                                //sender.sendMessage("The player has been eliminated "+ target.getNickname());
                                //org.bukkit.entity.Player p = Bukkit.getPlayer(target.getNickname());
                                //if(p != null){
                                //    p.kickPlayer("Your economy account has been deleted.");
                                //}
                                return null;
                            }else{
                                sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                                return null;
                            }
                        }else {
                            this.open();
                            return null;
                        }
                    });
                });

        setItem(40,createItem(Materials.BARRIER, "Back","go back"),f->{
            GUIFactory.accountPanel(sender,this.getParent().getParent()).open();});

        setItem(29,createItem(Materials.PAPER,"Deposit Currency","Deposit currency into the player's account"),
                f -> {
                    GUIFactory.depositPanel(sender , target, this).open();
                });

        setItem(31,createItem(Materials.PAPER,"Set balance currency","Set the balance of a currency in the player's account"),
                f -> {
                    GUIFactory.setPanel(sender , target, this).open();
                });

        setItem(33,createItem(Materials.PAPER,"Withdraw Currency","Withdraw currency from the player's account"),
                f -> {
                    GUIFactory.withdrawPanel(sender , target, this).open();
                });

        setItem(11,createItem(Materials.BOOK, "See Balance","See the balance of the player's account"),
                f -> {
                    GUIFactory.balancePanel( sender, UUID.fromString(target.getUuid()), this).open();
                });

        boolean isBlocked = searchAccountUseCase.getAccount(UUID.fromString(target.getUuid())).getValue().isBlocked();
        if (isBlocked) {
            setItem(13, createItem(Materials.RED_CONCRETE, "Account is blocked", "Click to unblock transactions","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"),
                    f -> {
                        Result<Void>result= editAccountUseCase.unblockAccount(UUID.fromString(target.getUuid()));
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        } else {
            setItem(13, createItem(Materials.LIME_CONCRETE, "Account is enabled", "Click to block transactions Account","This affects:","Withdraw","Deposit","Transfer", "Pay","Trade","Exchange","All economy op"),
                    f -> {
                        Result<Void>result= editAccountUseCase.blockAccount(UUID.fromString(target.getUuid()));
                        if (result.isSuccess()){
                            GUIFactory.editAccountPanel( sender, target, this.getParent()).open();
                        }
                        else {
                            sender.sendMessage(result.getErrorMessage()+" "+ result.getErrorCode());
                        }
                    });
        }
    }
}