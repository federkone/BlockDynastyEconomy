package APITest;

import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.BukkitImplementation.Integrations.vault.VaultHook;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VaultHookTest {
    @Test
    void testGetBalance() {
        // Mock dependencies
        UsesCaseFactory usesCaseFactory = mock(UsesCaseFactory.class);
        GetBalanceUseCase getBalanceUseCase = mock(GetBalanceUseCase.class);
        when(usesCaseFactory.getGetBalanceUseCase()).thenReturn(getBalanceUseCase);

        // Mock balance result
        Money money = mock(Money.class);
        when(money.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(getBalanceUseCase.getBalance("player")).thenReturn(Result.success(money));

        // Create VaultHook
        VaultHook vaultHook = new VaultHook(usesCaseFactory);

        // Test getBalance
        double result = vaultHook.getBalance("player");
        assertEquals(100, result);
    }

    @Test
    void testGetBalanceWithOfflinePlayer() {
        // Mock dependencies
        UsesCaseFactory usesCaseFactory = mock(UsesCaseFactory.class);
        GetBalanceUseCase getBalanceUseCase = mock(GetBalanceUseCase.class);
        when(usesCaseFactory.getGetBalanceUseCase()).thenReturn(getBalanceUseCase);

        // Mock balance result
        Money money = mock(Money.class);
        when(money.getAmount()).thenReturn(BigDecimal.valueOf(200));
        when(getBalanceUseCase.getBalance("playerName")).thenReturn(Result.success(money));

        // Mock OfflinePlayer
        OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
        when(offlinePlayer.getName()).thenReturn("playerName");

        // Create VaultHook
        VaultHook vaultHook = new VaultHook(usesCaseFactory);

        // Test getBalance with OfflinePlayer
        double result = vaultHook.getBalance(offlinePlayer);
        assertEquals(200, result);
    }

}