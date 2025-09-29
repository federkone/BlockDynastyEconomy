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
