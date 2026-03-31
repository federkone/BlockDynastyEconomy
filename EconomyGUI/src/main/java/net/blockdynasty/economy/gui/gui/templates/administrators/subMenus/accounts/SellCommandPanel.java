package net.blockdynasty.economy.gui.gui.templates.administrators.subMenus.accounts;

import net.blockdynasty.economy.core.aplication.useCase.currency.SearchCurrencyUseCase;
import net.blockdynasty.economy.core.aplication.useCase.transaction.interfaces.IWithdrawUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.entities.currency.ICurrency;
import net.blockdynasty.economy.core.domain.events.Context;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.ITextInput;
import net.blockdynasty.economy.gui.gui.components.generics.CurrencySelectorAndAmount;
import java.math.BigDecimal;

public class SellCommandPanel extends CurrencySelectorAndAmount {
    private final IWithdrawUseCase withdrawUseCase;
    private final Player targetPlayer;
    private final PlatformGUI platformAdapter;
    private final ITextInput textInput;

    public SellCommandPanel(IEntityGUI player, Player targetPlayer,
                            SearchCurrencyUseCase searchCurrencyUseCase, IWithdrawUseCase withdrawUseCase, IGUI parentGUI, ITextInput textInput, PlatformGUI platformAdapter) {
        super(player, searchCurrencyUseCase, parentGUI,textInput);
        this.platformAdapter = platformAdapter;
        this.targetPlayer = targetPlayer;
        this.withdrawUseCase = withdrawUseCase;
        this.textInput = textInput;
    }


    @Override
    public String execute(IEntityGUI sender, ICurrency currency, BigDecimal amount) {
        textInput.asInputChat().open(this,sender,"Command input","give "+targetPlayer.getNickname()+" <itemname> [amount]" ,
                s-> giveCommandToPlayer(sender,currency,amount,s)
        );
        return null;
    }

    private String giveCommandToPlayer(IEntityGUI sender, ICurrency currency,BigDecimal amount, String command){
        if(command.isEmpty()){
            return "Command cannot be empty.";
        }

        IPlayer playerEntity = platformAdapter.getPlayer(targetPlayer.getNickname());
        if(playerEntity == null){
            return "Player " + targetPlayer.getNickname() + " is not online. Cannot execute command.";
        }

        Result<Void> result = withdrawUseCase.execute(targetPlayer, currency.getSingular(), amount, Context.SYSTEM);
        if (!result.isSuccess()) {
            return "Failed to withdraw currency to player, reason: " + result.getErrorMessage();
        }

        try {
            platformAdapter.dispatchCommand(command);
        }catch (Exception e){
            return "Failed to execute command for player: " + targetPlayer.getNickname();
        }
        return "Command sold to player " + targetPlayer.getNickname()+" for a value of "+ amount +" "+ currency.getSingular()+".";
    }
}

