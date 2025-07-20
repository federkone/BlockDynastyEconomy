package repositoryTest;

import me.BlockDynasty.Economy.Infrastructure.repositoryV2.AccountRepository;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.CurrencyRepository;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IAccountRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrudAccountTest {
   IAccountRepository accountRepository = new AccountRepository(new MockConnectionHibernateH2().getSession());
   CurrencyRepository currencyRepository = new CurrencyRepository(new MockConnectionHibernateH2().getSession());

    @Test
    public void testCreateAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        Currency currency2 = new Currency(UUID.randomUUID(),"coin","coin");
        currencyRepository.create(currency2);
       currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        account.setBalance(currency, BigDecimal.valueOf(1000));
        account.setBalance(currency2, BigDecimal.valueOf(1000));
        accountRepository.create(account);

        account.add(currency, BigDecimal.valueOf(1000));
        accountRepository.update(account);

        Account foundAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals(account.getUuid(), foundAccount.getUuid(), "The account UUID should match");
        assertEquals(BigDecimal.valueOf(2000).setScale(2), foundAccount.getBalance(currency).getAmount(), "The account balance should match");

    }

    @Test
    public void testUpdateAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        accountRepository.create(account);

        account.setNickname("updatedName");
        account.setBalance(currency, BigDecimal.valueOf(1000));
        accountRepository.save(account);

        Account updatedAccount = accountRepository.findByUuid(account.getUuid().toString());
        assertEquals("updatedName", updatedAccount.getNickname());
        assertEquals(BigDecimal.valueOf(1000).setScale(2), updatedAccount.getBalance(currency).getAmount());
    }

    @Test
    public  void testDeleteAccount() {
        Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
        currencyRepository.create(currency);

        Account account = new Account(UUID.randomUUID(), "nullplague");
        account.setBalance(currency, BigDecimal.valueOf(1000));
        accountRepository.create(account);

        accountRepository.delete(account);

        assertThrows(AccountNotFoundException.class ,()->{ accountRepository.findByUuid(account.getUuid().toString()); });
    }
}
