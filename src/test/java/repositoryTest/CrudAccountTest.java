package repositoryTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.AccountRepository;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.CurrencyRepository;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;
import me.BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import mockClass.repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrudAccountTest {
  /*  IAccountRepository accountRepository = new AccountRepository(new MockConnectionHibernateH2().getSession());
    CurrencyRepository currencyRepository = new CurrencyRepository(new MockConnectionHibernateH2().getSession());

    @Test
    public void testCreateAccount() {
        Account account = new Account(UUID.randomUUID(), "nullplague");
        accountRepository.create(account);

        Account foundAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals(account.getUuid(), foundAccount.getUuid(), "The account UUID should match");
    }

    @Test
    public void testUpdateAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        account.add(currency, BigDecimal.valueOf(1000));
        accountRepository.create(account);


        account.setNickname("updatedName");
        //account.add(currency, BigDecimal.valueOf(1000));
        accountRepository.update(account);

        Account updatedAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals("updatedName", updatedAccount.getNickname());
        assertEquals(BigDecimal.valueOf(1000), updatedAccount.getBalance(currency).getAmount());
    }*/
}
