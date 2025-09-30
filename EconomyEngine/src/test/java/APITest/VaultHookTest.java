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

package APITest;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VaultHookTest {
   /* @Test
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
*/
}