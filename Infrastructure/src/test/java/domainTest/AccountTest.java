package domainTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
/*
    1 Gema = 30,000 Oro
    1 Gema = 300,000 Plata
    1 Gema = 1,500,000 Bronce

    1 Plata = 5 Bronce
    1 Plata =

    1 Oro = 10 Plata
    1 Oro = 50 Bronce
*/
public class AccountTest {
    /*private Account account;
    private Currency plata;
    private Currency gema;
    private Currency bronce;
    private Currency oro;
    private Currency coin;
    private Currency dinero;

    @BeforeEach
    public void setUp() {
        account = new Account(UUID.randomUUID(),"Nullplague");
        gema = new Currency(UUID.randomUUID(),"Gema","Gemas");
        gema.setExchangeRate(1);

        plata = new Currency(UUID.randomUUID(),"Plata","Plata");
        plata.setExchangeRate(300000);

        bronce = new Currency(UUID.randomUUID(),"Bronce","Bronce");
        bronce.setExchangeRate(1500000);

        oro = new Currency(UUID.randomUUID(),"Oro","Oro");
        oro.setExchangeRate(30000);

        dinero= new Currency(UUID.randomUUID(),"Dinero","Dinero");
        dinero.setExchangeRate(30000);

        coin = new Currency(UUID.randomUUID(),"Coin","Coin");
        coin.setDecimalSupported(false);
        coin.setExchangeRate(1);
    }


    @Test
    void testCoinToDineroExchange(){
        account.createBalance(dinero, BigDecimal.valueOf(0));
        account.createBalance(coin,BigDecimal.valueOf(20));

        Result<BigDecimal> result = Transaction.exchange(account,coin,dinero,BigDecimal.valueOf(500000));

        System.out.println(result.getErrorMessage()+result.getErrorCode()+result.isSuccess()+result.getValue());
        assertEquals(0,account.getBalance(dinero).getBalance().doubleValue());
        assertEquals(20, account.getBalance(coin).getBalance().doubleValue());
    }


    @Test
    void testPlataToGemExchange(){
        account.createBalance(plata, BigDecimal.valueOf(300000));
        account.createBalance(gema,BigDecimal.valueOf(0));

        Result<BigDecimal> result = Transaction.exchange(account,plata,gema,BigDecimal.valueOf(1));

        assertEquals(0,account.getBalance(plata).getBalance().doubleValue());
        assertEquals(1, account.getBalance(gema).getBalance().doubleValue());
    }

    @Test
    void testBronceToGem(){
        account.createBalance(bronce,BigDecimal.valueOf(1500000));
        account.createBalance(gema,BigDecimal.valueOf(0));

        Result<BigDecimal> result =  Transaction.exchange(account,bronce,gema,BigDecimal.valueOf(1));

        assertEquals(0,account.getBalance(bronce).getBalance().doubleValue());
        assertEquals(1, account.getBalance(gema).getBalance().doubleValue());
    }

    @Test
    void testOroToGem(){
        account.createBalance(oro,BigDecimal.valueOf(30000));
        account.createBalance(gema,BigDecimal.valueOf(0));

        Result<BigDecimal> result = Transaction.exchange(account,oro,gema,BigDecimal.valueOf(1));

        assertEquals(0,account.getBalance(oro).getBalance().doubleValue());
        assertEquals(1, account.getBalance(gema).getBalance().doubleValue());
    }

    @Test
    void testPlataToBronceExchange() { //0.2 plata para poder comprar 1 bronce
        account.createBalance(plata, BigDecimal.valueOf(0.2)); // 0.2 Plata
        account.createBalance(bronce, BigDecimal.valueOf(0));

        // Intercambiamos plata para conseguir bronce
        Result<BigDecimal> result = Transaction.exchange(account,plata, bronce, BigDecimal.valueOf(1)); //quiero 1 bronce usando plata

        assertEquals(1, account.getBalance(bronce).getBalance().doubleValue()); //consulto si conseguí 1 de bronce
        assertEquals(0, account.getBalance(plata).getBalance().doubleValue()); //consulto si se uso la cantidad de plata correspondiente

    }

    @Test
    void testBroncetoPLtaExchange() {// 5 de bronce para poder comprar 1 plata
        account.createBalance(bronce, BigDecimal.valueOf(5)); //5 bronce
        account.createBalance(plata, BigDecimal.valueOf(0));

        // Intercambiamos bronce para conseguir plata
        Result<BigDecimal> result = Transaction.exchange(account,bronce, plata, BigDecimal.valueOf(1)); //quiero 1 plata usando bronce

        //assertEquals(false, result.isSuccess()); //falla por que no le alcanza el bronce para conseguir la plata
        //System.out.println(result.getErrorMessage());

        assertEquals(1, account.getBalance(plata).getBalance().doubleValue());
        assertEquals(0, account.getBalance(bronce).getBalance().doubleValue());

    }
    */
}
