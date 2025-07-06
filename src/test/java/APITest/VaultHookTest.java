package APITest;

import me.BlockDynasty.Economy.aplication.useCase.UsesCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Integrations.vault.VaultHook;
import me.BlockDynasty.Economy.domain.result.Result;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VaultHookTest {
    @Test
    void testGetBalance() {
        // Mock dependencies
        UsesCase usesCase = mock(UsesCase.class);
        GetBalanceUseCase getBalanceUseCase = mock(GetBalanceUseCase.class);
        when(usesCase.getGetBalanceUseCase()).thenReturn(getBalanceUseCase);

        // Mock balance result
        Balance balance = mock(Balance.class);
        when(balance.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(getBalanceUseCase.getBalance("player")).thenReturn(Result.success(balance));

        // Create VaultHook
        VaultHook vaultHook = new VaultHook(usesCase);

        // Test getBalance
        double result = vaultHook.getBalance("player");
        assertEquals(100, result);
    }

    @Test
    void testGetBalanceWithOfflinePlayer() {
        // Mock dependencies
        UsesCase usesCase = mock(UsesCase.class);
        GetBalanceUseCase getBalanceUseCase = mock(GetBalanceUseCase.class);
        when(usesCase.getGetBalanceUseCase()).thenReturn(getBalanceUseCase);

        // Mock balance result
        Balance balance = mock(Balance.class);
        when(balance.getBalance()).thenReturn(BigDecimal.valueOf(200));
        when(getBalanceUseCase.getBalance("playerName")).thenReturn(Result.success(balance));

        // Mock OfflinePlayer
        OfflinePlayer offlinePlayer = mock(OfflinePlayer.class);
        when(offlinePlayer.getName()).thenReturn("playerName");

        // Create VaultHook
        VaultHook vaultHook = new VaultHook(usesCase);

        // Test getBalance with OfflinePlayer
        double result = vaultHook.getBalance(offlinePlayer);
        assertEquals(200, result);
    }

}