package listenerTest;

import net.blockdynasty.economy.core.aplication.services.AccountService;
import net.blockdynasty.economy.core.aplication.services.CurrencyService;
import net.blockdynasty.economy.core.aplication.useCase.account.CreateAccountUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import net.blockdynasty.economy.core.aplication.useCase.account.getAccountUseCase.GetOfflineAccountsUseCase;
import net.blockdynasty.economy.core.domain.entities.account.Account;
import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.core.domain.result.Result;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.blockdynasty.economy.engine.platform.listeners.PlayerJoinListenerOffline;
import net.blockdynasty.economy.engine.platform.listeners.PlayerJoinListenerOnline;
import repositoryTest.FactoryRepo;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerJoinListenerTest {
    IRepository repository;
    GetAccountByNameUseCase getAccountByNameUseCase;
    GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    GetOfflineAccountsUseCase getOfflineAccountsUseCase;
    CreateAccountUseCase createAccountUseCase;
    AccountService accountService;
    CurrencyService currencyService;
    PlayerJoinListenerOffline playerJoinListenerOffline;
    PlayerJoinListenerOnline playerJoinListenerOnline;

    @BeforeEach
    void setUp() {
        repository = FactoryRepo.getDb();
        currencyService = new CurrencyService(repository);
        accountService = new AccountService(5 ,repository, currencyService);
        getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
        getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        getOfflineAccountsUseCase = new GetOfflineAccountsUseCase(accountService);
        createAccountUseCase = new CreateAccountUseCase(accountService, currencyService,repository);
        playerJoinListenerOffline = new PlayerJoinListenerOffline(getAccountByNameUseCase,createAccountUseCase,accountService);
        playerJoinListenerOnline = new PlayerJoinListenerOnline(getAccountByUUIDUseCase,createAccountUseCase,accountService);
    }

    @Test
    void testLoadAccountOffline(){
        Player playerExist = new Player(UUID.randomUUID(), "Nullplague");
        createAccountUseCase.execute(playerExist.getUuid(), playerExist.getNickname());

        Player playerNewUUID = new Player(UUID.randomUUID(), playerExist.getNickname());
        playerJoinListenerOffline.loadAccount(new MockPlayer(playerNewUUID));

        Result<List<Account>> result = getOfflineAccountsUseCase.execute();
        long count = result.getValue().stream().filter(acc -> acc.getPlayer().getNickname().equals(playerExist.getNickname())).count();
        assertEquals(1, count);

        Result<Account> resultAccount = getAccountByNameUseCase.execute(playerExist.getNickname());
        assertEquals(playerNewUUID.getUuid(), resultAccount.getValue().getPlayer().getUuid());
    }

    @Test
    void testLoadAccountOnline(){
        Player playerExist = new Player(UUID.randomUUID(), "Nullplague");
        createAccountUseCase.execute(playerExist.getUuid(), playerExist.getNickname());

        Player playerNewName = new Player(playerExist.getUuid(), "Null");
        playerJoinListenerOnline.loadAccount(new MockPlayer(playerNewName));

        Result<List<Account>> result = getOfflineAccountsUseCase.execute();
        long count = result.getValue().stream().filter(acc -> acc.getPlayer().getUuid().equals(playerExist.getUuid())).count();
        assertEquals(1, count);

        Result<Account> resultAccount = getAccountByUUIDUseCase.execute(playerExist.getUuid());
        assertEquals(playerNewName.getNickname(), resultAccount.getValue().getPlayer().getNickname());
    }

    public class MockPlayer implements IEntityCommands{
        Player player;
        public MockPlayer(Player player){
            this.player = player;
        }
        @Override
        public UUID getUniqueId() {
            return player.getUuid();
        }

        @Override
        public String getName() {
            return player.getNickname();
        }

        @Override
        public void sendMessage(String message) {

        }

        @Override
        public Object getRoot() {
            return null;
        }

        @Override
        public void playNotificationSound() {

        }

        @Override
        public boolean isOnline() {
            return false;
        }

        @Override
        public boolean hasPermission(String permission) {
            return false;
        }

        @Override
        public void kickPlayer(String message) {

        }

        @Override
        public IEntityGUI asEntityGUI() {
            return null;
        }
    }
}
