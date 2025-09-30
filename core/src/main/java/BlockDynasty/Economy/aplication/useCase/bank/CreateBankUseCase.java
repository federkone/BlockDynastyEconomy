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

package BlockDynasty.Economy.aplication.useCase.bank;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IBankService;

import java.util.List;

//crear un banco, basado en un nombre, un creador, y una lista de monedas.
public class CreateBankUseCase {
    private IBankService bankService;

    public CreateBankUseCase(IBankService bankService) {
        this.bankService = bankService;
    }

    public Result<Void> execute(String bankName, String creatorName, List<Money> monies) {
        if(bankService.bankExists(bankName)) {
            return Result.failure("Bank already exists", ErrorCode.BANK_ALREADY_EXISTS);

        }
        if (bankName == null || bankName.isEmpty()) {
            return Result.failure("Bank name cannot be empty", ErrorCode.BANK_NAME_CANNOT_BE_EMPTY);
        }

        if (creatorName == null || creatorName.isEmpty()) {
            return Result.failure("Creator name cannot be empty",ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (monies == null || monies.isEmpty()) {
            return Result.failure("Balances cannot be empty", ErrorCode.BANK_NOT_HAVE_CURRENCY);
        }

        // Logic to create the bank would go here



        return Result.success(null); // Return success result
    }
}
