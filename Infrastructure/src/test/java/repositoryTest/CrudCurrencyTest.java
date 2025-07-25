package repositoryTest;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.entities.ICurrencyRepository;
import BlockDynasty.repository.CurrencyRepository;
import repositoryTest.ConnectionHandler.MockConnectionHibernateH2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrudCurrencyTest {
        ICurrencyRepository currencyRepository = new CurrencyRepository(new MockConnectionHibernateH2().getSession());
        @BeforeEach
        public void setUp() {
        }

        @Test
        public void testCreate() {
            Currency currency = new Currency(UUID.randomUUID(),"dinero","dinero");
            this.currencyRepository.create(currency);
        }

        @Test
        public void testFindAll() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dineros");
            Currency currency2 = new Currency(UUID.randomUUID(), "coin", "coins");

            currencyRepository.create(currency);
            currencyRepository.create(currency2);
            List<Currency> currencies = currencyRepository.findAll();
            assertEquals(2, currencies.size(), "Should find two currencies");
        }
        @Test
        public void testFindByName() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dineros");

            currencyRepository.create(currency);
            Currency currencyDb = currencyRepository.findByName(currency.getSingular());
            assertEquals("dinero", currencyDb.getSingular());

        }
        @Test
        public void testFindByUuid() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");

            currencyRepository.create(currency);
            Currency currencyDb = currencyRepository.findByUuid(currency.getUuid().toString());

            assertEquals("dinero", currencyDb.getSingular());

        }
        @Test
        public void testSave() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero"); //dos monedas iguales cargadas en memoria = error

            currencyRepository.create(currency);
            currency.setSingular("dinerito");
            currencyRepository.save(currency);

            Currency currencyDb = currencyRepository.findByUuid(currency.getUuid().toString());

            assertEquals("dinerito", currencyDb.getSingular());
        }
        @Test
        public void testDelete() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");

           currencyRepository.create(currency);
            currencyRepository.delete(currency);

            assertThrows(CurrencyNotFoundException.class, () -> currencyRepository.findByName(currency.getSingular()));
        }
        @Test
        public void testUpdate() {
            // Create the currency within a transaction
            Currency currency = new Currency(UUID.randomUUID(), "dinero", "dinero");
            currencyRepository.create(currency);
            currency.setSingular("dinerito");
            currencyRepository.update(currency);

            Currency currencyFind =currencyRepository.findByName(currency.getSingular());

            assertEquals(currencyFind.getSingular(), currency.getSingular());
        }

}
