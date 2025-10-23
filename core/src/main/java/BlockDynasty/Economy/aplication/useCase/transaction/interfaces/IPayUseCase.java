package BlockDynasty.Economy.aplication.useCase.transaction.interfaces;

import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPayUseCase {
    Result<Void> execute(UUID userFrom, UUID targetUUID,String currencyName, BigDecimal amount);
    Result<Void> execute(String userFrom,String targetName, String currencyName, BigDecimal amount);
}
