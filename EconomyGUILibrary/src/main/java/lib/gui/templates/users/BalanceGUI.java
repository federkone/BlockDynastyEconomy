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

package lib.gui.templates.users;

import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.PaginatedGUI;
import lib.util.colors.ChatColor;
import lib.util.colors.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalanceGUI extends PaginatedGUI<Money> {
    private final GetBalanceUseCase getBalanceUseCase;
    private final UUID targetUUID;

    public BalanceGUI(IEntityGUI player, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Account balance", 3, player, parent, 7); // 7 currencies per page
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = player.getUniqueId();

        loadBalances();
    }

    public BalanceGUI(IEntityGUI sender, UUID target, GetBalanceUseCase getBalanceUseCase, IGUI parent) {
        super("Account balance", 3, sender, parent, 7);
        this.getBalanceUseCase = getBalanceUseCase;
        this.targetUUID = target;

        loadBalances();
    }

    private void loadBalances() {
        Result<List<Money>> result = getBalanceUseCase.getBalances(targetUUID);

        //TEST
        //List<Money> monies = new ArrayList<>();
        //for (int i=0; i < 45; i++) {
        //    monies.add(new Money(new Currency(UUID.randomUUID(),"test","test")));
        //}

        if (result.isSuccess() && result.getValue() != null) {
            showItemsPage(result.getValue());

        }else {showItemsPage( new ArrayList<>());}
    }

    @Override
    protected IItemStack createItemFor(Money money) {
        Currency currency = money.getCurrency();
        return createItem(Materials.GOLD_INGOT,
                ChatColor.stringValueOf(currency.getColor()) + currency.getSingular(),
                ChatColor.stringValueOf(Colors.YELLOW)+"Balance: " + ChatColor.stringValueOf(currency.getColor()) + money.format());
    }

    @Override
    protected IItemStack createEmptyMessage() {
        return createItem(Materials.BARRIER, ChatColor.stringValueOf(Colors.RED)+"No Currencies", ChatColor.stringValueOf(Colors.WHITE)+"There are no coins in the account");
    }

    @Override
    protected void addCustomButtons() {
        setItem(4, createItem(Materials.BOOK, ChatColor.stringValueOf(Colors.GOLD)+"Account balance", ChatColor.stringValueOf(Colors.WHITE)+"Available balances"), null);
    }
}