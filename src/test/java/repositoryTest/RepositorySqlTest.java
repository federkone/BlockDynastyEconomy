package repositoryTest;

import me.BlockDynasty.Economy.Infrastructure.repository.RepositorySql;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import mockClass.repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositorySqlTest {
  /*  IRepository repository = new RepositorySql(new MockConnectionHibernateH2());


    @Test
    public void testLoadCurrencies() {
        // Test loading currencies
        var currencies = repository.loadCurrencies(null);
        assertEquals(0, currencies.size(), "Initial currency list should be empty");
    }

    @Test
    public void testCreateCurrency() {
        // Test creating a currency
        var currency = new Currency(UUID.randomUUID(), "TC", "TESTS");
        repository.saveCurrency(currency);

        var currencies = repository.loadCurrencies(null);
        Currency foundCurrency = currencies.stream()
                .filter(c -> c.getSingular().equals("TC"))
                .findFirst()
                .orElse(null);
        assertEquals("TC", foundCurrency.getSingular());
    }

    @Test
    public void testDeleteCurrency() {
        // Test deleting a currency
        var currency = new Currency(UUID.randomUUID(), "TC", "TESTS");
        repository.saveCurrency(currency);

        repository.deleteCurrency(currency);
        var currencies = repository.loadCurrencies(null);
        assertEquals(0, currencies.size(), "Currencies should be empty");
    }*/
}
